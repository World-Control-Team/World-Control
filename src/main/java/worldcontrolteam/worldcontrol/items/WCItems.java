package worldcontrolteam.worldcontrol.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WCItems {
	public static Item kit;
	public static Item card;
	public static Item thermometer;
	
	public static void registerItems(){
		kit = new ItemKit();
		card = new ItemCard();
		thermometer = new ItemThermometer();
	}
}
