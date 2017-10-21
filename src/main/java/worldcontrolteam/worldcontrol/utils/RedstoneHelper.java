package worldcontrolteam.worldcontrol.utils;


import net.minecraft.block.properties.PropertyBool;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedstoneHelper {

    public static final PropertyBool POWERED = PropertyBool.create("powered");

    private static boolean isPoweredWire(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == Blocks.REDSTONE_WIRE && Blocks.REDSTONE_WIRE.getStrongPower(world.getBlockState(pos), world, pos, EnumFacing.DOWN) > 0;
    }

    public static void checkPowered(World world, TileEntity tileentity) {
        if (world != null && tileentity != null && tileentity instanceof IRedstoneConsumer) {
            boolean powered = world.isBlockPowered(tileentity.getPos())
                    || isPoweredWire(world, tileentity.getPos().add(1, 0, 0))
                    || isPoweredWire(world, tileentity.getPos().add(-1, 0, 0))
                    || isPoweredWire(world, tileentity.getPos().add(0, 0, 1))
                    || isPoweredWire(world, tileentity.getPos().add(0, 0, -1));
            ((IRedstoneConsumer) tileentity).setPowered(powered);
        }
    }


    public interface IRedstoneConsumer {
        boolean getPowered();

        void setPowered(boolean value);
    }
}
