package worldcontrolteam.worldcontrol.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import worldcontrolteam.worldcontrol.WorldControl;

import java.util.ArrayList;
import java.util.List;

public class WCBaseItem extends Item {

    public static List<Item> wcItems = new ArrayList<Item>();

    public WCBaseItem(String name) {
        this.setCreativeTab(WorldControl.TAB);
        this.setRegistryName("worldcontrol." + name);
        this.setUnlocalizedName("worldcontrol." + name);

        wcItems.add(this);
        //GameRegistry.register(this);

        if (WorldControl.side == Side.CLIENT)
            ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("worldcontrol:" + name, "inventory"));
    }
}
