package worldcontrolteam.worldcontrol.items;

import java.util.List;

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
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!heatTypes.isEmpty()){
			if(!stack.hasTagCompound()){
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setInteger("TYPE", 0);
				stack.setTagCompound(tagCompound);
			}
			if(stack.getTagCompound() != null){
				int toUse = stack.getTagCompound().getInteger("TYPE");
				IHeatSeeker user = heatTypes.get(toUse);
				if(user.canUse(world, pos, world.getTileEntity(pos))){
					if(!world.isRemote)
						player.addChatComponentMessage(new TextComponentString(WCUtility.translateFormatted("THERMOMETER.chatInfo", user.getHeat(world, pos, world.getTileEntity(pos)))));
					stack.damageItem(10, player);
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand){
		if(!heatTypes.isEmpty()){
			if(itemStack.hasTagCompound()){
				NBTTagCompound tag = itemStack.getTagCompound();
				int currentType = tag.getInteger("TYPE");
				if(currentType + 1 < heatTypes.size())
					tag.setInteger("TYPE", currentType++);
				else tag.setInteger("TYPE", 0);
			}else{
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setInteger("TYPE", 0);
				itemStack.setTagCompound(tagCompound);
			}
			return new ActionResult(EnumActionResult.SUCCESS, itemStack);
		}
		return new ActionResult(EnumActionResult.PASS, itemStack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		try{
			tooltip.add(WCUtility.translateFormatted("THERMOMETER.mode", WCUtility.translate("THERMOMETER.mode." + heatTypes.get(stack.getTagCompound().getInteger("TYPE")).getUnloalizedName())));
		}catch (NullPointerException e){
			tooltip.add(WCUtility.translateFormatted("THERMOMETER.mode", WCUtility.translate("THERMOMETER.unset")));
		}
	}
}
