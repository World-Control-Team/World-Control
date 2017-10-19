package worldcontrolteam.worldcontrol.items;

import net.minecraft.item.Item;
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
	}
}
