package worldcontrolteam.worldcontrol.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.init.WCItems;
import worldcontrolteam.worldcontrol.utils.NBTUtils;

public class ItemKit extends WCBaseItem {

	public ItemKit() {
		super("kit");
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		ItemStack card = new ItemStack(WCItems.CARD);
		NBTTagCompound nbt = new NBTTagCompound();

		NBTUtils.writeBlockPos(nbt, pos);

		card.setTagCompound(nbt);

		player.inventory.mainInventory[player.inventory.currentItem] = card;
		return EnumActionResult.SUCCESS;
	}
}
