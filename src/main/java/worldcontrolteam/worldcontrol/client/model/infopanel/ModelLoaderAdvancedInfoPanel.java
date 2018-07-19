package worldcontrolteam.worldcontrol.client.model.infopanel;

import com.google.gson.Gson;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import java.io.InputStreamReader;

/**
 * File created by mincrmatt12 on 6/26/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public class ModelLoaderAdvancedInfoPanel implements ICustomModelLoader {
    public static class ModelSpec {
        public String side;
        public String back;
    }

    IResourceManager rm;

    @Override
    public boolean accepts(ResourceLocation resourceLocation) {
        return resourceLocation.getResourcePath().endsWith("!worldcontrol:info_panel_adv");
    }

    @Override
    public IModel loadModel(ResourceLocation resourceLocation) throws Exception {
        String path = resourceLocation.getResourcePath();
        path = path.replace("!worldcontrol:info_panel_adv", "");
        IResource r = rm.getResource(new ResourceLocation(resourceLocation.getResourceDomain(), path + ".json"));
        Gson gson = new Gson();
        ModelLoaderAdvancedInfoPanel.ModelSpec s = gson.fromJson(new InputStreamReader(r.getInputStream()), ModelLoaderAdvancedInfoPanel.ModelSpec.class);

        return new ModelAdvancedInfoPanel(new ResourceLocation(s.side), new ResourceLocation(s.back));
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
        rm = iResourceManager;
    }
}
