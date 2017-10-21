package worldcontrolteam.worldcontrol.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.card.CardManager;
import worldcontrolteam.worldcontrol.init.WCRegistries;
import worldcontrolteam.worldcontrol.utils.CardUtils;
import worldcontrolteam.worldcontrol.utils.NBTUtils;

import java.util.List;

public class ItemKit extends WCBaseItem {
    public ItemKit() {
        super("kit");
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        CardManager manager = CardUtils.getCardManager(stack).orElse(null);
        if (manager==null||!manager.isValidBlock(worldIn, pos))
            return EnumActionResult.PASS;
        NBTTagCompound click = getClickData(pos, facing);

        ItemStack card = ItemCard.createCard(manager, click);
        player.setHeldItem(hand, card);

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    public NBTTagCompound getClickData(BlockPos pos, EnumFacing facing) {
        NBTTagCompound click = new NBTTagCompound();
        NBTUtils.writeBlockPos(click, pos);
        NBTUtils.writeEnumFacing(click, facing);
        click.setBoolean("click", true);
        return click;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            List<CardManager> values = WCRegistries.REGISTRY.getValues();
            for (int i = 0; i < values.size(); i++) {
                CardManager manager = values.get(i);
                if (manager.hasKit())
                    items.add(new ItemStack(this, 1, i));
            }
        }
    }
}