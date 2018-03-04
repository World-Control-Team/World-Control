package worldcontrolteam.worldcontrol.client.model.base;

import com.google.common.collect.Maps;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Map;

public class CustomModelLoader implements ICustomModelLoader {
    public static Map<ResourceLocation, IModel> BLOCK_MODELS = Maps.newHashMap();
    public static Map<ResourceLocation, IModel> ITEM_MODELS = Maps.newHashMap();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return BLOCK_MODELS.containsKey(modelLocation) || ITEM_MODELS.containsKey(modelLocation);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) {
        if (BLOCK_MODELS.containsKey(modelLocation)) {
            return BLOCK_MODELS.get(modelLocation);
        } else {
            return BLOCK_MODELS.containsKey(modelLocation) ? BLOCK_MODELS.get(modelLocation) : null;
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        BLOCK_MODELS.clear();
        ITEM_MODELS.clear();
        MinecraftForge.EVENT_BUS.post(new CustomModelReloadEvent());
    }

    public static class CustomModelReloadEvent extends Event {
    }
}