package worldcontrolteam.worldcontrol.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.api.thermometer.IHeatSeeker;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.List;

public class ItemThermometer extends WCBaseItem {

	private static List<IHeatSeeker> heatTypes;

	public ItemThermometer() {
		super("thermometer");
		this.setMaxStackSize(1);
		this.setMaxDamage(102);
	}

	public static void addInHeatTypes(List<IHeatSeeker> types){
		heatTypes = types;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!heatTypes.isEmpty()){
			ItemStack stack = player.getHeldItem(hand);
			if(!stack.hasTagCompound()){
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setInteger("type", 0);
				stack.setTagCompound(tagCompound);
			}
			if(stack.getTagCompound() != null){
				int toUse = stack.getTagCompound().getInteger("type");
				IHeatSeeker user = heatTypes.get(toUse);
				if(user.canUse(world, pos, world.getTileEntity(pos))){
					if(!world.isRemote)
						player.sendMessage(new TextComponentString(WCUtility.translateFormatted("thermometer.chat_info", user.getHeat(world, pos, world.getTileEntity(pos)))));
					stack.damageItem(10, player);
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		ItemStack itemStack = player.getHeldItem(hand);
		if(player.isSneaking()) {
			if (!heatTypes.isEmpty()) {
				if (itemStack.hasTagCompound()) {
					NBTTagCompound tag = itemStack.getTagCompound();
					int currentType = tag.getInteger("type");
					if (currentType + 1 < heatTypes.size()) {
						currentType++;
						tag.setInteger("type", currentType);
						itemStack.setTagCompound(tag);
					} else tag.setInteger("type", 0);
				} else {
					NBTTagCompound tagCompound = new NBTTagCompound();
					tagCompound.setInteger("type", 0);
					itemStack.setTagCompound(tagCompound);
				}
				if(world.isRemote) {
					player.sendMessage(new TextComponentString(
						WCUtility.translateFormatted("thermometer.switchTo", WCUtility.translate("thermometer.mode." + heatTypes.get(itemStack.getTagCompound().getInteger("type")).getUnloalizedName()))
					));
				}
				return new ActionResult(EnumActionResult.SUCCESS, itemStack);
			}
		}
		return new ActionResult(EnumActionResult.PASS, itemStack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced){
		try{
			tooltip.add(WCUtility.translateFormatted("thermometer.mode", WCUtility.translate("thermometer.mode." + heatTypes.get(stack.getTagCompound().getInteger("type")).getUnloalizedName())));
		}catch (NullPointerException e){
			tooltip.add(WCUtility.translateFormatted("thermometer.mode", WCUtility.translate("thermometer.unset")));
		}
	}
}
