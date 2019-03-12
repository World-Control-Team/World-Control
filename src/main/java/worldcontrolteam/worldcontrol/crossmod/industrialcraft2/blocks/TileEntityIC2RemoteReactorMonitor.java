package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.item.IElectricItem;
import ic2.api.reactor.IReactor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.IC2Module;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.ReactorUtils;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2ReactorCard;
import worldcontrolteam.worldcontrol.inventory.ISlotItemFilter;
import worldcontrolteam.worldcontrol.items.ItemUpgrade;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;
import worldcontrolteam.worldcontrol.utils.NBTUtils;
import worldcontrolteam.worldcontrol.utils.WCConfig;

import javax.annotation.Nonnull;

/**
 * Created by dmf444 on 10/25/2017. Code originally written for World-Control.
 */
public class TileEntityIC2RemoteReactorMonitor extends TileEntityBaseReactorHeatMonitor implements IItemHandler, IItemHandlerModifiable, ISlotItemFilter {

    private NonNullList<ItemStack> itemStack = NonNullList.<ItemStack>withSize(5, ItemStack.EMPTY);
    private static final double MIN_RANGE = 20;
    //TODO: Cap upgrade stacksize to 16 blocks
    private static final double ADDITIONAL_RANGE = 2.75f;

    private static final int BASE_STORAGE = 600;
    private static final int STORAGE_PER_UPGRADE = 10000;

    private BasicSink energySink;

    public TileEntityIC2RemoteReactorMonitor(){
        energySink = new BasicSink(this, BASE_STORAGE, 1);
    }

    @Override
    public void onLoad() {
        energySink.onLoad(); // notify the energy sink
    }

    @Override
    public void invalidate() {
        energySink.invalidate(); // notify the energy sink
        super.invalidate(); // this is important for mc!
    }

    @Override
    public void onChunkUnload() {
        energySink.onChunkUnload(); // notify the energy sink
    }

    @Override
    public int getCurrentHeat() {
        BlockPos ref = this.getReferenceBlock();
        if(ref != null) {
            IReactor reactor = ReactorUtils.getReactorAt(world, ref);
            if (reactor != null) {
                return reactor.getHeat();
            }
            return -1;
        }
        return -2;
    }

    @Override
    public boolean isOverHeated() {
        if(energySink.useEnergy(WCConfig.remoteMonitorPowerUseIC2)){
            return super.isOverHeated();
        }
        return false;
    }

    @Override
    public boolean isConnectionValid() {
        if(itemStack.get(0) != ItemStack.EMPTY){
            if(itemStack.get(0).getItem() instanceof IC2ReactorCard){
                ItemStack card = itemStack.get(0);
                if (card.hasTagCompound()) {
                    BlockPos pos = NBTUtils.getBlockPos(card.getTagCompound());
                    double distance = this.getPos().getDistance(pos.getX(), pos.getY(), pos.getZ());
                    if(distance <= ((itemStack.get(1).getCount() - 1) * ADDITIONAL_RANGE) + MIN_RANGE) {
                        this.setReferenceBlock(pos);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getSlots() {
        return this.itemStack.size();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.itemStack.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        //Modified from {@link SidedInvWrapper#extractItem}
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (slot == -1)
            return stack;

        ItemStack stackInSlot = this.getStackInSlot(slot);

        int m;
        if (!stackInSlot.isEmpty())
        {
            if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), getSlotLimit(slot)))
                return stack;

            if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
                return stack;


            m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();

            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackInSlot.getCount());
                    this.itemStack.set(slot, copy);
                }

                return stack;
            } else {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.splitStack(m);
                    copy.grow(stackInSlot.getCount());
                    this.itemStack.set(slot, copy);
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            }
        } else {

            m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
            if (m < stack.getCount()) {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    this.itemStack.set(slot, stack.splitStack(m));
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            } else {
                if (!simulate)
                    this.itemStack.set(slot,  stack);
                return ItemStack.EMPTY;
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        //Modified from {@link SidedInvWrapper#extractItem}
        if(amount == 0)
            return ItemStack.EMPTY;

        ItemStack stack = getStackInSlot(slot);
        if(stack.isEmpty()){
            return ItemStack.EMPTY;
        }

        if (simulate)
        {
            if (stack.getCount() < amount)
            {
                return stack.copy();
            }
            else
            {
                ItemStack copy = stack.copy();
                copy.setCount(amount);
                return copy;
            }
        }
        else
        {
            int m = Math.min(stack.getCount(), amount);
            ItemStack ret = this.decrStackSize(slot, m);
            this.markDirty();
            return ret;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != ItemStack.EMPTY)
            if (stack.getCount() > amount) {
                stack = stack.splitStack(amount);
                markDirty();
            } else {
                this.itemStack.set(slot, ItemStack.EMPTY);
            }
        return stack;
    }

    public void setStackInSlot(int slot, @Nonnull ItemStack stack){
        this.itemStack.set(slot, stack);
        if (stack != ItemStack.EMPTY && stack.getCount() > this.getSlotLimit(slot)) {
            stack.setCount(this.getSlotLimit(slot));
        }
    }

    public double getEnergy(){
        return this.energySink.getEnergyStored();
    }

    public void setEnergy(int energyIn){
        this.energySink.setEnergyStored(energyIn);
    }

    public double getMaxStorage(){
        return this.energySink.getCapacity();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        energySink.readFromNBT(nbttagcompound);
        for(int i=0; i < itemStack.size(); i++){
            NBTTagCompound tag = nbttagcompound.getCompoundTag("stack"+i);
            ItemStack stack = new ItemStack(tag);
            this.itemStack.set(i, stack);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound = super.writeToNBT(nbttagcompound);
        energySink.writeToNBT(nbttagcompound);
        for(int i=0; i < itemStack.size(); i++){
            ItemStack stack = this.itemStack.get(i);
            NBTTagCompound tag = stack.writeToNBT(new NBTTagCompound());
            nbttagcompound.setTag("stack"+i, tag);
        }
        return nbttagcompound;
    }

    @Override
    public Block getBlock() {
        return IC2Module.REMOTE_THERMO_MONITOR;
    }

    public String getRenderType() {
        return "full_block";
    }

    @Override
    public boolean isItemValid(int slotIndex, ItemStack itemstack) {
        switch (slotIndex) {
            case 1:
                if (Item.getIdFromItem(itemstack.getItem()) == Item.getIdFromItem(IC2Module.suBattery.getItem()))
                    return true;
                if (itemstack.getItem() instanceof IElectricItem) {
                    IElectricItem item = (IElectricItem) itemstack.getItem();
                    if (item.canProvideEnergy(itemstack) && item.getTier(itemstack) <= this.energySink.getSinkTier()) {
                        return true;
                    }
                }
                return false;
            case 0:
                return itemstack.getItem() instanceof IC2ReactorCard;// || itemstack.getItem() instanceof ItemCard55Reactor;
            default:
                return itemstack.isItemEqual(IC2Module.transformerUpgrade)
                        || itemstack.isItemEqual(IC2Module.energyUpgrade)
                        || (itemstack.getItem() instanceof ItemUpgrade && itemstack.getItemDamage() == ItemUpgrade.DAMAGE_RANGE);
        }
    }
}
