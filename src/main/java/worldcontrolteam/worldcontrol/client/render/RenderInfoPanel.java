package worldcontrolteam.worldcontrol.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.api.screen.IScreenElement;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;

/**
 * File created by mincrmatt12 on 6/18/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public class RenderInfoPanel extends TileEntitySpecialRenderer<TileEntityInfoPanel> {
    @Override
    public void render(TileEntityInfoPanel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.ise == null) return;
        if (!te.getPowered()) return;
        IScreenElement ise = te.ise;

        GlStateManager.pushMatrix();
        x = x - (te.getPos().getX() - te.origin.getX()) - 0.0001;
        y = y - (te.getPos().getY() - te.origin.getY()) - 0.0001;
        z = z - (te.getPos().getZ() - te.origin.getZ()) - 0.0001;

        int sizeX = te.end.getX() - te.origin.getX() + 1;
        int sizeY = te.end.getY() - te.origin.getY() + 1;
        int sizeZ = te.end.getZ() - te.origin.getZ() + 1;

        switch (te.facing) {
            case NORTH:
                GlStateManager.translate(x, y, z);
                GlStateManager.scale(sizeX / ((float)sizeX * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeX * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                //GlStateManager.scale(-1, 1, 1);
                ise.draw(sizeX * 64, sizeY * 64);
                break;
            case SOUTH:
                GlStateManager.translate(x+sizeX, y, z+1.001);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.scale(sizeX / ((float)sizeX * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeX * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                //GlStateManager.scale(-1, 1, 1);
                ise.draw(sizeX * 64, sizeY * 64);
                break;
            case EAST:
                GlStateManager.translate(x+1.001, y, z);
                GlStateManager.rotate(-90, 0, 1, 0);
                GlStateManager.scale(sizeZ / ((float)sizeZ * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeZ * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                ise.draw(sizeZ * 64, sizeY * 64);
                break;
            case WEST:
                GlStateManager.translate(x, y, z+sizeZ);
                GlStateManager.rotate(90, 0, 1, 0);
                GlStateManager.scale(sizeZ / ((float)sizeZ * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeZ * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                ise.draw(sizeZ * 64, sizeY * 64);
            default:
                break;
        }
        GlStateManager.popMatrix();
    }
}
