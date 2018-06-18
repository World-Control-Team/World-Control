package worldcontrolteam.worldcontrol.api.screen.predefs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.predefs.IProviderCard;
import worldcontrolteam.worldcontrol.api.card.predefs.StringWrapper;
import worldcontrolteam.worldcontrol.api.screen.IScreenElement;

import java.util.ArrayList;
import java.util.List;

/**
 * File created by mincrmatt12 on 6/17/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public class ScreenElementProviderCard implements IScreenElement {
    private final IProviderCard card;
    private List<StringWrapper> joinedData;

    public ScreenElementProviderCard(IProviderCard card) {
        this.card = card;
        this.joinedData = new ArrayList<>();
    }

    @Override
    public void onCardUpdate(World world, ItemStack newItemStack) {
        this.joinedData.clear();
        this.card.getStringData(this.joinedData, 0, newItemStack, true); //todo: ask someone what tha params are for. (deprecate me)
    }

    @Override
    public void draw(int sizeX, int sizeY) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        int row = 0;
        for (StringWrapper panelString : joinedData) {
            if (panelString.textLeft != null)
                fontRenderer.drawString(panelString.textLeft, 2, row * 10 + 20, 0x06aee4);

            if (panelString.textCenter != null)
                fontRenderer.drawString(panelString.textCenter, (sizeX - fontRenderer.getStringWidth(panelString.textCenter)) / 2, row * 10 + 20, 0x06aee4);

            if (panelString.textRight != null)
                fontRenderer.drawString(panelString.textRight, sizeX - fontRenderer.getStringWidth(panelString.textRight), (row - 1) * 10 + 20, 0x06aee4);

            if ((row++) * 10 + 20 > sizeY) return;
        }
    }
}
