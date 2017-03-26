package worldcontrolteam.worldcontrol.client;




import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AlarmAudioLoader {

    protected static File WCalarms;
    public static void checkAndCreateFolders(File file){
        String soundsFolder = file.getParent();
        if(soundsFolder != null){
            WCalarms = new File(soundsFolder, "WCalarms");
            File jsonLoc = new File(WCalarms, "assets"+ File.separator +"worldcontrol" + File.separator + "sounds.json");
            File audioLoc = new File(WCalarms, "assets"+ File.separator +"worldcontrol" + File.separator + "sounds");

            if(!WCalarms.exists()){
                try {
                    WCalarms.mkdir();
                    audioLoc.mkdirs();
                    buildJSON();

                } catch (IOException e) {e.printStackTrace();}

            }
        }
    }

    private static void buildJSON() throws IOException {
        JsonWriter parse = new JsonWriter(new FileWriter(WCalarms.getAbsolutePath() + File.separator + "assets"+ File.separator +"worldcontrol"+ File.separator + "sounds.json"));
        parse.beginObject();
        parse.name("_comment").value("EXAMPLE FORM: 'alarm-default': {'category': 'master','sounds': [{'name': 'worldcontrol:alarm-default','stream': true}]}");
        parse.endObject();
        parse.close();
    }

    public static class TextureSetting implements IResourceManagerReloadListener {

        public TextureSetting() {
        }


        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {
            if (resourceManager instanceof SimpleReloadableResourceManager) {
                SimpleReloadableResourceManager simplemanager = (SimpleReloadableResourceManager) resourceManager;
                FolderResourcePack pack = new FolderResourcePack(WCalarms);
                simplemanager.reloadResourcePack(pack);


            }
        }
    }
}
