package worldcontrolteam.worldcontrol.api.card.predefs;

import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.ICard;
import worldcontrolteam.worldcontrol.api.screen.IScreenElement;
import worldcontrolteam.worldcontrol.api.screen.predefs.ScreenElementProviderCard;

import java.util.List;

public interface IProviderCard extends ICard {

    List<StringWrapper> getStringData(List<StringWrapper> list, int displaySettings, ItemStack card, boolean showLabels);

    List<String> getGuiData();

    @SideOnly(Side.CLIENT)
    int getCardColor();

    @Override
    default IScreenElement getRenderer(ItemStack s) {
        return new ScreenElementProviderCard(this);
    }

    @Override
    default Gui getConfigGui(World w, ItemStack is) {return null;} // TODO: dmf add toggle gui here.
}
