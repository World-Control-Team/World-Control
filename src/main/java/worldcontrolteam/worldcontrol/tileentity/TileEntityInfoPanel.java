package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.screen.IScreenContainer;
import worldcontrolteam.worldcontrol.screen.IScreenElement;

import java.util.ArrayList;

/**
 * Created by dmf444 on 8/15/2017. Code originally written for World-Control.
 */
public class TileEntityInfoPanel extends TileEntity {
    private ArrayList<IScreenElement> screenElements;

    public BlockPos origin;
    public BlockPos end;

    public EnumFacing facing;

    public TileEntityInfoPanel() {

    }

    boolean isAreaValid(BlockPos o, BlockPos e) {
        for (int x = o.getX(); x < e.getX(); x++) {
            for (int y = o.getY(); y < e.getY(); y++) {
                for (int z = o.getZ(); z < e.getZ(); z++) {
                    final BlockPos pos1 = new BlockPos(x, y, z);
                    if (!(world.getBlockState(pos1).getBlock() instanceof IScreenContainer)) {
                        return false;
                    }
                    IScreenContainer c = (IScreenContainer) world.getBlockState(pos1).getBlock();
                    if (!(c.getFacing(world, pos1) == facing)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    boolean inSamePlane(BlockPos c) {
        switch (facing) {
            case UP:
            case DOWN:
                return c.getY() == origin.getY();
            case NORTH:
            case SOUTH:
                return c.getZ() == origin.getZ();
            default:
                return c.getX() == origin.getX();
        }
    }

    boolean tryToAdd(BlockPos aPos) {
        if (!inSamePlane(aPos)) {
            return false;
        }
        if (aPos.getX() < origin.getX() || aPos.getY() < origin.getY() || aPos.getZ() < origin.getZ()) {

        }
    }

}
