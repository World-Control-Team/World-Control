package worldcontrolteam.worldcontrol.crossmod.industrialcraft2;

import net.minecraft.item.Item;
import worldcontrolteam.worldcontrol.api.core.WorldControlAPI;
import worldcontrolteam.worldcontrol.api.core.ModuleBase;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2ReactorCard;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2ReactorKit;

public class IC2Module extends ModuleBase {

	protected static Item reactorKit;
	public static Item reactorCard;

	@Override
	public void preInit(){
		WorldControlAPI.getInstance().addThermometerModule(new IC2ReactorHeat());
		reactorKit = new IC2ReactorKit();
		reactorCard = new IC2ReactorCard();
	}

	@Override
	public void init(){

	}

	@Override
	public void postInit(){

	}

	@Override
	public String modID(){
		return "IC2";
	}
}
