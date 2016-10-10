package worldcontrolteam.worldcontrol.items;

import net.minecraft.item.Item;

public class WCItems {
	public static Item FLUID_CARD;
	public static Item FLUID_KIT;
	public static Item KIT;
	public static Item CARD;
	public static Item THERMOMETER;
	public static Item TIME_CARD;
	public static Item REMOTE_PANEL;

	public static void registerItems(){
		KIT = new ItemKit();
		CARD = new ItemCard();
		THERMOMETER = new ItemThermometer();
		TIME_CARD = new ItemCardTime();
		REMOTE_PANEL = new ItemRemotePanel();
		FLUID_CARD = new ItemFluidCard();
		FLUID_KIT = new ItemFluidKit();
	}
}
