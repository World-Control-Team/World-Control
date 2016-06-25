package worldcontrolteam.worldcontrol.items;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.IProviderCard;
import worldcontrolteam.worldcontrol.api.card.StringWrapper;

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
}
