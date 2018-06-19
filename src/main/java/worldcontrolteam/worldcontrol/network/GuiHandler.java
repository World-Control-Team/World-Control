package worldcontrolteam.worldcontrol.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.client.gui.GuiHowlerAlarm;
import worldcontrolteam.worldcontrol.client.gui.GuiIndustrialAlarm;
import worldcontrolteam.worldcontrol.client.gui.GuiInfoPanel;
import worldcontrolteam.worldcontrol.client.gui.GuiRemotePanel;
import worldcontrolteam.worldcontrol.container.ContainerEmpty;
import worldcontrolteam.worldcontrol.container.ContainerInfoPanel;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.inventory.container.ContainerRemotePanel;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;
import worldcontrolteam.worldcontrol.utils.GuiLib;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = WCUtility.getTileEntity(world, new BlockPos(x, y, z)).orElse(null);
        if (ID == GuiLib.REMOTE_PANEL) {
            return new ContainerRemotePanel(player.inventory, player.getHeldItemMainhand(), new InventoryItem(player.getHeldItemMainhand()));
        } else if (ID == GuiLib.INDUSTRIAL_ALARM) {
            return new ContainerEmpty(tile);
        } else if (ID == GuiLib.HOWLER_ALARM) {
            return new ContainerEmpty(tile);
        } else if (ID == GuiLib.INFO_PANEL) {
            return new ContainerInfoPanel(player, (TileEntityInfoPanel) tile);
        }
        //ALWAY KEEP THIS AS LAST CALL
        return WorldControl.MODULES.guiHandlerServer(ID, player, world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = WCUtility.getTileEntity(world, new BlockPos(x, y, z)).orElse(null);
        if (ID == GuiLib.REMOTE_PANEL) {
            return new GuiRemotePanel(player.inventory, player.getHeldItemMainhand(), new InventoryItem(player.getHeldItemMainhand()), player);
        } else if (ID == GuiLib.INDUSTRIAL_ALARM) {
            return new GuiIndustrialAlarm((TileEntityHowlerAlarm) tile);
        } else if (ID == GuiLib.HOWLER_ALARM) {
            return new GuiHowlerAlarm((TileEntityHowlerAlarm) tile);
        } else if (ID == GuiLib.INFO_PANEL) {
            return new GuiInfoPanel(new ContainerInfoPanel(player, (TileEntityInfoPanel) tile));
        }
        //ALWAY KEEP THIS AS LAST CALL
        return WorldControl.MODULES.guiHandlerClient(ID, player, world, x, y, z);
    }
}
