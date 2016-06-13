package xbony2.thechiefssurprise;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import xbony2.thechiefssurprise.items.CSItems;

public class CSCreativeTab extends CreativeTabs {

	public CSCreativeTab() {
		super("The Chief's Surprise");
	}

	@Override
	public Item getTabIconItem() {
		return CSItems.maize;
	}
	
	@Override
	public String getTranslatedTabLabel() {
		return CSUtility.translate("creativetab");
	}
}
