package worldcontrolteam.worldcontrol.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;

public class PacketUpdateClientMonitorCard implements IMessage {

    public BlockPos pos;
    public ItemStack card;

    public PacketUpdateClientMonitorCard() {}

    public PacketUpdateClientMonitorCard(BlockPos pos, ItemStack card){
        this.card = card;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.card = ByteBufUtils.readItemStack(buf);
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, card);
        buf.writeLong(pos.toLong());
    }

    public static class Handler implements IMessageHandler<PacketUpdateClientMonitorCard, IMessage> {


        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketUpdateClientMonitorCard message, MessageContext ctx) {
            WorldClient server = Minecraft.getMinecraft().world;
            TileEntity tileEntity = server.getTileEntity(message.pos);
            if(tileEntity instanceof TileEntityInfoPanel) {
                TileEntityInfoPanel panel = (TileEntityInfoPanel) tileEntity;
                if(message.card.hasTagCompound()) {
                    panel.setCardNBT(message.card.getTagCompound());
                }
               // panel.setStackInSlot(0, message.card);
            }
            return null;
        }
    }
}
