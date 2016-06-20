package worldcontrolteam.worldcontrol;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import worldcontrolteam.worldcontrol.items.WCItems;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public class WCCreativeTab extends CreativeTabs {

	public WCCreativeTab() {
		super("World Control");
	}

	@Override
	public Item getTabIconItem() {
		return WCItems.kit;
	}
	
	@Override
	public String getTranslatedTabLabel() {
		return WCUtility.translate("creativetab");
	}
}
