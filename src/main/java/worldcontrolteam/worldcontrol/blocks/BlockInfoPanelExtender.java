package worldcontrolteam.worldcontrol.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import worldcontrolteam.worldcontrol.client.ClientUtil;
import worldcontrolteam.worldcontrol.screen.IScreenContainer;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanelExtender;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.Optional;

public class BlockInfoPanelExtender extends BlockBasicRotate implements IScreenContainer {
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
                        if (((TileEntityInfoPanel) te).tryToAdd(pos)) {
                            WCUtility.getTileEntity(world, pos, TileEntityInfoPanelExtender.class).ifPresent(te_ -> te_.origin = pos1);
                        }
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
        return new BlockStateContainer.Builder(this).add(FACING).build();
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
    public EnumFacing getFacing(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock() == this ? (EnumFacing) worldIn.getBlockState(pos).getProperties().get(FACING) : EnumFacing.DOWN;
    }

    @Override
    public BlockPos getOrigin(World worldIn, BlockPos pos) {
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
}
