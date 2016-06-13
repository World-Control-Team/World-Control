package worldcontrolteam.worldcontrol.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WCItems {
	public static Item kit;
	
	public static void registerItems(){
		kit = new WCBaseItem("kit");
	}
}
