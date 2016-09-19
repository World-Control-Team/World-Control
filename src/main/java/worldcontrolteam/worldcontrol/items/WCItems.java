package worldcontrolteam.worldcontrol.items;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class WCItems {
	public static Item kit;
	public static Item card;
	public static Item thermometer;
	public static Item cardTime;
	public static Item remotePanel;

	public static void registerItems(){
		kit = new ItemKit();
		card = new ItemCard();
		thermometer = new ItemThermometer();
		cardTime = new ItemCardTime();
		remotePanel = new ItemRemotePanel();
		
		//temp.
		GameRegistry.addRecipe(new ShapedOreRecipe(thermometer, new Object[]{
				"ig ", "gbg", " gg",
					'i', "ingotIron",
					'g', "blockGlass",
					'b', Items.WATER_BUCKET}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(cardTime, new Object[]{
				" c ", "cgc", " c ",
					'g', "ingotGold",
					'c', Items.CLOCK}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(remotePanel, new Object[]{
				"ppp", "gdg", "ppp",
					'p', Items.PAPER,
					'd', "gemDiamond",
					'g', "ingotGold"}));
	}
}
