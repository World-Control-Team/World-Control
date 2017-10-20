package worldcontrolteam.worldcontrol.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;

public class HeatMonitorTESR extends TileEntitySpecialRenderer<TileEntityBaseReactorHeatMonitor> {

    @Override
    public void render(TileEntityBaseReactorHeatMonitor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        int threshold = te.getThreshhold();
        GlStateManager.pushMatrix();

        short side = (short) getWorld().getBlockState(te.getPos()).getValue(BlockBasicRotate.FACING).getOpposite().getIndex();
        float var12 = 0.014F;
        GlStateManager.translate((float) x, (float) y, (float) z);
        switch (side) {
            case 0:
                break;
            case 1:
                GlStateManager.translate(1, 1, 0);
                GlStateManager.rotate(180, 1, 0, 0);
                GlStateManager.rotate(180, 0, 1, 0);
                break;
            case 2:
                GlStateManager.translate(0, 1, 0);
                GlStateManager.rotate(0, 0, 1, 0);
                GlStateManager.rotate(90, 1, 0, 0);
                break;
            case 3:
                GlStateManager.translate(1, 1, 1);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.rotate(90, 1, 0, 0);
                break;
            case 4:
                GlStateManager.translate(0, 1, 1);
                GlStateManager.rotate(90, 0, 1, 0);
                GlStateManager.rotate(90, 1, 0, 0);
                break;
            case 5:
                GlStateManager.translate(1, 1, 0);
                GlStateManager.rotate(-90, 0, 1, 0);
                GlStateManager.rotate(90, 1, 0, 0);
                break;

        }
        GlStateManager.translate(0.5F, 0.4375F, 0.6875F);

        GlStateManager.rotate(-90, 1, 0, 0);
        GlStateManager.scale(var12, -var12, var12);
        GlStateManager.doPolygonOffset(-10, -10);
        GlStateManager.enablePolygonOffset();
        getFontRenderer().drawString(String.valueOf(threshold), -getFontRenderer().getStringWidth(String.valueOf(threshold)) / 2, -getFontRenderer().FONT_HEIGHT, 0);
        GlStateManager.disablePolygonOffset();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();

    }
}
