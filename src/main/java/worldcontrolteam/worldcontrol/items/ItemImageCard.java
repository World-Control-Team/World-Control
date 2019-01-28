package worldcontrolteam.worldcontrol.items;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.ICard;
import worldcontrolteam.worldcontrol.api.screen.IScreenElement;
import worldcontrolteam.worldcontrol.screen.element.ScreenElementImageCard;

public class ItemImageCard extends WCBaseItem implements ICard {
    public ItemImageCard() {
        super("image_card");
        this.setMaxStackSize(1);
    }

    @Override
    public CardState update(World world, ItemStack card) {
        return CardState.OK;
    }

    @Override
    public IScreenElement getRenderer(ItemStack stack) {
        return new ScreenElementImageCard();
    }

    @Override
    public GuiScreen getConfigGui(World world, BlockPos pos, int i, ItemStack card) {
        return null;
    }
}
