package worldcontrolteam.worldcontrol.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import worldcontrolteam.worldcontrol.client.GuiLib;
import worldcontrolteam.worldcontrol.client.gui.GuiRemotePanel;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.inventory.container.ContainerRemotePanel;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		if(ID == GuiLib.REMOTE_PANEL){
			return new ContainerRemotePanel(player.inventory, player.getHeldItemMainhand(), new InventoryItem(player.getHeldItemMainhand()));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		if(ID == GuiLib.REMOTE_PANEL){
			return new GuiRemotePanel(player.inventory, player.getHeldItemMainhand(), new InventoryItem(player.getHeldItemMainhand()), player);
		}
		return null;
	}
}
