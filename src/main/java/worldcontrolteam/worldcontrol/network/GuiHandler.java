package worldcontrolteam.worldcontrol.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.client.gui.GuiRemotePanel;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.inventory.container.ContainerRemotePanel;
import worldcontrolteam.worldcontrol.utils.GuiLib;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		if(ID == GuiLib.REMOTE_PANEL)
			return new ContainerRemotePanel(player.inventory, player.getHeldItemMainhand(), new InventoryItem(player.getHeldItemMainhand()));
		//ALWAY KEEP THIS AS LAST CALL
		return WorldControl.modules.guiHandlerServer(ID, player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		if(ID == GuiLib.REMOTE_PANEL)
			return new GuiRemotePanel(player.inventory, player.getHeldItemMainhand(), new InventoryItem(player.getHeldItemMainhand()), player);
		//ALWAY KEEP THIS AS LAST CALL
		return WorldControl.modules.guiHandlerClient(ID, player, world, x, y, z);
	}
}
