package worldcontrolteam.worldcontrol.screen.img;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.WorldControl;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class ScreenImageMarshaller {
    public static ScreenImageMarshaller INSTANCE = new ScreenImageMarshaller();

    // TODO: make me a config
    private static final ImmutableSet<String> validHosts = ImmutableSet.of("i.imgur.com", "i.redd.it");
    private static int maximumImages = 16;

    private int abs_max_size = 2048;

    public static class ImageInfo {
        public int width;
        public int height;

        public double u, v;

        public int texID;
        public String imgKey;
    }

    private HashMap<String, ImageInfo> allocated_things;
    private HashMap<String, Float> last_partial_tick_usage;

    // public interface

    public ImageInfo tryGetImage(String imgURL, float currentTicks) {
        if (WorldControl.PROXY.getImageGrabber() == null) return null;

        if (allocated_things.containsKey(imgURL) && allocated_things.get(imgURL).texID >= 0) {
             last_partial_tick_usage.put(imgURL, currentTicks);
             return allocated_things.get(imgURL);
        }

        if (!allocated_things.containsKey(imgURL)) {
            ImageInfo i = new ImageInfo();
            i.texID = -1;

            // Calculate an image key

            i.imgKey = calculateKey(imgURL);

            WorldControl.PROXY.getImageGrabber().beginDownload(imgURL, i.imgKey);
            allocated_things.put(imgURL, i);
        }


        ImageInfo i = allocated_things.get(imgURL);

        BufferedImage possibleResult = WorldControl.PROXY.getImageGrabber().retrieveDownloadedImage(i.imgKey);

        if (possibleResult == null) {
            return null;
        }
        else {
            // at this point, we need to initialize a gl texture, but first we need to make sure the image is the right size

            int w = possibleResult.getWidth();
            int h = possibleResult.getHeight();

            while (w > abs_max_size || h > abs_max_size) {
                w /= 2;
                h /= 2;
            }

            int minWTexSize = MathHelper.smallestEncompassingPowerOfTwo(w);
            int minHTexSize = MathHelper.smallestEncompassingPowerOfTwo(h);

            if (last_partial_tick_usage.size() > maximumImages) removeSlotIfRequired();

            i.texID = GlStateManager.generateTexture();
            GlStateManager.pushAttrib();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(i.texID);

            Image finalImage = possibleResult.getScaledInstance(w, h, BufferedImage.SCALE_FAST);
            BufferedImage im_format = new BufferedImage(minWTexSize, minHTexSize, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = im_format.getGraphics();
            g.drawImage(finalImage, 0, 0, null);
            g.dispose();

            int[] data = new int[minWTexSize*minHTexSize];
            im_format.getRGB(0, 0, minWTexSize, minHTexSize, data, 0, minWTexSize);

            ByteBuffer b = BufferUtils.createByteBuffer(minWTexSize*minHTexSize*4);
            for (int y = 0; y < minHTexSize; ++y) {
                for (int x = 0; x < minWTexSize; ++x) {
                    int pixel = data[y * minWTexSize + x];
                    b.put((byte) ((pixel >> 16) & 0xFF));
                    b.put((byte) ((pixel >> 8) & 0xFF));
                    b.put((byte) (pixel & 0xFF));
                    b.put((byte) ((pixel >> 24) & 0xFF));
                }
            }

            b.flip();

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, minWTexSize, minHTexSize, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, b);
            if (GL11.glGetError() == GL11.GL_OUT_OF_MEMORY) {
                removeSlotIfRequired();
                GlStateManager.bindTexture(0);
                GlStateManager.deleteTexture(i.texID);
                GlStateManager.popAttrib();

                allocated_things.remove(imgURL);
                return null;
            }

            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

            GlStateManager.popAttrib();

            i.width = w;
            i.height = h;

            i.u = (1.0d / minWTexSize) * w;
            i.v = (1.0d / minHTexSize) * h;

            allocated_things.put(imgURL, i);
            last_partial_tick_usage.put(imgURL, currentTicks);

            return i;
        }
    }

    private String calculateKey(String url) {
        try {
            URL u = new URL(url);

            return u.getHost() + "-" + u.getFile();
        }
        catch (MalformedURLException e) {return "";}
    }

    private void removeSlotIfRequired() {
        String chosen = null;
        for (String url : allocated_things.keySet()) {
            if (allocated_things.get(url).texID == -1) continue;
            if (last_partial_tick_usage.get(url) > 20.0 * 40) {
                chosen = url;
                break;
            }
        }
        if (chosen != null) {
            // free up texture

            GL11.glDeleteTextures(allocated_things.get(chosen).texID);

            allocated_things.remove(chosen);
            last_partial_tick_usage.remove(chosen);
        }
    }

    public boolean isImageURLValid(String imgURL) {
        URL url;
        try {
            url = new URL(imgURL);
        }
        catch (MalformedURLException e) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (validHosts.contains(url.getHost().toLowerCase().trim())) return true;

        // todo: add other conditions here

        return false;
    }

    public ScreenImageMarshaller() {
        allocated_things = new HashMap<>();
        last_partial_tick_usage = new HashMap<>();
    }

    public void initGL() {
        abs_max_size = GlStateManager.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
    }
}
