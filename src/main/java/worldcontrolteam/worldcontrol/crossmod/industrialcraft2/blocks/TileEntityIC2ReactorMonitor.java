package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.ReactorUtils;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;

public class TileEntityIC2ReactorMonitor extends TileEntityBaseReactorHeatMonitor {


    @Override
    public int getCurrentHeat() {
        IReactor reactor = ReactorUtils.getReactorAt(world, referenceBlock);
        if (reactor != null) {
            return reactor.getHeat();
        }
        return -1;
    }

    @Override
    public boolean isConnectionValid() {
        EnumFacing facing = this.getWorld().getBlockState(this.getPos()).getValue(BlockBasicRotate.FACING);
        BlockPos toCheck = this.getPos().offset(facing.getOpposite());
        if(toCheck instanceof IReactor || toCheck instanceof IReactorChamber){
            return true;
        }
        return false;
    }
}
