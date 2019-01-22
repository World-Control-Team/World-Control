package worldcontrolteam.worldcontrol.api.screen.predefs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.card.compat.IProviderCard;
import worldcontrolteam.worldcontrol.api.card.compat.StringWrapper;
import worldcontrolteam.worldcontrol.api.screen.IScreenElement;

import java.util.ArrayList;
import java.util.List;

/**
 * File created by mincrmatt12 on 6/17/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
@SuppressWarnings("Duplicates")
public class ScreenElementProviderCard implements IScreenElement {
    private final IProviderCard card;
    private List<StringWrapper> joinedData;
    boolean showingLabels = false;

    public ScreenElementProviderCard(IProviderCard card) {
        this.card = card;
        this.joinedData = new ArrayList<>();
    }

    @Override
    public void onCardUpdate(World world, ItemStack newItemStack) {
        this.joinedData.clear();
        this.card.getStringData(this.joinedData, 0, newItemStack, showingLabels); //todo: ask someone what tha params are for. (deprecate me)
    }

    @Override
    public void setContext(EnumScreenContext ctx, boolean showLabels) {
        this.showingLabels = showLabels;
    }

    @Override
    public void draw(int sizeX, int sizeY) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        int maxlen = 1;

        for (StringWrapper sw : joinedData) {
            int len = ((sw.textLeft != null ? fontRenderer.getStringWidth(sw.textLeft) : 8) +
                    (sw.textRight != null ? fontRenderer.getStringWidth(sw.textRight)+4 : 8) +
                    (sw.textCenter != null ? fontRenderer.getStringWidth(sw.textCenter)+4 : 8));
            maxlen = Math.max(maxlen, len);
        }

        double scaleFactor = (double)sizeX / (double)maxlen;
        if (joinedData.size()*10*scaleFactor > sizeY) {
            scaleFactor = (float)sizeY / (joinedData.size() * 10);
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(scaleFactor, scaleFactor, 0);
        sizeX /= scaleFactor;

        int row = 0;

        for (StringWrapper panelString : joinedData) {
            if (panelString.textLeft != null)
                fontRenderer.drawString(panelString.textLeft, 2, row * 10, 0x06aee4);

            if (panelString.textCenter != null)
                fontRenderer.drawString(panelString.textCenter, (sizeX - fontRenderer.getStringWidth(panelString.textCenter)) / 2, row * 10, 0x06aee4);

            if (panelString.textRight != null)
                fontRenderer.drawString(panelString.textRight, sizeX - fontRenderer.getStringWidth(panelString.textRight), (row) * 10, 0x06aee4);

            if ((row++) * 10 > sizeY) return;
        }

        GlStateManager.popMatrix();
    }

    @Override
    public double getSizeY(int sizeX) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        int maxlen = 1;

        for (StringWrapper sw : joinedData) {
            int len = ((sw.textLeft != null ? fontRenderer.getStringWidth(sw.textLeft) : 8) +
                    (sw.textRight != null ? fontRenderer.getStringWidth(sw.textRight)+4 : 8) +
                    (sw.textCenter != null ? fontRenderer.getStringWidth(sw.textCenter)+4 : 8));
            maxlen = Math.max(maxlen, len);
        }

        double scaleFactor = (double)sizeX / (double)maxlen;

        int row = joinedData.size();
        return (row * 10 + 20) * scaleFactor;
    }
}
