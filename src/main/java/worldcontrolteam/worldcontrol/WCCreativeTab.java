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
    public void displayAllRelevantItems(NonNullList<ItemStack> items)
    {
        super.displayAllRelevantItems(items);
        sortList(items);
    }

    private void sortList(NonNullList<ItemStack> items){
        //TODO: Coded will probably read this and have a better way to achieve the same thing
        NonNullList<ItemStack> blocks = NonNullList.create();
        NonNullList<ItemStack> cards = NonNullList.create();
        NonNullList<ItemStack> others = NonNullList.create();
        for(ItemStack stack : items){
            if(stack.getItem() instanceof ItemBlock){
                blocks.add(stack);
            }else if(stack.getItem() instanceof ItemBaseKit){
                cards.add(stack);
                cards.add(new ItemStack(((ItemBaseKit)stack.getItem()).getCardType()));
            }else if(stack.getItem() instanceof ItemBaseCard){
                continue;
            }else{
                others.add(stack);
            }
        }
        items.clear();
        items.addAll(blocks);
        items.addAll(others);
        items.addAll(cards);
    }
}
