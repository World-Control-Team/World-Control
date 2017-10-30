package worldcontrolteam.worldcontrol.crossmod.industrialcraft2;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import worldcontrolteam.worldcontrol.api.core.ModuleBase;
import worldcontrolteam.worldcontrol.api.core.WorldControlAPI;
import worldcontrolteam.worldcontrol.client.gui.GuiIC2RemoteReactorHeatMonitor;
import worldcontrolteam.worldcontrol.client.gui.GuiReactorHeatMonitor;
import worldcontrolteam.worldcontrol.container.ContainerEmpty;
import worldcontrolteam.worldcontrol.container.ContainerIC2RemoteReactorMonitor;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks.BlockIC2ReactorMonitor;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks.BlockIC2RemoteReactorMonitor;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks.TileEntityIC2ReactorMonitor;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks.TileEntityIC2RemoteReactorMonitor;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2EnergyStorageCard;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2EnergyStorageKit;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2ReactorCard;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2ReactorKit;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;
import worldcontrolteam.worldcontrol.utils.GuiLib;

public class IC2Module extends ModuleBase {

	protected static Item reactorKit;
	public static Item reactorCard;
	public static Item energyCard, energyKit;
	public static Block THERMO_MONITOR, REMOTE_THERMO_MONITOR;

	@Override
	public void registryEvents() {
		reactorKit = new IC2ReactorKit();
		reactorCard = new IC2ReactorCard();
		energyCard = new IC2EnergyStorageCard();
		energyKit = new IC2EnergyStorageKit();
		THERMO_MONITOR = new BlockIC2ReactorMonitor();
		REMOTE_THERMO_MONITOR = new BlockIC2RemoteReactorMonitor();
		WCContent.addBlocks(THERMO_MONITOR, REMOTE_THERMO_MONITOR);
		WCContent.addItems(reactorCard,reactorKit,energyCard,energyKit);
	}

	@Override
	public void preInit(){
		WorldControlAPI.getInstance().addThermometerModule(new IC2ReactorHeat());

		GameRegistry.registerTileEntity(TileEntityIC2ReactorMonitor.class, "IC2reactorMonitor");
		GameRegistry.registerTileEntity(TileEntityIC2RemoteReactorMonitor.class, "IC2remoteReactorMonitor");
		//temp.
		//GameRegistry.addRecipe(new ShapedOreRecipe(reactorKit, new Object[]{" c ", "cgc", " c ", 'g', "ingotGold", 'c', "circuitBasic"}));

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
		TileEntity tile = world.getTileEntity(new BlockPos(x,y,z));
		if(ID == GuiLib.IC2_HEAT_MONITOR){
			return new ContainerEmpty(tile);
		}else if(ID == GuiLib.IC2_REMOTE_HEAT_MONITOR){
			return new ContainerIC2RemoteReactorMonitor(player, (TileEntityIC2RemoteReactorMonitor) tile);
		}
		return null;
	}

	@Override
	public Object handleClientGUI(int ID, EntityPlayer player, World world, int x, int y, int z){
		TileEntity tile = world.getTileEntity(new BlockPos(x,y,z));
		if(ID == GuiLib.IC2_HEAT_MONITOR){
			return new GuiReactorHeatMonitor((TileEntityBaseReactorHeatMonitor) tile);
		}else if(ID == GuiLib.IC2_REMOTE_HEAT_MONITOR){
			return new GuiIC2RemoteReactorHeatMonitor(player, (TileEntityIC2RemoteReactorMonitor) tile);
		}
		return null;
	}
}
