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
        IScreenElement ise = te.ise;

        GlStateManager.pushMatrix();
        x = x - (te.getPos().getX() - te.origin.getX()) - 0.0001;
        y = y - (te.getPos().getY() - te.origin.getY()) - 0.0001;
        z = z - (te.getPos().getZ() - te.origin.getZ()) - 0.0001;

        double sizeX = te.end.getX() - te.origin.getX() + .75;
        double sizeY = te.end.getY() - te.origin.getY() + .75;
        double sizeZ = te.end.getZ() - te.origin.getZ() + .75;

        switch (te.facing) {
            case NORTH:
                GlStateManager.translate(x+.2, y+.1, z);
                GlStateManager.scale(sizeX / ((float)sizeX * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeX * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                //GlStateManager.translate(0.125, -0.125, 0);
                //GlStateManager.scale(-1, 1, 1);
                ise.draw((int)(sizeX * 64), (int)(sizeY * 64));
                break;
            case SOUTH:
                //GlStateManager.translate(0.125, -0.125, 0);
                GlStateManager.translate(x+sizeX+.05, y+.18, z+1.001);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.scale(sizeX / ((float)sizeX * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeX * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                //GlStateManager.translate(0.125, 0.125, 0);
                //GlStateManager.scale(-1, 1, 1);
                ise.draw((int)(sizeX * 64), (int)(sizeY * 64));
                break;
            case EAST:
                GlStateManager.translate(x+1.001, y+.18, z+.2);
                GlStateManager.rotate(-90, 0, 1, 0);
                GlStateManager.scale(sizeZ / ((float)sizeZ * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeZ * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                GlStateManager.translate(0.125, 0.125, 0);

                ise.draw((int)(sizeZ * 64), (int)(sizeY * 64));
                break;
            case WEST:
                GlStateManager.translate(x, y+.18, z+sizeZ+.05);
                GlStateManager.rotate(90, 0, 1, 0);
                GlStateManager.scale(sizeZ / ((float)sizeZ * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeZ * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                GlStateManager.translate(0.125, 0.125, 0);

                ise.draw((int)(sizeZ * 64), (int)(sizeY * 64));
            default:
                break;
        }
        GlStateManager.popMatrix();
    }
}
