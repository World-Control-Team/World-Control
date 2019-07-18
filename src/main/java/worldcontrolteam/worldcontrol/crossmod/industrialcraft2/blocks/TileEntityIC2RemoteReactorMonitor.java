package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.item.IElectricItem;
import ic2.api.reactor.IReactor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
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
public class TileEntityIC2RemoteReactorMonitor extends TileEntityBaseReactorHeatMonitor implements  ISlotItemFilter {

    private final ItemStackHandler inventory = new ItemStackHandler(5);
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

    public int getSlots() {
        return this.itemStack.size();
    }

    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return this.itemStack.get(slot);
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
    public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        }
        return super.getCapability(cap, side);
    }


    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        energySink.readFromNBT(nbttagcompound);
        inventory.deserializeNBT(nbttagcompound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound = super.writeToNBT(nbttagcompound);
        energySink.writeToNBT(nbttagcompound);
        nbttagcompound.merge(inventory.serializeNBT());
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
