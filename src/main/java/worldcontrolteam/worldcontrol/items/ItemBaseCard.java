package worldcontrolteam.worldcontrol.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.IProviderCard;
import worldcontrolteam.worldcontrol.api.card.StringWrapper;
import worldcontrolteam.worldcontrol.utils.NBTUtils;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.List;

public abstract class ItemBaseCard extends WCBaseItem implements IProviderCard {

	public ItemBaseCard(String name) {
		super(name);
	}

	@Override
	public abstract CardState update(World world, ItemStack card);

	@Override
	public abstract List<StringWrapper> getStringData(List<StringWrapper> list, int displaySettings, ItemStack card, boolean showLabels);

	@Override
	public abstract List<String> getGuiData();

	@Override
	@SideOnly(Side.CLIENT)
	public abstract int getCardColor();

	public boolean hasKit() {
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced){
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
}
