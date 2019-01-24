package worldcontrolteam.worldcontrol.api.screen;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * File created by mincrmatt12 on 6/15/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information
 */
public interface IScreenElement {
    enum EnumScreenContext {
        REMOTE_PANEL,
        SCREEN_BASIC,
        SCREEN_ADVANCED
    }
    /*
       Screen elements come from cards.

       They represent the graphical view of a card on a screen.
       A screen element is constructed whenever the screen starts displaying data.
       Whenever card
     */

    /**
     * draw() - draw the element
     *
     * Takes no parameters, assume a gl state with a matrix configured where 0, 0 is top left corner and 1 unit = 1/64 of a block.
     *
     * (using fonts, remember that 1 line = ~8 units && 1 char = ~4, so 1/8 of block = 1 line, and 16 chars per block at default scale)
     *
     * When drawing to a remotePanel, the units are not the same.
     *
     * If user has moved the element around a screen, the matrix will be automatically updated. (screenSize also gets set correctly)
     * @param sizeX size of screen X axis
     * @param sizeY size of screen Y axis
     * @param partialTicks current partialticks
     */
    @SideOnly(Side.CLIENT)
    void draw(int sizeX, int sizeY, float partialTicks);

    /**
     * Calculate the y-size for a given x-size.
     * This is done so that we can automatically lay out things.
     *
     * @param sizeX size of the entire screen
     * @return the desired y height to draw to
     */
    @SideOnly(Side.CLIENT)
    double getSizeY(int sizeX);

    /*
        Called on card update. You can use this to do various things, such as update screen contents based on the card. You can also ignore
        the card entirely should you so desire.
     */
    default void onCardUpdate(World world, ItemStack newItemStack) {}

    /**
     * Set the context this renderer is in.
     *
     * @param ctx where we are rendering from
     * @param showLabels are labels being shown?
     */
    default void setContext(EnumScreenContext ctx, boolean showLabels) {}
}
