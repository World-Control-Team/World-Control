package worldcontrolteam.worldcontrol.api.card;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.api.screen.IScreenElement;

/**
 * File created by mincrmatt12 on 6/17/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public interface ICard {
    CardState update(World world, ItemStack card);

    IScreenElement getRenderer(ItemStack stack);

    /**
     * Grab a configuration gui.
     * @param world the world
     * @param tePos position of the block containing this card (te implements ICardSettingsHolder)
     * @param cardId slot id of the card in the block
     * @param card the card
     * @return a guiscreen
     */
    @SideOnly(Side.CLIENT)
    GuiScreen getConfigGui(World world, BlockPos tePos, int cardId, ItemStack card);
}
