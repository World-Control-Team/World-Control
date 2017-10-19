package worldcontrolteam.worldcontrol.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFilter extends Slot {

    private final int slotIndex;

    public SlotFilter(IInventory inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.slotIndex = index;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        if (inventory instanceof ISlotItemFilter)
            return ((ISlotItemFilter) inventory).isItemValid(slotIndex, itemStack);
        return super.isItemValid(itemStack);
    }
}
