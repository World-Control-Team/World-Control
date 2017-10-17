package worldcontrolteam.worldcontrol.crossmod.tesla;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import worldcontrolteam.worldcontrol.api.core.ModuleBase;
import worldcontrolteam.worldcontrol.client.gui.GuiAverageCounter;
import worldcontrolteam.worldcontrol.inventory.container.ContainerAverageCounter;

public class TeslaModule extends ModuleBase {

	public static Item TESLA_KIT;
	public static Item TESLA_CARD;
	public static Block TESLA_AVERAGE_COUNTER;

	@Override
	public void registryEvents() {
		TESLA_KIT = new TeslaPowerKit();
		TESLA_CARD = new TeslaPowerCard();
		TESLA_AVERAGE_COUNTER = new BlockTeslaAverageCounter();
	}

	@Override
	public void preInit(){
		GameRegistry.registerTileEntity(TileEntityTeslaAverageCounter.class, "telsa_average_counter");
	}

	@Override
	public void init(){

	}

	@Override
	public void postInit(){

	}

	@Override
	public String modID(){
		return "tesla";
	}

	@Override
	public Object handleServerGUI(int ID, EntityPlayer player, World world, int x, int y, int z){
		BlockPos pos = new BlockPos(x, y, z);
		if(world.getTileEntity(pos) instanceof TileEntityTeslaAverageCounter)
			return new ContainerAverageCounter(player, (TileEntityTeslaAverageCounter) world.getTileEntity(pos));
		return null;
	}

	@Override
	public Object handleClientGUI(int ID, EntityPlayer player, World world, int x, int y, int z){
		BlockPos pos = new BlockPos(x, y, z);
		if(world.getTileEntity(pos) instanceof TileEntityTeslaAverageCounter)
			return new GuiAverageCounter(new ContainerAverageCounter(player, (TileEntityTeslaAverageCounter) world.getTileEntity(pos)));
		return null;
	}
}
