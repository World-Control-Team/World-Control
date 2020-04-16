package worldcontrolteam.worldcontrol.client;


import com.google.gson.stream.JsonWriter;
import java.util.function.Predicate;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import worldcontrolteam.worldcontrol.utils.WCConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AlarmAudioLoader {

    protected static File WCalarms;

    public static void checkAndCreateFolders(File file) {
        String soundsFolder = file.getParent();
        if (soundsFolder != null && WCConfig.useCustomSounds) {
            WCalarms = new File(soundsFolder, "WCalarms");
            File audioLoc = new File(WCalarms, "assets" + File.separator + "worldcontrol" + File.separator + "sounds");

            if (!WCalarms.exists()) {
                try {
                    WCalarms.mkdir();
                    audioLoc.mkdirs();
                    buildJSON();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private static void buildJSON() throws IOException {
        JsonWriter parse = new JsonWriter(new FileWriter(WCalarms.getAbsolutePath() + File.separator + "assets" + File.separator + "worldcontrol" + File.separator + "sounds.json"));
        parse.beginObject();
        parse.name("_comment-ERASE ME").value("EXAMPLE FORM: 'file-name': {'category': 'master','sounds': [{'name': 'worldcontrol:file-name','stream': true}]}");
        parse.endObject();
        parse.close();
    }

    public static class TextureSetting implements ISelectiveResourceReloadListener {

        public TextureSetting() {
        }


        @Override
        public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
            if(resourcePredicate.test(VanillaResourceType.SOUNDS)){
                if (resourceManager instanceof SimpleReloadableResourceManager) {
                    SimpleReloadableResourceManager simplemanager = (SimpleReloadableResourceManager) resourceManager;
                    FolderResourcePack pack = new FolderResourcePack(WCalarms);
                    simplemanager.reloadResourcePack(pack);
                }
            }
        }
    }
}
