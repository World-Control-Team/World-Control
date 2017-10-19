package worldcontrolteam.worldcontrol.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFilterItemHandler extends SlotItemHandler {

    private final int slotIndex;

    public SlotFilterItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.slotIndex = index;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        if (inventory instanceof ISlotItemFilter)
            return ((ISlotItemFilter) inventory).isItemValid(slotIndex, itemStack);
        return super.isItemValid(itemStack);
    }
}
