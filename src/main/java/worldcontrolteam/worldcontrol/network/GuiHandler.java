package worldcontrolteam.worldcontrol.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.client.gui.GuiIndustrialAlarm;
import worldcontrolteam.worldcontrol.client.gui.GuiRemotePanel;
import worldcontrolteam.worldcontrol.container.ContainerEmpty;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.inventory.container.ContainerRemotePanel;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;
import worldcontrolteam.worldcontrol.utils.GuiLib;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if(ID == GuiLib.REMOTE_PANEL) {
			return new ContainerRemotePanel(player.inventory, player.getHeldItemMainhand(), new InventoryItem(player.getHeldItemMainhand()));
		}else if(ID == GuiLib.INDUSTRIAL_ALARM){
			return new ContainerEmpty(tile);
		}
		//ALWAY KEEP THIS AS LAST CALL
		return WorldControl.modules.guiHandlerServer(ID, player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if(ID == GuiLib.REMOTE_PANEL) {
			return new GuiRemotePanel(player.inventory, player.getHeldItemMainhand(), new InventoryItem(player.getHeldItemMainhand()), player);
		}else if(ID == GuiLib.INDUSTRIAL_ALARM){
			return new GuiIndustrialAlarm((TileEntityHowlerAlarm) tile);
		}
		//ALWAY KEEP THIS AS LAST CALL
		return WorldControl.modules.guiHandlerClient(ID, player, world, x, y, z);
	}
}
