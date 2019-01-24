package worldcontrolteam.worldcontrol.network.img;

import java.awt.image.BufferedImage;

public interface IImageGrabber {
    void beginDownload(String trustedUrl, String key);

    BufferedImage retrieveDownloadedImage(String key);
}
