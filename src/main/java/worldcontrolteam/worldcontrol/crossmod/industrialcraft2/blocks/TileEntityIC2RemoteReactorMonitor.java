package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.IC2Items;
import ic2.api.item.IElectricItem;
import ic2.api.reactor.IReactor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.ReactorUtils;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2ReactorCard;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.inventory.ISlotItemFilter;
import worldcontrolteam.worldcontrol.items.ItemUpgrade;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;
import worldcontrolteam.worldcontrol.utils.NBTUtils;

/**
 * Created by dmf444 on 10/25/2017. Code originally written for World-Control.
 */
public class TileEntityIC2RemoteReactorMonitor extends TileEntityBaseReactorHeatMonitor implements IEnergySink, IInventory, ISlotItemFilter{

    private final int CARD_SLOT = 0;
    private final int POWER_SLOT = 1;
    private final int MIN_SLOT = 2;
    private final int MAX_SLOT = 4;
    private NonNullList<ItemStack> invContents = NonNullList.<ItemStack>withSize(5, ItemStack.EMPTY);

    private static final double MIN_RANGE = 20;
    //TODO: Cap upgrade stacksize to 16 blocks
    private static final double ADDITIONAL_RANGE = 2.1875f;
    private boolean addedToENet = false;
    private double energy;

    //TODO: Re-add Storage Size Upgrades
    private double maxStorage = 600;
    public final double STORAGE_INCREMENT = 10000;
    public static final int BASE_STORAGE = 600;

    public TileEntityIC2RemoteReactorMonitor(){

    }

    @Override
    public int getCurrentHeat() {
        BlockPos ref = this.getReferenceBlock();
        if(ref != null) {
            IReactor reactor = ReactorUtils.getReactorAt(world, ref);
            if (reactor != null) {
                return reactor.getHeat();
            }
        }
        return -1;
    }

    @Override
    public boolean isConnectionValid() {
        if(invContents.get(CARD_SLOT) != ItemStack.EMPTY){
            if(invContents.get(CARD_SLOT).getItem() instanceof IC2ReactorCard){
                ItemStack card = invContents.get(CARD_SLOT);
                if (card.hasTagCompound()) {
                    BlockPos pos = NBTUtils.getBlockPos(card.getTagCompound());
                    double distance = this.getPos().getDistance(pos.getX(), pos.getY(), pos.getZ());

                    //Get Range Upgrade Count
                    int count = this.getItemCount(WCContent.UPGRADE);

                    if(distance <= (count * ADDITIONAL_RANGE) + MIN_RANGE) {
                        this.setReferenceBlock(pos);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public void update(){
        if(!addedToENet && !getWorld().isRemote){
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            addedToENet = !addedToENet;
        }
        super.update();
    }

    public void invalidate() {
        onChunkUnload();
        super.invalidate();
    }

    public void onChunkUnload() {
        if (addedToENet && !getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            addedToENet = false;
        }
    }

    @Override
    public double getDemandedEnergy() {
        return maxStorage - energy;
    }

    @Override
    public int getSinkTier() {
        int count = this.getItemCount(IC2Items.getItem("upgrade", "transformer").getItem());
        count = Math.min(count, 4) + 1;
        return count;
    }

    @Override
    public double injectEnergy(EnumFacing enumFacing, double amount, double voltage) {
        energy += amount;
        double left = 0.0;

        if (energy > maxStorage) {
            left = energy - maxStorage;
            energy = maxStorage;
        }
        setEnergy(energy);
        return left;
    }

    public void setEnergy(double value) {
        energy = value;
    }

    public double getEnergy() {
        return energy;
    }

    public double getMaxStorage() {
        return maxStorage;
    }

    public void setMaxStorage(double maxStorage) {
        this.maxStorage = maxStorage;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing enumFacing) {
        return this.getWorld().getBlockState(this.getPos()).getValue(BlockBasicRotate.FACING) != enumFacing;
    }

    @Override
    public boolean isItemValid(int slotIndex, ItemStack itemStack) {
        switch (slotIndex){
            case POWER_SLOT:
                return itemStack.getItem() instanceof IElectricItem && ((IElectricItem)itemStack.getItem()).canProvideEnergy(itemStack);
            case CARD_SLOT:
                return itemStack.getItem() instanceof IC2ReactorCard;
            default:
                return itemStack.getItem() == IC2Items.getItem("upgrade", "transformer").getItem() ||
                        itemStack.getItem() == IC2Items.getItem("upgrade", "energy_storage").getItem() ||
                        (itemStack.getItem() == WCContent.UPGRADE && itemStack.getItemDamage() == ItemUpgrade.DAMAGE_RANGE);
        }
    }

    @Override
    public int getSizeInventory() {
        return this.invContents.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.invContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.invContents.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.invContents, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.invContents, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.invContents.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.invContents.set(index, stack);

        if(!flag)
            this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(getPos()) == this && player.getDistanceSq(getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D) <= 64D;
    }

    @Override
    public void openInventory(EntityPlayer player) { }

    @Override
    public void closeInventory(EntityPlayer player) { }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) { return isItemValid(index, stack); }

    @Override
    public int getField(int id) { return 0; }

    @Override
    public void setField(int id, int value) { }

    @Override
    public int getFieldCount() { return 0; }

    @Override
    public void clear() { this.invContents.clear(); }

    @Override
    public String getName() { return "vaca"; }

    @Override
    public boolean hasCustomName() { return false; }

    private int getItemCount(Item item) {
        int count = 0;
        for(int i = MIN_SLOT; i < MAX_SLOT; i++){
            if(!(invContents.get(i) == ItemStack.EMPTY) && invContents.get(i).getItem() == item) {
                count += invContents.get(i).getCount();
            }
        }
        return count;
    }
}
