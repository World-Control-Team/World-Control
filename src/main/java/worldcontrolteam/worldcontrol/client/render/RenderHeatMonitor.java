package worldcontrolteam.worldcontrol.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;

public class RenderHeatMonitor extends TileEntitySpecialRenderer<TileEntityBaseReactorHeatMonitor> {
    private static final String TEXTURE_FILE = "worldcontrol:textures/blocks/remoteheatmonitor/scale.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);


    @Override
    public void render(TileEntityBaseReactorHeatMonitor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(te.getRenderType().equals("half_block")) {
            renderNumberOnBlock(te.getThreshhold(), getWorld().getBlockState(te.getPos()).getValue(BlockBasicRotate.FACING), 0, x, y, z);
        } else if(te.getRenderType().equals("full_block")){
            renderNumberOnBlock(te.getThreshhold(), getWorld().getBlockState(te.getPos()).getValue(BlockBasicRotate.FACING), 0.58f, x, y, z);
            drawHeatMap(te, getWorld().getBlockState(te.getPos()).getValue(BlockBasicRotate.FACING), x, y, z);
        }
    }



    private void renderNumberOnBlock(int number, EnumFacing facing, float translationOffset, double x, double y, double z){
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
        GlStateManager.translate(0.5F, 0.4375F + translationOffset, 0.6875F);

        GlStateManager.rotate(-90, 1, 0, 0);
        GlStateManager.scale(var12, -var12, var12);
        GlStateManager.doPolygonOffset(-10, -10);
        GlStateManager.enablePolygonOffset();
        getFontRenderer().drawString(String.valueOf(threshold), -getFontRenderer().getStringWidth(String.valueOf(threshold)) / 2, -getFontRenderer().FONT_HEIGHT, 0);
        GlStateManager.disablePolygonOffset();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }


    private void drawHeatMap(TileEntityBaseReactorHeatMonitor baseReactorHeatMonitor, EnumFacing facing, double x, double y, double z){
        GL11.glPushMatrix();
        GL11.glPolygonOffset(-10, -10);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        //int heat = thermo.getHeatLevel();

        GL11.glTranslatef((float) x, (float) y, (float) z);
        bindTexture(TEXTURE_LOCATION);
        switch (facing.getOpposite().getIndex()) {
            case 0:
                break;
            case 1:
                GL11.glTranslatef(1, 1, 0);
                GL11.glRotatef(180, 1, 0, 0);
                GL11.glRotatef(180, 0, 1, 0);
                break;
            case 2:
                GL11.glTranslatef(0, 1f, 0);
                GL11.glRotatef(0, 0, 1, 0);
                GL11.glRotatef(90, 1, 0, 0);
                break;
            case 3:
                GL11.glTranslatef(1, 1, 1);
                GL11.glRotatef(180, 0, 1, 0);
                GL11.glRotatef(90, 1, 0, 0);
                break;
            case 4:
                GL11.glTranslatef(0, 1, 1f);
                GL11.glRotatef(90, 0, 1, 0);
                GL11.glRotatef(90, 1, 0, 0);
                break;
            case 5:
                GL11.glTranslatef(1, 1, 0);
                GL11.glRotatef(-90, 0, 1, 0);
                GL11.glRotatef(90, 1, 0, 0);
                break;

        }
        GL11.glTranslatef(0.5F, 1F, 0.5F);
        GL11.glRotatef(-90, 1, 0, 0);
        /*switch (thermo.rotation) {
            case 0:
                break;
            case 1:
                GL11.glRotatef(-90, 0, 0, 1);
                break;
            case 2:
                GL11.glRotatef(90, 0, 0, 1);
                break;
            case 3:
                GL11.glRotatef(180, 0, 0, 1);
                break;
        }*/

        int currentHeat = baseReactorHeatMonitor.getCurrentHeat();
        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        tessellator.getBuffer().normal(0, 0, 1);
        if (currentHeat > -2) {
            //tessellator.setBrightness(block.getMixedBrightnessForBlock(thermo.getWorldObj(), thermo.xCoord, thermo.yCoord, thermo.zCoord));
            tessellator.getBuffer().color(1f, 1f, 1f, 1f);
            double left = -0.4375;
            double top = -0.3125;
            double width = 0.875;
            double height = 0.25;
            double deltaU = 1;
            double deltaV = 1;
            double u = 1D / 16;
            double v;
            double middle;
            if (currentHeat == -1) {
                v = 0;
                middle = width;
            } else {
                double heatLevel = ((double) baseReactorHeatMonitor.getThreshhold()) / baseReactorHeatMonitor.getCurrentHeat();
                if (heatLevel > 1)
                    heatLevel = 1;
                middle = heatLevel * width;
                v = 4D / 16;
            }
            this.draw(tessellator, left, top, 0, u, v);
            this.draw(tessellator, left + middle, top, 0, u + middle * deltaU, v);
            this.draw(tessellator, left + middle, top + height, 0, u + middle * deltaU, v + height * deltaV);
            this.draw(tessellator, left, top + height, 0, u, v + height * deltaV);

            if (middle != width) {
                v = 0.5;
                this.draw(tessellator, left + middle, top, 0, u, v);
                this.draw(tessellator, left + width, top, 0, u + width * deltaU, v);
                this.draw(tessellator, left + width, top + height, 0, u + width * deltaU, v + height * deltaV);
                this.draw(tessellator, left + middle, top + height, 0, u, v + height * deltaV);
            }
        }
        tessellator.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        /*FontRenderer fontRenderer = this.getFontRenderer();
        GL11.glDepthMask(false);
        GL11.glScalef(0.016666668F, -0.016666668F, 0.016666668F);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, -fontRenderer.FONT_HEIGHT, 0);
        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);*/

        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPopMatrix();
    }

    private static void draw(Tessellator tess, double x, double y, double z, double U, double V){
        tess.getBuffer().pos(x, y ,z);
        tess.getBuffer().tex(U, V);
        tess.getBuffer().color(1.0f, 1.0f, 1.0f, 1.0f);
        tess.getBuffer().endVertex();
    }
}
