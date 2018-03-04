package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.screen.IScreenPart;
import worldcontrolteam.worldcontrol.screen.Screen;

/**
 * Created by dmf444 on 8/15/2017. Code originally written for World-Control.
 */
public class TileEntityInfoPanel extends TileEntity implements IScreenPart {
    public static final EnumFacing[][] FACING_ARRAY = new EnumFacing[][]{
            new EnumFacing[]{EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST},
            new EnumFacing[]{EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST},
            new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST},
            new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.WEST, EnumFacing.EAST},
            new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH},
            new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH}
    };

    public Screen screen = new Screen();

    private boolean isExtender;

    public void attemptConnection() {
        for(EnumFacing facing : FACING_ARRAY[getFacing().getIndex()]) {
            TileEntity tile = getWorld().getTileEntity(getPos().offset(facing));
            if(tile instanceof IScreenPart) {
                ((IScreenPart) tile).getScreen().attemptJoin(this);
            }
        }
    }

    @Override
    public Screen getScreen() {
        return screen;
    }

    @Override
    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public void updateData() {

    }

    public TileEntityInfoPanel(boolean isExtender) {
        this.isExtender = isExtender;
    }

    public TileEntityInfoPanel() {
        this(false);
    }

    public EnumFacing getFacing() {
        return getWorld().getBlockState(getPos()).getValue(BlockBasicRotate.FACING);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        isExtender = compound.getBoolean("extender");
        if (!isExtender) {
            screen.deserializeNBT(compound.getCompoundTag("screen"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("extender", isExtender);
        if (!isExtender)
            compound.setTag("screen", screen.serializeNBT());
        return super.writeToNBT(compound);
    }

    public boolean connectedTo(Side side) {
        EnumFacing sideFacing = FACING_ARRAY[getFacing().getIndex()][side.ordinal()];
        TileEntity tile = getWorld().getTileEntity(getPos().offset(sideFacing));
        if (tile instanceof IScreenPart) {
            return screen.isBlockPartOf(tile);
        }
        return false;
    }

    public enum Side {
        UP, DOWN, LEFT, RIGHT
    }
}