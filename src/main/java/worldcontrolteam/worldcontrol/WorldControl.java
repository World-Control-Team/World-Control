package worldcontrolteam.worldcontrol;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import worldcontrolteam.worldcontrol.items.WCItems;

@Mod(modid = WorldControl.MODID, version = "@VERSION@")
public class WorldControl{
	
	public static final String MODID = "worldcontrol";
	
	public static WCCreativeTab tab = new WCCreativeTab();
	
	public static Side side; //As in client vs server
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		WCUtility.info("We are in pre-init!");
		side = event.getSide();
		
		WCItems.registerItems();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		WCUtility.info("We are in init!");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		WCUtility.info("We are in post-init!");
	}
}
