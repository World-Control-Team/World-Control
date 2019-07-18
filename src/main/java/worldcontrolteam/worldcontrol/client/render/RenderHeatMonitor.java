package worldcontrolteam.worldcontrol.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public class RenderHeatMonitor extends TileEntitySpecialRenderer<TileEntityBaseReactorHeatMonitor> {
    private static final String TEXTURE_FILE = "worldcontrol:textures/blocks/remoteheatmonitor/scale.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);


    @SuppressWarnings("Duplicates")
    @Override
    public void render(TileEntityBaseReactorHeatMonitor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();

        int i = this.getWorld().getCombinedLight(te.getPos().offset(getWorld().getBlockState(te.getPos()).getValue(BlockBasicRotate.FACING)), 0);
        int i1 = i % 65536;
        int j1 = i / 65536;
        OpenGlHelper.setLightmapTextureCoords (OpenGlHelper.lightmapTexUnit, (float) i1, (float) j1);
        GlStateManager.disableLighting();

        if(te.getRenderType().equals("half_block")) {
            renderNumberOnBlock(te.getThreshhold(), getWorld().getBlockState(te.getPos()).getValue(BlockBasicRotate.FACING), 0, 0, x, y, z);
        } else if(te.getRenderType().equals("full_block")){
            drawHeatMap(te, getWorld().getBlockState(te.getPos()).getValue(BlockBasicRotate.FACING), x, y, z);
            renderNumberOnBlock(te.getThreshhold(), getWorld().getBlockState(te.getPos()).getValue(BlockBasicRotate.FACING), 0.58f, -0.25F, x, y, z);
        }

        GlStateManager.popAttrib();
    }



    @SuppressWarnings("Duplicates")
    private void renderNumberOnBlock(int number, EnumFacing facing, float translationOffset, float upOffset, double x, double y, double z){
        int threshold = number;
        GlStateManager.pushMatrix();

        short side = (short) facing.getOpposite().getIndex();
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
        GlStateManager.translate(0.5F, 0.4375F + translationOffset, 0.6875F + upOffset);

        GlStateManager.rotate(-90, 1, 0, 0);
        GlStateManager.scale(var12, -var12, var12);
        GlStateManager.doPolygonOffset(-10, -10);
        GlStateManager.enablePolygonOffset();
        getFontRenderer().drawString(String.valueOf(threshold), -getFontRenderer().getStringWidth(String.valueOf(threshold)) / 2, -getFontRenderer().FONT_HEIGHT, 0x000000);
        GlStateManager.disablePolygonOffset();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }


    @SuppressWarnings("Duplicates")
    private void drawHeatMap(TileEntityBaseReactorHeatMonitor baseReactorHeatMonitor, EnumFacing facing, double x, double y, double z){
        // Setup transforms for block face -- copied straight out of the infopanel.

        GlStateManager.pushMatrix();

        // Bind textures
        bindTexture(TEXTURE_LOCATION);

        short side = (short) facing.getIndex();
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
        GlStateManager.rotate(-90, 1, 0, 0);
        GlStateManager.translate(0, -1 - 0.025, 0+ 0.009);

        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        // Show thing
        double amt = MathHelper.clamp(baseReactorHeatMonitor.getThreshhold() / (double)baseReactorHeatMonitor.getCurrentHeat(), 0.0D, 1.0D);

        b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        b.pos(1 - amt, 0.15625 + 0.05, -0.01).tex(amt, 0).endVertex();
        b.pos(1 - amt, 0.5, -0.01).tex(amt, 0.8).endVertex();
        b.pos(1, 0.5, -0.01).tex(0, 0.8).endVertex();
        b.pos(1, 0.15625+ 0.05, -0.01).tex(0, 0).endVertex();
        t.draw();

        GlStateManager.popMatrix();
    }

}
