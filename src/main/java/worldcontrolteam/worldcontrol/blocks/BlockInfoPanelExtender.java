package worldcontrolteam.worldcontrol.blocks;

import com.google.common.collect.ImmutableMap;
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import worldcontrolteam.worldcontrol.client.ClientUtil;
import worldcontrolteam.worldcontrol.screen.IScreenContainer;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanelExtender;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.Optional;

@SuppressWarnings("Duplicates")
public class BlockInfoPanelExtender extends BlockBasicRotate implements IScreenContainer {
    private static ImmutableMap<EnumFacing, Pair<EnumFacing, EnumFacing>> facings;

    static {
        ImmutableMap.Builder<EnumFacing, Pair<EnumFacing, EnumFacing>> builder = new ImmutableMap.Builder<>();
        // dir, down, left
        builder.put(EnumFacing.UP, new Pair<>(EnumFacing.SOUTH, EnumFacing.WEST));
        builder.put(EnumFacing.DOWN, new Pair<>(EnumFacing.NORTH, EnumFacing.EAST));
        builder.put(EnumFacing.NORTH, new Pair<>(EnumFacing.DOWN, EnumFacing.EAST));
        builder.put(EnumFacing.SOUTH, new Pair<>(EnumFacing.DOWN, EnumFacing.WEST));
        builder.put(EnumFacing.EAST, new Pair<>(EnumFacing.DOWN, EnumFacing.NORTH));
        builder.put(EnumFacing.WEST, new Pair<>(EnumFacing.DOWN, EnumFacing.SOUTH));
        facings = builder.build();
    }

    public BlockInfoPanelExtender(String name, boolean advanced) {
            super(Material.IRON, name);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        WCUtility.getTileEntity(world, pos, TileEntityInfoPanelExtender.class).ifPresent(TileEntityInfoPanelExtender::init);
        for (EnumFacing f : EnumFacing.VALUES) {
            IBlockState b = world.getBlockState(pos.offset(f));
            if (b.getBlock() instanceof IScreenContainer) {
                BlockPos pos1 = ((IScreenContainer) b.getBlock()).getOrigin(world, pos.offset(f));
                if (pos1 != null) {
                    TileEntity te = world.getTileEntity(pos1);
                    if (te instanceof TileEntityInfoPanel) {
                        ((TileEntityInfoPanel) te).tryToAdd(pos);
                    }
                }
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        Optional<TileEntityInfoPanelExtender> optte = WCUtility.getTileEntity(world, pos, TileEntityInfoPanelExtender.class);
        super.breakBlock(world, pos, state);
        world.removeTileEntity(pos);
        optte.ifPresent(te_ -> {
            if (te_.origin != null) {
                WCUtility.getTileEntity(world, te_.origin, TileEntityInfoPanel.class).ifPresent(TileEntityInfoPanel::reInit);
            }
        });
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(FACING).add(BlockInfoPanel.STATE).build();
    }

    @Override
    public TileEntity createTile(World world, int meta) {
        return new TileEntityInfoPanelExtender();
    }

    @Override
    public boolean hasGUI() {
        return false;
    }

    @Override
    public int guiID() {
        return 0;
    }

    @Override
    public EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock() == this ? (EnumFacing) worldIn.getBlockState(pos).getProperties().get(FACING) : EnumFacing.DOWN;
    }

    @Override
    public BlockPos getOrigin(IBlockAccess worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityInfoPanelExtender) {
            return ((TileEntityInfoPanelExtender)tile).origin;
        }
        else {
            return null;
        }
    }

    @Override
    public void registerModels(ModelRegistryEvent event) {
        ClientUtil.registerToNormalWithoutMapper(this);
    }

    @Override
    public boolean isValid(World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        return tile instanceof TileEntityInfoPanelExtender;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState estate = (IExtendedBlockState) state;

            BlockInfoPanel.InfoPanelState istate = new BlockInfoPanel.InfoPanelState();
            istate.color = 0;
            istate.power = true;  // todo: get these values

            EnumFacing f = getFacing(world, pos);

            istate.down = isConnectedTo(world, pos.offset(facings.get(f).getKey()), pos);
            istate.up = isConnectedTo(world, pos.offset(facings.get(f).getKey().getOpposite()), pos);
            istate.left = isConnectedTo(world, pos.offset(facings.get(f).getValue()), pos);
            istate.right = isConnectedTo(world, pos.offset(facings.get(f).getValue().getOpposite()), pos);

            estate = estate.withProperty(BlockInfoPanel.STATE, istate);
            return estate;
        }
        else {
            return state;
        }
    }

    private boolean isConnectedTo(IBlockAccess world, BlockPos offset, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityInfoPanelExtender) {
            TileEntityInfoPanelExtender tile1 = (TileEntityInfoPanelExtender) tile;
            if (tile1.origin == null) {
                return false;
            }
            Block block = world.getBlockState(offset).getBlock();
            if (block instanceof IScreenContainer) {
                IScreenContainer cont = (IScreenContainer) block;
                return WCUtility.compareBPos(cont.getOrigin(world, offset),tile1.origin);
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public void setOrigin(World worldIn, BlockPos posBlock, BlockPos posOrigin) {
        TileEntity tile = worldIn.getTileEntity(posBlock);
        if (tile instanceof TileEntityInfoPanelExtender) {
            ((TileEntityInfoPanelExtender)tile).origin = posOrigin;
        }
    }
}
