package worldcontrolteam.worldcontrol;

import worldcontrolteam.worldcontrol.api.core.ModuleBase;
import worldcontrolteam.worldcontrol.api.core.WorldControlAPI;
import worldcontrolteam.worldcontrol.api.thermometer.IHeatSeeker;
import worldcontrolteam.worldcontrol.crossmod.Modules;

public class WCapiImpl implements WorldControlAPI.IWorldControlAPI {

	@Override
	public void addThermometerModule(IHeatSeeker m){
		WorldControl.heatTypez.add(m);
	}

	@Override
	public void removeModule(Class<? extends ModuleBase> module){
		Modules.removeModule(module);
	}
}
