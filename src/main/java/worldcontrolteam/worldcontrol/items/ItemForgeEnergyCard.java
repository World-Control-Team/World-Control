package worldcontrolteam.worldcontrol.items;


import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.StringWrapper;

import java.util.List;

public class ItemForgeEnergyCard extends ItemBaseCard{

    public ItemForgeEnergyCard() {
        super("ForgeEnergyCard");
    }

    @Override
    public CardState update(World world, ItemStack card) {
        return null;
    }

    @Override
    public List<StringWrapper> getStringData(List<StringWrapper> list, int displaySettings, ItemStack card, boolean showLabels) {
        return null;
    }

    @Override
    public List<String> getGuiData() {
        return null;
    }

    @Override
    public int getCardColor() {
        return 0;
    }
}
