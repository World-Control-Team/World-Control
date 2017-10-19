package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.utils.RedstoneHelper;

public class BlockIC2ReactorMonitor extends BlockBasicRotate{

    public BlockIC2ReactorMonitor(Material material, String name) {
        super(Material.ANVIL, "ic2_reactor_monitor");
    }

    @Override
    public TileEntity getTile(World world, int meta) {
        return new TileEntityIC2ReactorMonitor();
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
        return new BlockStateContainer(this, new IProperty[]{FACING, RedstoneHelper.POWERED});
    }


}
