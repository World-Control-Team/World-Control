package worldcontrolteam.worldcontrol.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.init.WCCapabilities;
import worldcontrolteam.worldcontrol.init.WCItems;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;

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
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (!player.getHeldItemMainhand().isEmpty())
                if (player.getHeldItemMainhand().getItem() == WCItems.REMOTE_PANEL) {
                    // ItemRemoteMonitor remote = (ItemRemoteMonitor)
                    // player.getHeldItem().getItem();
                    InventoryItem itemInv = new InventoryItem(player.getHeldItemMainhand());
                    // NCLog.fatal(itemInv.getStackInSlot(0));
                    if (itemInv.getStackInSlot(0).isEmpty() || !(itemInv.getStackInSlot(0).hasCapability(WCCapabilities.CARD_HOLDER, null)))
                        return null;
                    if (message.stack.hasTagCompound()) {
                        ItemStack stackz = itemInv.getStackInSlot(0).copy();
                        stackz.setTagCompound(message.stack.getTagCompound());
                        itemInv.setInventorySlotContents(0, stackz);

                    }
                    // NCLog.fatal("CLIENT RECIEVE: " +
                    // ItemStackUtils.getTagCompound(itemInv.getStackInSlot(0)).getInteger("energyL"));
                }
            return null;
        }
    }
}
