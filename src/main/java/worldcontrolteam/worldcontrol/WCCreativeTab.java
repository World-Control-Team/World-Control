package worldcontrolteam.worldcontrol;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import worldcontrolteam.worldcontrol.init.WCItems;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public class WCCreativeTab extends CreativeTabs {

	public WCCreativeTab() {
		super("World Control");
	}

	@Override
	public Item getTabIconItem(){
		return WCItems.KIT;
	}

	@Override
	public String getTranslatedTabLabel(){
		return WCUtility.translate("creativetab");
	}
}
