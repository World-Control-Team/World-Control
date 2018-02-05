package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import worldcontrolteam.worldcontrol.inventory.ISlotItemFilter;

import javax.annotation.Nullable;

public abstract class TileEntityBaseAverageCounter extends TileEntity implements ITickable, ISlotItemFilter {

    public short period;
    public ItemStackHandler inventory;

    public TileEntityBaseAverageCounter() {
        this.period = 1;
        this.inventory = new ItemStackHandler(1);
    }

    @Override
    public void update() {
        countAverage();
    }

    public abstract void countAverage();

    public abstract int getAverage();

    public abstract String getPowerTranslateKey();

    public void setPeriod(short newPeriod) {
        period = newPeriod;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory);
        return super.getCapability(capability, facing);
    }
}