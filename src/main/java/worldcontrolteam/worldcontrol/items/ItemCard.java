package worldcontrolteam.worldcontrol.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.utils.NBTUtils;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public class ItemCard extends WCBaseItem {

	public ItemCard() {
		super("card");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		try{
			NBTTagCompound nbt = stack.getTagCompound();
			BlockPos pos = NBTUtils.getBlockPos(nbt);
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			tooltip.add(WCUtility.translateFormatted("card.cords", x, y, z));
		}catch (NullPointerException e){
			tooltip.add(WCUtility.translate("card.no_cords"));
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		WCUtility.error(world.getTileEntity(pos));
		return EnumActionResult.PASS;
	}
}
