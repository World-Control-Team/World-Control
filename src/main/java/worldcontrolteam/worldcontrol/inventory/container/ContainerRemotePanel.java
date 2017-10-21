package worldcontrolteam.worldcontrol.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import worldcontrolteam.worldcontrol.api.card.IProviderCard;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.inventory.SlotFilter;

public class ContainerRemotePanel extends Container {

	protected ItemStack is;
	protected InventoryItem item;

	public ContainerRemotePanel(InventoryPlayer inv, ItemStack stack, InventoryItem iItem) {
		this.is = stack;
		this.item = iItem;

		this.addSlotToContainer(new SlotFilter(this.item, 0, 177, 21));
		bindPlayerInventory(inv);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer){
		/*
		 * for (int i = 0; i < 3; i++) { for (int j = 0; j < 9; j++) {
		 * addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j *
		 * 18, 84 + i * 18)); } }
		 */

		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_){
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot){
		ItemStack stack = null;
		Slot slots = this.inventorySlots.get(slot);

		if(slots.getStack() != ItemStack.EMPTY)
			if(slots.getStack().getItem() == WCContent.REMOTE_PANEL)
				return null;

		if(slots != null && slots.getHasStack()){
			ItemStack itemstackR = slots.getStack();
			stack = itemstackR.copy();

			if(slot == 0){
				boolean fixed = false;
				for(int h = 1; h < 10; h++){
					Slot know = this.inventorySlots.get(h);
					if(!know.getHasStack()){
						know.putStack(slots.getStack());
						slots.decrStackSize(1);
						fixed = true;
					}
				}
				if(!fixed)
					return null;
				slots.onSlotChange(itemstackR, stack);
			}else if(slots.getStack().getItem() instanceof IProviderCard && !this.inventorySlots.get(0).getHasStack()){
				this.inventorySlots.get(0).putStack(itemstackR);
				slots.decrStackSize(1);
				slots.onSlotChange(itemstackR, stack);
				this.inventorySlots.get(0).onSlotChanged();
			}else return null;
		}
		return stack;
	}

	@Override
	public ItemStack slotClick(int slot, int dragType, ClickType click, EntityPlayer player){
		if(slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItemMainhand())
			return null;
		return super.slotClick(slot, dragType, click, player);
	}
}
