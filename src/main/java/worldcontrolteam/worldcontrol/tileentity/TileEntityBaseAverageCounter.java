package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandler;
import worldcontrolteam.worldcontrol.inventory.ISlotItemFilter;

public abstract class TileEntityBaseAverageCounter extends TileEntity implements IItemHandler, ITickable, ISlotItemFilter {

    public short period;
    ItemStack stack;

    public TileEntityBaseAverageCounter() {
        this.period = 1;
        this.stack = ItemStack.EMPTY;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return stack;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stackl, boolean simulate) {
        if (stackl != ItemStack.EMPTY)
            if (this.stack.getCount() < 5) {
                //TODO: Finish this code...
            }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
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
}
