package worldcontrolteam.worldcontrol.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import worldcontrolteam.worldcontrol.inventory.SlotFilterItemHandler;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseAverageCounter;

public class ContainerAverageCounter extends Container {

	public TileEntityBaseAverageCounter averageCounter;
	private EntityPlayer player;
	private double lastAverage = -1;

	public ContainerAverageCounter(EntityPlayer player, TileEntityBaseAverageCounter averageCounter) {
		super();

		this.averageCounter = averageCounter;
		this.player = player;

		//transformer upgrades
		addSlotToContainer(new SlotFilterItemHandler(averageCounter, 0, 8, 18));

		//inventory
		for(int i = 0; i < 3; i++)
			for(int k = 0; k < 9; k++)
				addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));

		for(int j = 0; j < 9; j++)
			addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 142));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn){
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int slotId){
		Slot slot = this.inventorySlots.get(slotId);
		if(slot != null){
			ItemStack items = slot.getStack();
			if(items != null){
				int initialCount = items.stackSize;
				if(slotId < averageCounter.getSlots())//moving from counter to inventory
				{
					mergeItemStack(items, averageCounter.getSlots(), inventorySlots.size(), false);
					if(items.stackSize == 0)
						slot.putStack((ItemStack) null);
					else{
						slot.onSlotChanged();
						if(initialCount != items.stackSize)
							return items;
					}
				}else for(int i = 0; i < averageCounter.getSlots(); i++){
					if(!averageCounter.isItemValid(i, items))
						continue;
					ItemStack targetStack = averageCounter.getStackInSlot(i);
					if(targetStack == null){
						Slot targetSlot = this.inventorySlots.get(i);
						targetSlot.putStack(items);
						slot.putStack((ItemStack) null);
						break;
					}else if(items.isStackable() && items.isItemEqual(targetStack)){
						mergeItemStack(items, i, i + 1, false);
						if(items.stackSize == 0)
							slot.putStack((ItemStack) null);
						else{
							slot.onSlotChanged();
							if(initialCount != items.stackSize)
								return items;
						}
						break;
					}

				}
			}
		}
		return null;
	}

}
