package worldcontrolteam.worldcontrol.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.utils.NBTUtils;

public abstract class ItemBaseKit extends WCBaseItem {

	public ItemBaseKit(String name) {
		super(name);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		ItemStack card = new ItemStack(getCardType());
		NBTTagCompound nbt = new NBTTagCompound();

		NBTUtils.writeBlockPos(nbt, pos);

		card.setTagCompound(nbt);

		player.inventory.mainInventory[player.inventory.currentItem] = card;
		return EnumActionResult.SUCCESS;
	}

	public abstract Item getCardType();
}
