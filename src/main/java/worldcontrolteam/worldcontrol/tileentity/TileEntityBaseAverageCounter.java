package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandler;

public abstract class TileEntityBaseAverageCounter extends TileEntity implements IItemHandler, ITickable{

    ItemStack stack;

    public TileEntityBaseAverageCounter(){

    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return stack;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stackl, boolean simulate) {
        if (stackl != null) {
            if(this.stack.stackSize < 5){

            }
        }
        return null;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public void update() {
        countAverage();
    }

    abstract void countAverage();

    abstract void getInfo();
}
