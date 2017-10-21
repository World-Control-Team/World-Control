package worldcontrolteam.worldcontrol.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.init.IModelRegistrar;

public class WCBaseItem extends Item implements IModelRegistrar {

    public WCBaseItem(String name) {
        this.setCreativeTab(WorldControl.TAB);
        this.setRegistryName(name);
        this.setUnlocalizedName("worldcontrol." + name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}