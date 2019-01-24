package worldcontrolteam.worldcontrol.network.img;

import net.minecraft.util.Tuple;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@SuppressWarnings("SynchronizeOnNonFinalField")
public class ImageGrabber implements Runnable, IImageGrabber {
    // Grabs images from a remote as PNG

    private HashMap<String, BufferedImage> loaded;
    private BlockingQueue<Tuple<String, String>> toDownload;
    private Thread me;

    private BufferedImage downloadImage(String trustedUrl) throws IOException {
        URL url = new URL(trustedUrl);

        return ImageIO.read(url.openStream());
    }

    public ImageGrabber() {
        loaded = new HashMap<>();
        toDownload = new ArrayBlockingQueue<>(20);
        me = new Thread(this, "WC image downloader");
        me.setDaemon(true);
        me.start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Tuple<String, String> to_get;
            try {
                to_get = toDownload.take();
            }
            catch (InterruptedException e) {
                break;
            }
            try {
                WCUtility.info("Beginning image download: " + to_get.getFirst());
                BufferedImage i = downloadImage(to_get.getFirst());
                WCUtility.info("Download finished.");
                synchronized (loaded) {
                    loaded.put(to_get.getSecond(), i);
                }
            }
            catch (IOException e) {
                WCUtility.info("Failed to download from url: " + to_get.getFirst() + " because: " + e.getMessage());
            }
        }
    }

    @Override
    public void beginDownload(String trustedUrl, String key) {
        toDownload.add(new Tuple<String, String>(trustedUrl, key));
    }

    @Override
    public BufferedImage retrieveDownloadedImage(String key) {
        synchronized (loaded) {
            if (loaded.containsKey(key)) {
                BufferedImage i = loaded.get(key);
                loaded.remove(key);
                return i;
            }
        }
        return null;
    }


}
