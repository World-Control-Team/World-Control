package worldcontrolteam.worldcontrol.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpgrade extends WCBaseItem {

    public static final int DAMAGE_RANGE = 0;
    public static final int DAMAGE_COLOR = 1;

    public ItemUpgrade() {
        super("upgrade_cards");
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(16);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int damage = itemStack.getItemDamage();
        switch (damage) {
            case DAMAGE_RANGE:
                return "item.worldcontrol.range_upgrade";
            case DAMAGE_COLOR:
                return "item.worldcontrol.color_upgrade";
            default:
                return "";
        }
    }

    @Override
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> itemList) {
        if (this.isInCreativeTab(creativeTab)) {
            itemList.add(new ItemStack(this, 1, DAMAGE_RANGE));
            itemList.add(new ItemStack(this, 1, DAMAGE_COLOR));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(this, DAMAGE_RANGE, new ModelResourceLocation("worldcontrol:range_upgrade", "inventory"));
        ModelLoader.setCustomModelResourceLocation(this, DAMAGE_COLOR, new ModelResourceLocation("worldcontrol:color_upgrade", "inventory"));
    }
}
