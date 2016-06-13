package worldcontrolteam.worldcontrol.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.WCUtility;

public class ItemCard extends WCBaseItem {

	public ItemCard() {
		super("card");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		try{
			NBTTagCompound nbt = stack.getTagCompound();
			int x = nbt.getInteger("x");
			int y = nbt.getInteger("y");
			int z = nbt.getInteger("z");
			tooltip.add(WCUtility.translateFormatted("card.cords", x, y, z));
		}catch(NullPointerException e){
			tooltip.add(WCUtility.translate("card.nocords"));
		}
	}
}
