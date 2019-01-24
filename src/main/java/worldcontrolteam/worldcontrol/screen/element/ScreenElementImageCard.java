package worldcontrolteam.worldcontrol.screen.element;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.api.screen.IScreenElement;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.screen.img.ScreenImageMarshaller;

public class ScreenElementImageCard implements IScreenElement {
    private String imgURL;
    private ScreenImageMarshaller.ImageInfo lastImageInfo;
    private World theWorld;

    @Override
    public void draw(int sizeX, int sizeY, float partialTicks) {
        if (theWorld == null) return;
        lastImageInfo = ScreenImageMarshaller.INSTANCE.tryGetImage(imgURL, theWorld.getWorldTime());

        double sizeY_ = Math.min((double)sizeY, getSizeY(sizeX));
        double sizeX_ = Math.min((double)sizeX, getSizeX((int)sizeY_));
        sizeY_ = Math.min(sizeY_, getSizeY((int)sizeX_));

        if (lastImageInfo == null) return;

        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(lastImageInfo.texID);
        b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        b.pos(0, 0, 0).tex(0, 0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        b.pos(0, sizeY_, 0).tex(0, lastImageInfo.v).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        b.pos(sizeX_, sizeY_, 0).tex(lastImageInfo.u, lastImageInfo.v).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        b.pos(sizeX_, 0, 0).tex(lastImageInfo.u, 0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        t.draw();
    }

    @Override
    public double getSizeY(int sizeX) {
        return lastImageInfo == null ? 0 : ((float)lastImageInfo.height / lastImageInfo.width) * sizeX;
    }

    private double getSizeX(int sizeY) {
        return lastImageInfo == null ? 0 : ((float)lastImageInfo.width / lastImageInfo.height) * sizeY;
    }

    @Override
    public void onCardUpdate(World world, ItemStack newItemStack) {
        if (newItemStack.getItem() != WCContent.IMAGE_CARD) return;

        // TODO grab the img, rn i just use a test url.

        theWorld = world;
        String grabbedURL = "https://i.imgur.com/bR8ndDC.png";

        if (!ScreenImageMarshaller.INSTANCE.isImageURLValid(grabbedURL))
            return;

        // set the url
        imgURL = grabbedURL;
    }
}
