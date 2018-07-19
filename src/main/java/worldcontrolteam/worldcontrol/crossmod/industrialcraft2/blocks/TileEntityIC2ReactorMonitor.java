package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.IC2Module;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.ReactorUtils;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;

public class TileEntityIC2ReactorMonitor extends TileEntityBaseReactorHeatMonitor {

    @Override
    public int getCurrentHeat() {
        BlockPos ref = this.getReferenceBlock();
        if (ref != null) {
            IReactor reactor = ReactorUtils.getReactorAt(world, ref);
            if (reactor != null) {
                return reactor.getHeat();
            }
        }
        return -1;
    }

    @Override
    public boolean isConnectionValid() {
        EnumFacing facing = this.getWorld().getBlockState(this.getPos()).getValue(BlockBasicRotate.FACING);
        this.setReferenceBlock(this.getPos().offset(facing.getOpposite()));
        TileEntity tile = getWorld().getTileEntity(this.getReferenceBlock());
        if (tile instanceof IReactor || tile instanceof IReactorChamber) {
            return true;
        }
        return false;
    }

    @Override
    public Block getBlock() {
        return IC2Module.THERMO_MONITOR;
    }
}
