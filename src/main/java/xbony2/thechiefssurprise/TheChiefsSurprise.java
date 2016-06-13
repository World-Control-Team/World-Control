package xbony2.thechiefssurprise;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import xbony2.thechiefssurprise.items.CSItems;

@Mod(modid = TheChiefsSurprise.MODID, version = "@VERSION@")
public class TheChiefsSurprise{
	
	public static final String MODID = "thechiefssurprise";
	
	public static CSCreativeTab tab = new CSCreativeTab();
	
	public static Side side;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		CSUtility.info("We are in pre-init!");
		side = event.getSide();
		
		CSItems.registerItems();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		CSUtility.info("We are in init!");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		CSUtility.info("We are in post-init!");
	}
}
