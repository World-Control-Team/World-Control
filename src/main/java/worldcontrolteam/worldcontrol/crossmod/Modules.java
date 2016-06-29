package worldcontrolteam.worldcontrol.crossmod;

import net.minecraftforge.fml.common.Loader;
import worldcontrolteam.worldcontrol.api.core.ModuleBase;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.IC2Module;

import java.util.ArrayList;

public class Modules {

	private static  ArrayList<Class<? extends ModuleBase>> modules = new ArrayList<Class<? extends ModuleBase>>();

	public Modules() {
		modules.add(IC2Module.class);
	}

	public static void removeModule(Class<? extends ModuleBase> moduleBase){
		modules.remove(moduleBase);
	}

	public void preInit() {
		for(Class<? extends ModuleBase> mod : modules){
			try{
				ModuleBase moz = mod.newInstance();
				if(Loader.isModLoaded(moz.modID())){
					moz.preInit();
				}
			}catch (InstantiationException e){
				e.printStackTrace();
			}catch(IllegalAccessException e){
				e.printStackTrace();
			}
		}
	}

	public void init() {
		for(Class<? extends ModuleBase> mod : modules){
			try{
				ModuleBase moz = mod.newInstance();
				if(Loader.isModLoaded(moz.modID())){
					moz.init();
				}
			}catch (InstantiationException e){
				e.printStackTrace();
			}catch(IllegalAccessException e){
				
			}
		}
	}

	public void postInit() {
		for (Class<? extends ModuleBase> mod : modules) {
			try {
				ModuleBase moz = mod.newInstance();
				if (Loader.isModLoaded(moz.modID())) {
					moz.postInit();
				}
			}catch (InstantiationException e){
				e.printStackTrace();
			}catch(IllegalAccessException e){
				e.printStackTrace();
			}
		}
	}
}
