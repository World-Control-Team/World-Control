package worldcontrolteam.worldcontrol.crossmod;

import net.minecraftforge.fml.common.Loader;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.IC2Module;

import java.util.ArrayList;

public class Modules {

	private static final ArrayList<ModuleBase> modules = new ArrayList<ModuleBase>();

	public Modules() {
		modules.add(new IC2Module());
	}

	public void preInit() {
		for(ModuleBase mod : modules){
			if(Loader.isModLoaded(mod.modID())){
				mod.preInit();
			}
		}
	}

	public void init() {
		for(ModuleBase mod : modules){
			if(Loader.isModLoaded(mod.modID())){
				mod.init();
			}
		}
	}

	public void postInit() {
		for(ModuleBase mod : modules){
			if(Loader.isModLoaded(mod.modID())){
				mod.postInit();
			}
		}
	}

}
