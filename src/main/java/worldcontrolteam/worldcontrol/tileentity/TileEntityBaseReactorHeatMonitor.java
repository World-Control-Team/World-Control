package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityBaseReactorHeatMonitor extends TileEntity implements ITickable {

    boolean prevOverheated;
    private int threshhold = 500;
    private boolean outputInverse = false;
    private BlockPos referenceBlock;

    public TileEntityBaseReactorHeatMonitor() {

    }

    @Override
    public void update() {
        //WCUtility.log(Level.FATAL, outputInverse);
        boolean overheated = isOverHeated();
        if(prevOverheated != overheated){
            prevOverheated = overheated;
            this.markForUpdate();
        }
    }

    public int getThreshhold() {
        return threshhold;
    }

    public void setThreshhold(int updateT) {
        if (updateT > 1000000)
            updateT = 1000000;
        if (updateT < 0)
            updateT = 0;
        this.threshhold = updateT;
        this.markForUpdate();
    }

    public boolean getInversion() {
        return outputInverse;
    }

    public BlockPos getReferenceBlock() {
        return referenceBlock;
    }

    public void setReferenceBlock(BlockPos pos) {
        this.referenceBlock = pos;
    }

    public void setInverse(boolean updateInverse) {
        this.outputInverse = updateInverse;
        this.markForUpdate();
    }


    public abstract int getCurrentHeat();

    public abstract boolean isConnectionValid();

    public String getRenderType() {
        return "half_block";
    }

    public boolean isOverHeated() {
        boolean test = false;
        if (this.isConnectionValid()) {
            if (!outputInverse) {
                if (this.getCurrentHeat() >= threshhold) {
                    test = true;
                } else {
                    test = false;
                }
            } else {
                if (this.getCurrentHeat() <= threshhold) {
                    test = true;
                } else {
                    test = false;
                }
            }
        }
        return test;
    }

    public void markForUpdate()
    {
        world.notifyNeighborsOfStateChange(getPos(), this.getBlock(), false);
        world.notifyBlockUpdate(getPos(), this.getWorld().getBlockState(this.pos), this.getWorld().getBlockState(this.pos), 3);
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        syncData.setInteger("threshold", this.threshhold);
        syncData.setBoolean("inverted", this.outputInverse);
        return new SPacketUpdateTileEntity(this.getPos(), 1, syncData);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(net.minecraft.network.NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.threshhold = pkt.getNbtCompound().getInteger("threshold");
        this.outputInverse = pkt.getNbtCompound().getBoolean("inverted");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.threshhold = nbttagcompound.getInteger("threshold");
        this.outputInverse = nbttagcompound.getBoolean("inverted");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound = super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("inverted", this.outputInverse);
        nbttagcompound.setInteger("threshold", this.threshhold);
        return nbttagcompound;
    }

    public abstract Block getBlock();
}
