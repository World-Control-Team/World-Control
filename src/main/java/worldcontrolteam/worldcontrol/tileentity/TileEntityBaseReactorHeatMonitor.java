package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import worldcontrolteam.worldcontrol.utils.RedstoneHelper;

public abstract class TileEntityBaseReactorHeatMonitor extends TileEntity implements ITickable {

    private int threshhold = 500;
    private boolean outputInverse = false;
    protected BlockPos referenceBlock;

    public TileEntityBaseReactorHeatMonitor(){

    }

    @Override
    public void update() {
        if(this.isConnectionValid()) {
            if (!outputInverse) {
                if (this.getCurrentHeat() >= threshhold) {
                    this.world.setBlockState(this.getPos(), this.getWorld().getBlockState(this.getPos()).withProperty(RedstoneHelper.POWERED, true), 3);
                } else {
                    this.world.setBlockState(this.getPos(), this.getWorld().getBlockState(this.getPos()).withProperty(RedstoneHelper.POWERED, false), 3);
                }
            } else {
                if (this.getCurrentHeat() <= threshhold) {
                    this.world.setBlockState(this.getPos(), this.getWorld().getBlockState(this.getPos()).withProperty(RedstoneHelper.POWERED, true), 3);
                } else {
                    this.world.setBlockState(this.getPos(), this.getWorld().getBlockState(this.getPos()).withProperty(RedstoneHelper.POWERED, false), 3);
                }
            }
        }
    }

    public void setThreshhold(int updateT){
        if(updateT > 1000000)
            updateT = 1000000;
        if(updateT < 0)
            updateT = 0;
        this.threshhold = updateT;
    }

    public int getThreshhold(){
        return threshhold;
    }

    public boolean getInversion(){
        return outputInverse;
    }

    public void setInverse(boolean updateInverse){
        this.outputInverse = updateInverse;
    }


    public abstract int getCurrentHeat();
    public abstract boolean isConnectionValid();

    public boolean isOverHeated(){
        if (!outputInverse) {
            if (this.getCurrentHeat() >= threshhold) {
                return true;
            } else {
                return false;
            }
        }else{
            if (this.getCurrentHeat() <= threshhold) {
                return true;
            }else {
                return false;
            }
        }
    }
}
