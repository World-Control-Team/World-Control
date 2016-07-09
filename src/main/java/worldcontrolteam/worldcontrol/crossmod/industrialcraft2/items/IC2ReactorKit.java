package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items;

import net.minecraft.item.Item;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.IC2Module;
import worldcontrolteam.worldcontrol.items.ItemBaseKit;

public class IC2ReactorKit extends ItemBaseKit {
	public IC2ReactorKit() {
		super("IC2ReactorKit");
	}

	@Override
	public Item getCardType(){
		return IC2Module.reactorCard;
	}
}
