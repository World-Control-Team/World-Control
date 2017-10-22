package worldcontrolteam.worldcontrol;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.items.ItemBaseCard;
import worldcontrolteam.worldcontrol.items.ItemBaseKit;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public class WCCreativeTab extends CreativeTabs {

    public WCCreativeTab() {
        super("World Control");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
        return new ItemStack(WCContent.REMOTE_PANEL);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return WCUtility.translate("creativetab");
    }

    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> items) {
        super.displayAllRelevantItems(items);
        sortList(items);
    }

    private void sortList(NonNullList<ItemStack> items) {
        items.removeIf(s -> s.getItem() instanceof ItemBaseCard && ((ItemBaseCard) s.getItem()).hasKit());
        items.sort((stack1, stack2) -> {
            boolean s1b = stack1.getItem() instanceof ItemBlock;
            boolean s2b = stack2.getItem() instanceof ItemBlock;
            boolean s1k = stack1.getItem() instanceof ItemBaseKit;
            boolean s2k = stack2.getItem() instanceof ItemBaseKit;
            boolean s1c = stack1.getItem() instanceof ItemBaseCard;
            boolean s2c = stack2.getItem() instanceof ItemBaseCard;
            return s1b && s2b ? 0 : s1b ? -1 : s2b ? 1 : s1k && s2k ? 0 : s1k ? -1 : s2k ? 1 : s1c && s2c ? 0 : s1c ? -1 : s2c ? 1 : 1;
        });
        items.forEach(s -> {
            if (s.getItem() instanceof ItemBaseKit) {
                int i = items.indexOf(s);
                items.add(i + 1, new ItemStack(((ItemBaseKit) s.getItem()).getCardType()));
            }
        });
    }
}