package worldcontrolteam.worldcontrol.network.messages;


import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;


public class PacketUpdateHowlerAlarm implements IMessage {
    public int range;
    public String soundName;
    public BlockPos pos;

    public PacketUpdateHowlerAlarm() {
    }

    public PacketUpdateHowlerAlarm(int range, String sound, BlockPos pos) {
        this.range = range;
        this.soundName = sound;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        soundName = ByteBufUtils.readUTF8String(buf);
        range = buf.readInt();
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, soundName);
        buf.writeInt(range);
        buf.writeLong(pos.toLong());
    }


    public static class Handler implements IMessageHandler<PacketUpdateHowlerAlarm, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateHowlerAlarm message, MessageContext ctx) {
            World world = ctx.getServerHandler().player.getEntityWorld();
            TileEntity tile = world.getTileEntity(message.pos);
            if (tile instanceof TileEntityHowlerAlarm) {
                TileEntityHowlerAlarm alarm = (TileEntityHowlerAlarm) tile;
                alarm.setRange(message.range);
                alarm.setSound(message.soundName);
            }
            return null;
        }
    }
}
