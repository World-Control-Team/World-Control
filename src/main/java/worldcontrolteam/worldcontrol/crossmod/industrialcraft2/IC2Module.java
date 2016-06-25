package worldcontrolteam.worldcontrol.crossmod.industrialcraft2;

import worldcontrolteam.worldcontrol.api.core.WorldControlAPI;
import worldcontrolteam.worldcontrol.crossmod.ModuleBase;

public class IC2Module extends ModuleBase {

	@Override
	public void preInit() {
		WorldControlAPI.getInstance().addThermometerModule(new IC2ReactorHeat());
	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {

	}

	@Override
	public String modID() {
		return "IC2";
	}
}
