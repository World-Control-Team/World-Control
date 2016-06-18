package worldcontrolteam.worldcontrol.crossmod.IndustrialCraft;

import ic2.api.reactor.IReactor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.thermometer.IHeatSeeker;

public class IC2ReactorHeat implements IHeatSeeker {

    @Override
    public int getHeat(World world, BlockPos pos, TileEntity entity) {
        IReactor reactor = null;
        if(entity instanceof IReactor){
            reactor =(IReactor)entity;
        }
        if(reactor == null){
            reactor = ((ic2.core.block.reactor.tileentity.TileEntityReactorChamberElectric)entity).getReactorInstance();
        }
        return reactor.getHeat();
    }

    @Override
    public boolean canUse(World world, BlockPos pos, TileEntity tile) {
        if(tile instanceof IReactor)return true;
        if(tile instanceof ic2.core.block.reactor.tileentity.TileEntityReactorChamberElectric)return true;
        return false;
    }

    @Override
    public String getUnloalizedName() {
        return "item.thermo.IC2";
    }
}
