package worldcontrolteam.worldcontrol.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.api.card.IProviderCard;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.items.ItemCardTime;
import worldcontrolteam.worldcontrol.items.WCItems;
import worldcontrolteam.worldcontrol.utils.NBTUtils;

import java.util.HashMap;
import java.util.Map;

public class PacketClientRemotePanel implements IMessage {

    private ItemStack stack;

    public PacketClientRemotePanel() {
    }

    public PacketClientRemotePanel(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
       this.stack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, stack);
    }


    public static class Handler implements IMessageHandler<PacketClientRemotePanel, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketClientRemotePanel message, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if(player.getHeldItemMainhand() != null) {
                if (player.getHeldItemMainhand().getItem() == WCItems.remotePanel) {
                    //ItemRemoteMonitor remote = (ItemRemoteMonitor) player.getHeldItem().getItem();
                    InventoryItem itemInv = new InventoryItem(player.getHeldItemMainhand());
                    //NCLog.fatal(itemInv.getStackInSlot(0));
                    if (itemInv.getStackInSlot(0) == null || !(itemInv.getStackInSlot(0).getItem() instanceof IProviderCard)) {
                        return null;
                    }
                    if(message.stack.hasTagCompound()) {
                        ItemStack stackz = itemInv.getStackInSlot(0).copy();
                        stackz.setTagCompound(message.stack.getTagCompound());
                        itemInv.setInventorySlotContents(0, stackz);

                    }
                    //NCLog.fatal("CLIENT RECIEVE: " + ItemStackUtils.getTagCompound(itemInv.getStackInSlot(0)).getInteger("energyL"));
                }
            }
            return null;
        }
    }
}
