package worldcontrolteam.worldcontrol.crossmod.tesla;

import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseAverageCounter;

public class TileEntityTeslaAverageCounter extends TileEntityBaseAverageCounter {

    protected static final int DATA_POINTS = 11 * 20;
    //Time values of 1,3,5,10
    protected long[] data = new long[DATA_POINTS];
    protected int index;
    protected long lastPower;
    private BaseTeslaContainer container;

    public TileEntityTeslaAverageCounter() {
        this.container = new BaseTeslaContainer();
        this.index = 0;
        this.lastPower = 0;
    }

    @Override
    public void countAverage() {
        if (!getWorld().isRemote) {
            index = (index + 1) % DATA_POINTS;
            long currentPower = container.getStoredPower();
            long powerChange = currentPower - lastPower;
            data[index] = powerChange;
            lastPower = currentPower;
        }
    }

    @Override
    public int getAverage() {
        int start = DATA_POINTS + index - period * 20;
        double sum = 0;
        for (int i = 0; i < period * 20; i++)
            sum += data[(start + i) % DATA_POINTS];
        long realAverage = Math.round(sum / period / 20);
        return (int) realAverage;
    }

    @Override
    public String getPowerTranslateKey() {
        return "tesla.average";
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

        super.readFromNBT(compound);
        this.container = new BaseTeslaContainer(compound.getCompoundTag("TeslaContainer"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setTag("TeslaContainer", this.container.serializeNBT());
        return compound;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return (T) this.container;

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == TeslaCapabilities.CAPABILITY_CONSUMER || capability == TeslaCapabilities.CAPABILITY_PRODUCER || capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return true;

        return super.hasCapability(capability, facing);
    }

    @Override
    public boolean isItemValid(int slotIndex, ItemStack itemStack) {
        return false;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        syncData.setShort("period", period);
        return new SPacketUpdateTileEntity(this.pos, 1, syncData);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        period = pkt.getNbtCompound().getShort("period");
    }

}
