package worldcontrolteam.worldcontrol.crossmod.industrialcraft2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import worldcontrolteam.worldcontrol.api.core.ModuleBase;
import worldcontrolteam.worldcontrol.api.core.WorldControlAPI;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2EnergyStorageCard;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2EnergyStorageKit;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2ReactorCard;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2ReactorKit;

public class IC2Module extends ModuleBase {

	protected static Item reactorKit;
	public static Item reactorCard;
	public static Item energyCard, energyKit;

	@Override
	public void registryEvents() {
		reactorKit = new IC2ReactorKit();
		reactorCard = new IC2ReactorCard();
		energyCard = new IC2EnergyStorageCard();
		energyKit = new IC2EnergyStorageKit();
	}

	@Override
	public void preInit(){
		WorldControlAPI.getInstance().addThermometerModule(new IC2ReactorHeat());


		//temp.
		GameRegistry.addRecipe(new ShapedOreRecipe(reactorKit, new Object[]{" c ", "cgc", " c ", 'g', "ingotGold", 'c', "circuitBasic"}));

	}

	@Override
	public void init(){

	}

	@Override
	public void postInit(){

	}

	@Override
	public String modID(){ //TODO: change in 1.11
		return "ic2";
	}

	@Override
	public Object handleServerGUI(int ID, EntityPlayer player, World world, int x, int y, int z){
		return null;
	}

	@Override
	public Object handleClientGUI(int ID, EntityPlayer player, World world, int x, int y, int z){
		return null;
	}
}
