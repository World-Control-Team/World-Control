package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;
import worldcontrolteam.worldcontrol.utils.GuiLib;
import worldcontrolteam.worldcontrol.utils.RedstoneHelper;

public class BlockIC2ReactorMonitor extends BlockBasicRotate{

    public static IProperty RENDER_TYPE = PropertyEnum.create("rendertype", RenderType.class);

    public BlockIC2ReactorMonitor() {
        super(Material.ANVIL, "ic2_reactor_monitor");
        this.setDefaultState(this.getDefaultState().withProperty(RedstoneHelper.POWERED, false).withProperty(RENDER_TYPE, RenderType.NOT_FOUND));
    }

    @Override
    public TileEntity getTile(World world, int meta) {
        return new TileEntityIC2ReactorMonitor();
    }

    @Override
    public boolean hasGUI() {
        return true;
    }

    @Override
    public int guiID() {
        return GuiLib.IC2_HEAT_MONITOR;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing){
        return ((Boolean)state.getValue(RedstoneHelper.POWERED)) ? 15 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta)).withProperty(RedstoneHelper.POWERED, false);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(FACING).getIndex() * 2 + state.getValue(RedstoneHelper.POWERED).compareTo(true);
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, new IProperty[]{FACING, RedstoneHelper.POWERED, RENDER_TYPE});
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if(tile instanceof TileEntityBaseReactorHeatMonitor){
            if(((TileEntityBaseReactorHeatMonitor)tile).isConnectionValid()){
                if(!((TileEntityBaseReactorHeatMonitor)tile).isOverHeated()){
                    return state.withProperty(RENDER_TYPE, RenderType.NORMAL);
                }else{
                    return state.withProperty(RENDER_TYPE, RenderType.OVER_HEAT);
                }
            }
        }
        return state.withProperty(RENDER_TYPE, RenderType.NOT_FOUND);
    }

    public enum RenderType implements IStringSerializable{
        NORMAL("normal"),
        NOT_FOUND("none"),
        OVER_HEAT("hot");

        private String name;
        RenderType(String name){
            this.name = name;
        }

        @Override
        public String getName(){
            return this.name;
        }


    }


}
