package worldcontrolteam.worldcontrol.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;

public class PacketUpdateShowLabels implements IMessage {
  public boolean value;
  public BlockPos pos;

  public PacketUpdateShowLabels() {
  }

  public PacketUpdateShowLabels(boolean paneUpdate, BlockPos pos) {
    this.value = paneUpdate;
    this.pos = pos;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    value = buf.readBoolean();
    pos = BlockPos.fromLong(buf.readLong());
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeBoolean(this.value);
    buf.writeLong(pos.toLong());
  }


  public static class Handler implements IMessageHandler<PacketUpdateShowLabels, IMessage> {

    @Override
    public IMessage onMessage(PacketUpdateShowLabels message, MessageContext ctx) {
      World world = ctx.getServerHandler().player.getEntityWorld();
      TileEntity tile = world.getTileEntity(message.pos);
      if (tile instanceof TileEntityInfoPanel) {
        TileEntityInfoPanel panel = (TileEntityInfoPanel) tile;
        panel.updateShowLabels(message.value);
      }
      return null;
    }
  }
}
