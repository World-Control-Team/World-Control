package worldcontrolteam.worldcontrol.client.model.infopanel;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

/**
 * File created by mincrmatt12 on 6/15/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public class ModelLoaderInfoPanel implements ICustomModelLoader {


    @Override
    public boolean accepts(ResourceLocation resourceLocation) {
        return resourceLocation.getResourcePath().endsWith("!worldcontrol:info_panel");
    }

    @Override
    public IModel loadModel(ResourceLocation resourceLocation) throws Exception {
        return new ModelInfoPanel();
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {

    }
}
