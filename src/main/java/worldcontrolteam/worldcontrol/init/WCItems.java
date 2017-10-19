package worldcontrolteam.worldcontrol.init;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import worldcontrolteam.worldcontrol.items.*;

public class WCItems {
	public static Item FLUID_CARD;
	public static Item FLUID_KIT;
	public static Item THERMOMETER;
	public static Item TIME_CARD;
	public static Item REMOTE_PANEL;
	public static Item FORGE_ENERGY_CARD;
	public static Item FORGE_ENERGY_KIT;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event){
		THERMOMETER = new ItemThermometer();
		TIME_CARD = new ItemCardTime();
		REMOTE_PANEL = new ItemRemotePanel();
		FLUID_CARD = new ItemFluidCard();
		FLUID_KIT = new ItemFluidKit();
		FORGE_ENERGY_CARD = new ItemForgeEnergyCard();
		FORGE_ENERGY_KIT = new ItemForgeEnergyKit();

		WCBaseItem.wcItems.forEach(event.getRegistry()::register);

		//temp.
		//GameRegistry.addRecipe(new ShapedOreRecipe(THERMOMETER, new Object[]{"ig ", "gbg", " gg", 'i', "ingotIron", 'g', "blockGlass", 'b', Items.WATER_BUCKET}));

		//GameRegistry.addRecipe(new ShapedOreRecipe(TIME_CARD, new Object[]{" c ", "cgc", " c ", 'g', "ingotGold", 'c', Items.CLOCK}));

		//GameRegistry.addRecipe(new ShapedOreRecipe(REMOTE_PANEL, new Object[]{"ppp", "gdg", "ppp", 'p', Items.PAPER, 'd', "gemDiamond", 'g', "ingotGold"}));
	}
}
