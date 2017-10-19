package worldcontrolteam.worldcontrol.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;

public class PacketClientUpdateMonitor implements IMessage {

    public boolean toUpdate;
    public int threshold;
    public BlockPos pos;


    public PacketClientUpdateMonitor() {
    }

    public PacketClientUpdateMonitor(int newThreshold, boolean newState, BlockPos pos) {
        this.toUpdate = newState;
        this.pos = pos;
        this.threshold = newThreshold;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.toUpdate = byteBuf.readBoolean();
        this.pos = BlockPos.fromLong(byteBuf.readLong());
        this.threshold = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeBoolean(toUpdate);
        byteBuf.writeLong(pos.toLong());
        byteBuf.writeInt(threshold);
    }

    public static class Handler implements IMessageHandler<PacketClientUpdateMonitor, IMessage> {

        @Override
        public IMessage onMessage(PacketClientUpdateMonitor message, MessageContext ctx) {
            World world = ctx.getServerHandler().player.getEntityWorld();
            TileEntity tile = world.getTileEntity(message.pos);
            if (tile instanceof TileEntityBaseReactorHeatMonitor) {
                TileEntityBaseReactorHeatMonitor monitor = (TileEntityBaseReactorHeatMonitor) tile;
                monitor.setInverse(message.toUpdate);
                monitor.setThreshhold(message.threshold);
            }
            return null;
        }
    }
}
