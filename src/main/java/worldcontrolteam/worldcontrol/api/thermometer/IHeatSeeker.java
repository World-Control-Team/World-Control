package worldcontrolteam.worldcontrol.api.thermometer;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IHeatSeeker {

    int getHeat(World world, BlockPos pos, TileEntity entity);

    boolean canUse(World world, BlockPos pos, TileEntity tile);

    String getUnloalizedName();
}
