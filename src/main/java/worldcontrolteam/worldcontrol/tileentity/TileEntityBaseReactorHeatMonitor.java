package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import worldcontrolteam.worldcontrol.utils.RedstoneHelper;

public abstract class TileEntityBaseReactorHeatMonitor extends TileEntity implements ITickable {

    protected BlockPos referenceBlock;
    private int threshhold = 500;
    private boolean outputInverse = false;

    public TileEntityBaseReactorHeatMonitor() {

    }

    @Override
    public void update() {
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

    public void setThreshhold(int updateT){
        if(updateT > 15000)
            updateT = 15000;
        if(updateT < 0)
            updateT = 0;
        this.threshhold = updateT;
    }

    public void setInverse(boolean updateInverse) {
        this.outputInverse = updateInverse;
    }


    public abstract int getCurrentHeat();
}
