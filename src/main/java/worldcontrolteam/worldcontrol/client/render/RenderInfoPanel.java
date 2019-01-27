package worldcontrolteam.worldcontrol.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
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
        GlStateManager.pushAttrib();
        x = x - (te.getPos().getX() - te.origin.getX()) - 0.0001;
        y = y - (te.getPos().getY() - te.origin.getY()) - 0.0001;
        z = z - (te.getPos().getZ() - te.origin.getZ()) - 0.0001;

        double sizeX = te.end.getX() - te.origin.getX() + .75;
        double sizeY = te.end.getY() - te.origin.getY() + .75;
        double sizeZ = te.end.getZ() - te.origin.getZ() + .75;

        int i = this.getWorld().getCombinedLight(te.getPos().offset(te.facing), 0);
        int i1 = i % 65536;
        int j1 = i / 65536;
        OpenGlHelper.setLightmapTextureCoords (OpenGlHelper.lightmapTexUnit, (float) i1, (float) j1);
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        switch (te.facing) {
            case NORTH:
                GlStateManager.translate(x+.2, y+.1, z-0.001);
                GlStateManager.scale(sizeX / ((float)sizeX * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeX * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                ise.draw((int)(sizeX * 64), (int)(sizeY * 64), partialTicks);
                break;
            case SOUTH:
                GlStateManager.translate(x+sizeX+.05, y+.18, z+1.001);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.scale(sizeX / ((float)sizeX * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeX * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                ise.draw((int)(sizeX * 64), (int)(sizeY * 64), partialTicks);
                break;
            case EAST:
                GlStateManager.translate(x+1.001, y+.18, z+.2);
                GlStateManager.rotate(-90, 0, 1, 0);
                GlStateManager.scale(sizeZ / ((float)sizeZ * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeZ * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                GlStateManager.translate(0.125, 0.125, 0);
                ise.draw((int)(sizeZ * 64), (int)(sizeY * 64), partialTicks);
                break;
            case WEST:
                GlStateManager.translate(x-.001, y+.18, z+sizeZ+.05);
                GlStateManager.rotate(90, 0, 1, 0);
                GlStateManager.scale(sizeZ / ((float)sizeZ * 64), sizeY / ((float)sizeY * 64), 1);
                GlStateManager.translate(sizeZ * 64, sizeY * 64, 0);
                GlStateManager.scale(-1, -1, 1);
                GlStateManager.translate(0.125, 0.125, 0);
                ise.draw((int)(sizeZ * 64), (int)(sizeY * 64), partialTicks);
                break;
            case UP:
                GlStateManager.translate(x+.18, y+1.001, z+.18);
                GlStateManager.rotate(90, 1, 0, 0);
                GlStateManager.scale(sizeX / ((float)sizeX * 64), sizeZ / ((float)sizeZ * 64), 1);
                GlStateManager.translate(sizeX*64, sizeZ*64, 0);
                GlStateManager.scale(-1, -1, 1);
                ise.draw((int)(sizeX * 64), (int)(sizeZ * 64), partialTicks);
                break;
            case DOWN:
                GlStateManager.translate(x+.18, y-.001, z+.18);
                GlStateManager.rotate(-90, 1, 0, 0);
                GlStateManager.scale(sizeX / ((float)sizeX * 64), sizeZ / ((float)sizeZ * 64), 1);
                GlStateManager.translate(sizeX*64, 0, 0);
                GlStateManager.scale(-1, -1, 1);
                ise.draw((int)(sizeX * 64), (int)(sizeZ * 64), partialTicks);
                break;
            default:
                break;
        }
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}
