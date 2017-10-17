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
        if(!outputInverse) {
            if (this.getCurrentHeat() >= threshhold) {
                this.world.setBlockState(this.getPos(), this.getWorld().getBlockState(this.getPos()).withProperty(RedstoneHelper.POWERED, true), 3);
            } else{
                this.world.setBlockState(this.getPos(), this.getWorld().getBlockState(this.getPos()).withProperty(RedstoneHelper.POWERED, false), 3);
            }
        } else {
            if (this.getCurrentHeat() <= threshhold) {
                this.world.setBlockState(this.getPos(), this.getWorld().getBlockState(this.getPos()).withProperty(RedstoneHelper.POWERED, true), 3);
            } else{
                this.world.setBlockState(this.getPos(), this.getWorld().getBlockState(this.getPos()).withProperty(RedstoneHelper.POWERED, false), 3);
            }
        }
    }

    public void setThreshhold(int updateT){
        this.threshhold = updateT;
    }

    public void setInverse(boolean updateInverse){
        this.outputInverse = updateInverse;
    }


    public abstract int getCurrentHeat();
}
