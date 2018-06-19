package worldcontrolteam.worldcontrol.blocks;

import com.google.common.collect.ImmutableMap;
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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import worldcontrolteam.worldcontrol.client.ClientUtil;
import worldcontrolteam.worldcontrol.screen.IScreenContainer;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;
import worldcontrolteam.worldcontrol.utils.GuiLib;
import worldcontrolteam.worldcontrol.utils.RedstoneHelper;
import worldcontrolteam.worldcontrol.utils.WCUtility;

@SuppressWarnings("Duplicates")
public class BlockInfoPanel extends BlockBasicRotate implements IScreenContainer {
    private static ImmutableMap<EnumFacing, ImmutablePair<EnumFacing, EnumFacing>> facings;

    static {
        ImmutableMap.Builder<EnumFacing, ImmutablePair<EnumFacing, EnumFacing>> builder = new ImmutableMap.Builder<>();
        // dir, down, left
        builder.put(EnumFacing.UP, new ImmutablePair<>(EnumFacing.SOUTH, EnumFacing.WEST));
        builder.put(EnumFacing.DOWN, new ImmutablePair<>(EnumFacing.NORTH, EnumFacing.WEST));
        builder.put(EnumFacing.NORTH, new ImmutablePair<>(EnumFacing.DOWN, EnumFacing.EAST));
        builder.put(EnumFacing.SOUTH, new ImmutablePair<>(EnumFacing.DOWN, EnumFacing.WEST));
        builder.put(EnumFacing.EAST, new ImmutablePair<>(EnumFacing.DOWN, EnumFacing.SOUTH));
        builder.put(EnumFacing.WEST, new ImmutablePair<>(EnumFacing.DOWN, EnumFacing.NORTH));
        facings = builder.build();
    }

    public static class InfoPanelState {
        public boolean up, down, left, right, power;
        public int color;

        @Override
        public String toString() {
            return "asdf";
        }

        @Override
        public int hashCode() {
            return (up ? 1 : 0) + (down ? 2 : 0) + (left ? 4 : 0) + (right ? 8 : 0) + (power ? 16 : 0) + color << 5;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof InfoPanelState) {
                InfoPanelState that = (InfoPanelState) obj;
                return that.power == this.power && that.up == this.up && this.down == that.down && this.left == that.left && this.right == that.right && this.color == that.color;
            }
            return false;
        }
    }
    public static final IUnlistedProperty<InfoPanelState> STATE = new IUnlistedProperty<InfoPanelState>() {
        @Override
        public String getName() {
            return "state";
        }

        @Override
        public boolean isValid(InfoPanelState infoPanelState) {
            return true;
        }

        @Override
        public Class<InfoPanelState> getType() {
            return InfoPanelState.class;
        }

        @Override
        public String valueToString(InfoPanelState infoPanelState) {
            return "asdf";
        }
    };

    public BlockInfoPanel(Material material, String name, boolean advanced) {
        super(material, name);
        defaultCreativeTab();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        WCUtility.getTileEntity(world,pos, TileEntityInfoPanel.class).ifPresent(TileEntityInfoPanel::init);
    }

    public boolean isConnectedTo(IBlockAccess world, BlockPos p, BlockPos me) {
        Block block = world.getBlockState(p).getBlock();
        return block instanceof IScreenContainer && WCUtility.compareBPos(((IScreenContainer) block).getOrigin(world, p),(me));
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState estate = (IExtendedBlockState) state;

            InfoPanelState istate = new InfoPanelState();
            istate.color = 3;
            istate.power = RedstoneHelper.checkPowered(world, pos);  // todo: switch this tile to using IRedstoneConsumer

            BlockPos origin = pos;
            if (origin != null) {
                TileEntity te_origin = world.getTileEntity(origin);
                if (te_origin instanceof TileEntityInfoPanel) {
                    istate.color = ((TileEntityInfoPanel) te_origin).color;
                    istate.power = ((TileEntityInfoPanel) te_origin).power;
                }
            }

            EnumFacing f = getFacing(world, pos);

            istate.down = isConnectedTo(world, pos.offset(facings.get(f).getKey()), pos);
            istate.up = isConnectedTo(world, pos.offset(facings.get(f).getKey().getOpposite()), pos);
            istate.left = isConnectedTo(world, pos.offset(facings.get(f).getValue()), pos);
            istate.right = isConnectedTo(world, pos.offset(facings.get(f).getValue().getOpposite()), pos);

            estate = estate.withProperty(STATE, istate);
            return estate;
        }
        else {
            return state;
        }
    }

    @Override
    public void registerModels(ModelRegistryEvent event) {
        ClientUtil.registerToNormalWithoutMapper(this);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(FACING).add(STATE).build();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        WCUtility.getTileEntity(world, pos, TileEntityInfoPanel.class).ifPresent(te -> {
            te.updateAllProviders(true);
        });
        super.breakBlock(world, pos, state);
    }

    @Override
    public TileEntity createTile(World world, int meta) {
        TileEntityInfoPanel tileEntityInfoPanel = new TileEntityInfoPanel();
        tileEntityInfoPanel.setWorld(world);
        return tileEntityInfoPanel;
    }

    @Override
    public boolean hasGUI() {
        return true;
    }

    @Override
    public int guiID() {
        return GuiLib.INFO_PANEL;
    }

    @Override
    public EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock() == this ? (EnumFacing) worldIn.getBlockState(pos).getProperties().get(FACING) : EnumFacing.DOWN;
    }

    @Override
    public BlockPos getOrigin(IBlockAccess worldIn, BlockPos pos) {
        return pos;
    }

    @Override
    public boolean isValid(World worldIn, BlockPos pos) {
        return true;
    }
}
