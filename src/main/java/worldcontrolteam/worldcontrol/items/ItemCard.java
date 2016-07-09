package worldcontrolteam.worldcontrol.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
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
			tooltip.add(WCUtility.translate("card.nocords"));
		}
	}
}
