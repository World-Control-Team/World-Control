package worldcontrolteam.worldcontrol.blocks;

import com.google.common.collect.ImmutableMap;
import javafx.util.Pair;
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
import worldcontrolteam.worldcontrol.client.ClientUtil;
import worldcontrolteam.worldcontrol.screen.IScreenContainer;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;
import worldcontrolteam.worldcontrol.utils.WCUtility;

@SuppressWarnings("Duplicates")
public class BlockInfoPanel extends BlockBasicRotate implements IScreenContainer {
    private static ImmutableMap<EnumFacing, Pair<EnumFacing, EnumFacing>> facings;

    static {
        ImmutableMap.Builder<EnumFacing, Pair<EnumFacing, EnumFacing>> builder = new ImmutableMap.Builder<>();
        // dir, down, left
        builder.put(EnumFacing.UP, new Pair<>(EnumFacing.SOUTH, EnumFacing.WEST));
        builder.put(EnumFacing.DOWN, new Pair<>(EnumFacing.NORTH, EnumFacing.WEST));
        builder.put(EnumFacing.NORTH, new Pair<>(EnumFacing.DOWN, EnumFacing.EAST));
        builder.put(EnumFacing.SOUTH, new Pair<>(EnumFacing.DOWN, EnumFacing.WEST));
        builder.put(EnumFacing.EAST, new Pair<>(EnumFacing.DOWN, EnumFacing.SOUTH));
        builder.put(EnumFacing.WEST, new Pair<>(EnumFacing.DOWN, EnumFacing.NORTH));
        facings = builder.build();
    }

    public static class InfoPanelState {
        public boolean up, down, left, right, power;
        public int color;

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("up", up)
                    .append("down", down)
                    .append("left", left)
                    .append("right", right)
                    .append("power", power)
                    .append("color", color)
                    .build();
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
            return infoPanelState.toString();
        }
    };

    public BlockInfoPanel(Material material, String name, boolean advanced) {
        super(material, name);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        WCUtility.getTileEntity(world,pos, TileEntityInfoPanel.class).ifPresent(TileEntityInfoPanel::init);
    }

    public boolean isConnectedTo(IBlockAccess world, BlockPos p, BlockPos me) {
        return world.getBlockState(p).getBlock() instanceof IScreenContainer && WCUtility.compareBPos(((IScreenContainer)world.getBlockState(p).getBlock()).getOrigin(world, p),(me));
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState estate = (IExtendedBlockState) state;

            InfoPanelState istate = new InfoPanelState();
            istate.color = 3;
            istate.power = false;  // todo: get these values

            BlockPos origin = getOrigin(world, pos);
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

    public TileEntity createTile(World world, int meta) {
        TileEntityInfoPanel tileEntityInfoPanel = new TileEntityInfoPanel();
        tileEntityInfoPanel.setWorld(world);
        return tileEntityInfoPanel;
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
        return pos;
    }

    @Override
    public boolean isValid(World worldIn, BlockPos pos) {
        return true;
    }
}
