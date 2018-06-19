package worldcontrolteam.worldcontrol.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import worldcontrolteam.worldcontrol.inventory.SlotFilterItemHandler;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;

public class ContainerInfoPanel extends Container
{
  public TileEntityInfoPanel panel;


  public ContainerInfoPanel()
  {
    super();
  }


  public ContainerInfoPanel(EntityPlayer player, TileEntityInfoPanel panel)
  {
    super();

    this.panel = panel;

    //card
    addSlotToContainer(new SlotFilterItemHandler(panel, 0, 8, 24+18));

    //range upgrade
    addSlotToContainer(new SlotFilterItemHandler(panel, 1, 8, 24+18*2));

    //color upgrade
    addSlotToContainer(new SlotFilterItemHandler(panel, 2, 8, 24+18*3));

    //inventory
    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 9; k++) {
        addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 24 + 84 + i * 18));
      }
    }

    for (int j = 0; j < 9; j++) {
      addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 24 + 142));
    }
  }

  @Override
  public boolean canInteractWith(EntityPlayer var1)
  {
    return true;
  }

  public void onContainerClosed(EntityPlayer playerIn)
  {
    super.onContainerClosed(playerIn);
    this.panel.closeInventory(playerIn);
  }


  @Override
  public ItemStack transferStackInSlot(EntityPlayer p, int slotId)
  {
    Slot slot = (Slot)this.inventorySlots.get(slotId);
    if (slot != null) {
      ItemStack items = slot.getStack();
      if (items != ItemStack.EMPTY) {
        int initialCount = items.getCount();
        if (slotId < panel.getSlots()) {
          //moving from panel to inventory
          mergeItemStack(items, panel.getSlots(), inventorySlots.size(), false);
          if (items.getCount() == 0) {
            slot.putStack(ItemStack.EMPTY);
          } else {
            slot.onSlotChanged();
            if(initialCount != items.getCount())
              return items;
          }
        } else {
          //moving from inventory to panel
          for (int i = 0;i < panel.getSlots();i++) {
            if (!panel.isItemValid(i, items)) {
              continue;
            }
            ItemStack targetStack = panel.getStackInSlot(i);
            if (targetStack == ItemStack.EMPTY) {
              Slot targetSlot = (Slot)this.inventorySlots.get(i);
              targetSlot.putStack(items);
              slot.putStack(ItemStack.EMPTY);
              break;
            } else if(items.isStackable() && items.isItemEqual(targetStack)) {
              mergeItemStack(items, i, i+1, false);
              if (items.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
              } else {
                slot.onSlotChanged();
                if (initialCount != items.getCount())
                  return items;
              }
              break;
            }
          }
        }
      }
    }
    return ItemStack.EMPTY;
  }

}
