package worldcontrolteam.worldcontrol;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.init.WCItems;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public class WCCreativeTab extends CreativeTabs {

    public WCCreativeTab() {
        super("World Control");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
        return new ItemStack(WCItems.THERMOMETER);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return WCUtility.translate("creativetab");
    }
}
