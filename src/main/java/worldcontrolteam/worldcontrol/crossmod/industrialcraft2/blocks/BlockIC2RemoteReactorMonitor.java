package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;
import worldcontrolteam.worldcontrol.utils.GuiLib;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import static worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks.BlockIC2ReactorMonitor.RENDER_TYPE;

public class BlockIC2RemoteReactorMonitor extends BlockBasicRotate {

    public BlockIC2RemoteReactorMonitor() {
        super(Material.IRON, "ic2_remote_reactor_monitor");
        this.setDefaultState(this.getDefaultState().withProperty(RENDER_TYPE, BlockIC2ReactorMonitor.RenderType.NOT_FOUND).withProperty(BlockBasicRotate.FACING, EnumFacing.UP));
    }

    @Override
    public TileEntity createTile(World world, int meta) {
        return new TileEntityIC2RemoteReactorMonitor();
    }

    @Override
    public boolean hasGUI() {
        return true;
    }

    @Override
    public int guiID() {
        return GuiLib.IC2_REMOTE_HEAT_MONITOR;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        if (facing == state.getValue(FACING).getOpposite())
            return 0;
        return WCUtility.getTileEntity(world, pos, TileEntityIC2ReactorMonitor.class).map(t -> t.isOverHeated() ? 15 : 0).orElse(0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, RENDER_TYPE);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache) world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (((TileEntityBaseReactorHeatMonitor) tile).isConnectionValid()) {
            return WCUtility.getTileEntity(world, pos, TileEntityIC2RemoteReactorMonitor.class).map(t -> t.isOverHeated() ? state.withProperty(RENDER_TYPE, BlockIC2ReactorMonitor.RenderType.OVER_HEAT) : state.withProperty(RENDER_TYPE, BlockIC2ReactorMonitor.RenderType.NORMAL)).orElse(state.withProperty(RENDER_TYPE, BlockIC2ReactorMonitor.RenderType.NOT_FOUND));
        }
        return state.withProperty(RENDER_TYPE, BlockIC2ReactorMonitor.RenderType.NOT_FOUND);
    }
}
