package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.blocks.BlockInfoPanel;
import worldcontrolteam.worldcontrol.screen.IScreenContainer;
import worldcontrolteam.worldcontrol.screen.IScreenElement;

import java.util.ArrayList;

/**
 * Created by dmf444 on 8/15/2017. Code originally written for World-Control.
 */
public class TileEntityInfoPanel extends TileEntity {
    public int color;
    private ArrayList<IScreenElement> screenElements;

    public BlockPos origin;
    public BlockPos end;

    public EnumFacing facing;

    public TileEntityInfoPanel() {
        this.color = 0;
    }

    public void init() {
        this.facing = (EnumFacing) world.getBlockState(getPos()).getProperties().get(BlockInfoPanel.FACING);

        searchForNeighbours();
    }

    public void reInit() {
        updateAllProviders(true);

        init();
    }

    private boolean isAreaValid(BlockPos o, BlockPos e) {
        for (int x = o.getX(); x <= e.getX(); x++) {
            for (int y = o.getY(); y <= e.getY(); y++) {
                for (int z = o.getZ(); z <= e.getZ(); z++) {
                    final BlockPos pos1 = new BlockPos(x, y, z);
                    if (!(world.getBlockState(pos1).getBlock() instanceof IScreenContainer)) {
                        return false;
                    }
                    IScreenContainer c = (IScreenContainer) world.getBlockState(pos1).getBlock();
                    if (!(c.getFacing(world, pos1) == facing)) {
                        return false;
                    }
                    if (!c.isValid(world, pos1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private void updateAllProviders(boolean nullify) {
        for (int x = origin.getX(); x < end.getX(); x++) {
            for (int y = origin.getY(); y < end.getY(); y++) {
                for (int z = origin.getZ(); z < end.getZ(); z++) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (nullify) {
                        ((IScreenContainer) (world.getBlockState(pos).getBlock())).setOrigin(world, pos, null);
                    }
                    else {
                        ((IScreenContainer) (world.getBlockState(pos).getBlock())).setOrigin(world, pos, getPos());
                    }
                }
            }
        }
    }

    private boolean inSamePlane(BlockPos c) {
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

    public boolean tryToAdd(BlockPos aPos) {
        if (!inSamePlane(aPos)) {
            return false;
        }
        if (aPos.getX() < origin.getX() || aPos.getY() < origin.getY() || aPos.getZ() < origin.getZ()) {
            BlockPos origin_ = new BlockPos(
                    Math.min(origin.getX(), aPos.getX()),
                    Math.min(origin.getY(), aPos.getY()),
                    Math.min(origin.getZ(), aPos.getZ())
            );
            if (isAreaValid(
                    origin_,
                    end
            )) {
                origin = origin_;
                updateAllProviders(false);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            BlockPos end_ = new BlockPos(
                    Math.max(end.getX(), aPos.getX()),
                    Math.max(end.getY(), aPos.getY()),
                    Math.max(end.getZ(), aPos.getZ())
            );
            if (isAreaValid(
                    origin,
                    end_
            )) {
                end = end_;
                updateAllProviders(false);
                return true;
            }
            else {
                return false;
            }
        }
    }

    private void searchForNeighbours() {
        EnumFacing left = EnumFacing.WEST; // negRelativeX
        EnumFacing down = EnumFacing.DOWN; // negRealtiveY

        switch (facing) {
            case UP:
            case DOWN:
                left = EnumFacing.WEST;
                down = EnumFacing.NORTH;
                break;
            case EAST:
            case WEST:
                left = EnumFacing.NORTH;
                down = EnumFacing.DOWN;
                break;
            case NORTH:
            case SOUTH:
                left = EnumFacing.WEST;
                down = EnumFacing.DOWN;
                break;
        }

        origin = getPos();
        end    = getPos();

        while (isAreaValid(origin, end)) {
            origin = origin.offset(left);
        }

        origin = origin.offset(left.getOpposite());

        while (isAreaValid(origin, end)) {
            origin = origin.offset(down);
        }

        origin = origin.offset(down.getOpposite());

        while (isAreaValid(origin, end)) {
            end = end.offset(left.getOpposite());
        }

        end = end.offset(left);

        while (isAreaValid(origin, end)) {
            end = end.offset(down.getOpposite());
        }

        end = end.offset(down);
    }

}
