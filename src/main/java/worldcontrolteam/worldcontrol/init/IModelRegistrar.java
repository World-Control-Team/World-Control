package worldcontrolteam.worldcontrol.init;

import net.minecraft.block.Block;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.client.ClientUtil;

public interface IModelRegistrar {
    @SideOnly(Side.CLIENT)
    default void registerModels(ModelRegistryEvent event) {
        if(this instanceof Block) {
            ClientUtil.registerWithMapper((Block)this);
        }
    }
}
