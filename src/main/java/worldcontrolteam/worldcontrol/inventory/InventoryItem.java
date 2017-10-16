package worldcontrolteam.worldcontrol.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import worldcontrolteam.worldcontrol.items.ItemBaseCard;
import worldcontrolteam.worldcontrol.items.ItemRemotePanel;

public class InventoryItem implements IInventory, ISlotItemFilter {
	private String name = "Inventory Item";

	public static final int INV_SIZE = 1;
	private ItemStack[] inventory = new ItemStack[INV_SIZE];

	/** Provides NBT Tag Compound to reference */
	private final ItemStack invItem;

	public InventoryItem(ItemStack stack) {
		this.inventory[0] = ItemStack.EMPTY;
		this.invItem = stack;
		if(stack != ItemStack.EMPTY){
			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			readFromNBT(stack.getTagCompound());
		}
	}

	@Override
	public int getSizeInventory(){
		return inventory.length;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int slot){
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount){
		ItemStack stack = getStackInSlot(slot);
		if(stack != ItemStack.EMPTY)
			if(stack.getCount() > amount){
				stack = stack.splitStack(amount);
				markDirty();
			}else setInventorySlotContents(slot, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot){
		ItemStack stack = getStackInSlot(slot);
		if(stack != ItemStack.EMPTY)
			setInventorySlotContents(slot, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack){
		this.inventory[slot] = itemstack;

		if(itemstack != null && itemstack.getCount() > this.getInventoryStackLimit())
			itemstack.setCount(this.getInventoryStackLimit());
		markDirty();
	}

	@Override
	public String getName(){
		return name;
	}

	@Override
	public boolean hasCustomName(){
		return name.length() > 0;
	}

	@Override
	public ITextComponent getDisplayName(){
		return new TextComponentString(name);
	}

	@Override
	public int getInventoryStackLimit(){
		return 1;
	}

	@Override
	public void markDirty(){
		for(int i = 0; i < getSizeInventory(); ++i)
			if(getStackInSlot(i) != ItemStack.EMPTY && getStackInSlot(i).getCount() == 0)
				inventory[i] = ItemStack.EMPTY;
		if(!invItem.hasTagCompound())
			invItem.setTagCompound(new NBTTagCompound());
		writeToNBT(invItem.getTagCompound());
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player){
		return player.getHeldItem(player.getActiveHand()) == invItem;
	}

	@Override
	public void openInventory(EntityPlayer player){
	}

	@Override
	public void closeInventory(EntityPlayer player){
		markDirty();
	}

	@Override
	public boolean isItemValid(int slotIndex, ItemStack itemStack){
		return !(itemStack.getItem() instanceof ItemRemotePanel) && itemStack.getItem() instanceof ItemBaseCard;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack){
		return !(itemstack.getItem() instanceof ItemRemotePanel) && itemstack.getItem() instanceof ItemBaseCard;
	}

	@Override
	public int getField(int id){
		return 0;
	}

	@Override
	public void setField(int id, int value){

	}

	@Override
	public int getFieldCount(){
		return 0;
	}

	@Override
	public void clear(){

	}

	public void readFromNBT(NBTTagCompound compound){
		NBTTagList items = compound.getTagList("item_inventory", compound.getId());
		for(int i = 0; i < items.tagCount(); ++i){
			NBTTagCompound item = items.getCompoundTagAt(i);
			byte slot = item.getByte("slot");
			if(slot >= 0 && slot < getSizeInventory())
				inventory[slot] = new ItemStack(item);
		}
	}

	public void writeToNBT(NBTTagCompound tagcompound){
		NBTTagList items = new NBTTagList();

		for(int i = 0; i < getSizeInventory(); ++i)
			if(getStackInSlot(i) != ItemStack.EMPTY){
				NBTTagCompound item = new NBTTagCompound();
				item.setInteger("slot", i);
				getStackInSlot(i).writeToNBT(item);
				items.appendTag(item);
			}
		tagcompound.setTag("item_inventory", items);
	}

}
