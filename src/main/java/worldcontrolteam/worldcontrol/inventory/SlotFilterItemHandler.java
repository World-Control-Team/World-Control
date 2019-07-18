package worldcontrolteam.worldcontrol.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotFilterItemHandler extends SlotItemHandler {

    private final int slotIndex;
    private final TileEntity entity;

    public SlotFilterItemHandler(IItemHandler itemHandler, TileEntity tile, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.slotIndex = index;
        this.entity = tile;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        if (this.entity instanceof ISlotItemFilter)
            return ((ISlotItemFilter) entity).isItemValid(slotIndex, itemStack);
        return super.isItemValid(itemStack);
    }
}
