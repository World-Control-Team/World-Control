package worldcontrolteam.worldcontrol.crossmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import worldcontrolteam.worldcontrol.api.core.ModuleBase;
import worldcontrolteam.worldcontrol.crossmod.extremereactors.ExtremeReactorsModule;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.IC2Module;
import worldcontrolteam.worldcontrol.crossmod.tesla.TeslaModule;

import java.util.ArrayList;

public class Modules {

	private static ArrayList<Class<? extends ModuleBase>> modules = new ArrayList<Class<? extends ModuleBase>>();

	public Modules() {
		modules.add(IC2Module.class);
		modules.add(TeslaModule.class);
		modules.add(ExtremeReactorsModule.class);
	}

	public static void removeModule(Class<? extends ModuleBase> moduleBase){
		modules.remove(moduleBase);
	}

	public void registryEvents(){
		for(Class<? extends ModuleBase> mod : modules)
			try{
				ModuleBase modBase = mod.newInstance();
				if(Loader.isModLoaded(modBase.modID()))
					modBase.registryEvents();
			}catch (InstantiationException e){
				e.printStackTrace();
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}
	}

	public void preInit(){
		for(Class<? extends ModuleBase> mod : modules)
			try{
				ModuleBase modBase = mod.newInstance();
				if(Loader.isModLoaded(modBase.modID()))
					modBase.preInit();
			}catch (InstantiationException e){
				e.printStackTrace();
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}
	}

	public void init(){
		for(Class<? extends ModuleBase> mod : modules)
			try{
				ModuleBase modBase = mod.newInstance();
				if(Loader.isModLoaded(modBase.modID()))
					modBase.init();
			}catch (InstantiationException e){
				e.printStackTrace();
			}catch (IllegalAccessException e){

			}
	}

	public void postInit(){
		for(Class<? extends ModuleBase> mod : modules)
			try{
				ModuleBase modBase = mod.newInstance();
				if(Loader.isModLoaded(modBase.modID()))
					modBase.postInit();
			}catch (InstantiationException e){
				e.printStackTrace();
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}
	}

	public Object guiHandlerServer(int ID, EntityPlayer player, World world, int x, int y, int z){
		for(Class<? extends ModuleBase> mod : modules)
			try{
				ModuleBase modBase = mod.newInstance();
				if(modBase.handleServerGUI(ID, player, world, x, y, z) != null)
					return modBase.handleServerGUI(ID, player, world, x, y, z);
			}catch (InstantiationException e){
				e.printStackTrace();
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}
		return null;
	}

	public Object guiHandlerClient(int ID, EntityPlayer player, World world, int x, int y, int z){
		for(Class<? extends ModuleBase> mod : modules)
			try{
				ModuleBase modBase = mod.newInstance();
				if(modBase.handleClientGUI(ID, player, world, x, y, z) != null)
					return modBase.handleClientGUI(ID, player, world, x, y, z);
			}catch (InstantiationException e){
				e.printStackTrace();
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}
		return null;
	}
}
