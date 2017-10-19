package worldcontrolteam.worldcontrol.crossmod.extremereactors;

import erogenousbeef.bigreactors.api.IHeatEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.thermometer.IHeatSeeker;

/**
 * Created by dmf444 on 10/15/2017. Code originally written for World-Control.
 */
public class ExtremeReactorHeat implements IHeatSeeker {
    @Override
    public int getHeat(World world, BlockPos pos, TileEntity entity) {
        if (entity instanceof IHeatEntity) {
            IHeatEntity tile = (IHeatEntity) entity;
            return (int) tile.getHeat();
        }
        return 0;
    }

    @Override
    public boolean canUse(World world, BlockPos pos, TileEntity tile) {
        return tile instanceof IHeatEntity;
    }

    @Override
    public String getUnloalizedName() {
        return "extremereactors";
    }
}
