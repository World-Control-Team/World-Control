package worldcontrolteam.worldcontrol.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import worldcontrolteam.worldcontrol.network.messages.PacketClientRemotePanel;
import worldcontrolteam.worldcontrol.network.messages.PacketClientUpdateMonitor;
import worldcontrolteam.worldcontrol.network.messages.PacketServerRemotePanel;
import worldcontrolteam.worldcontrol.network.messages.PacketUpdateHowlerAlarm;

public class ChannelHandler {
    public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("WorldControl");

    public static void init() {
        network.registerMessage(PacketServerRemotePanel.Handler.class, PacketServerRemotePanel.class, 0, Side.SERVER);
        network.registerMessage(PacketClientRemotePanel.Handler.class, PacketClientRemotePanel.class, 1, Side.CLIENT);
        network.registerMessage(PacketUpdateHowlerAlarm.Handler.class, PacketUpdateHowlerAlarm.class, 2, Side.SERVER);
        network.registerMessage(PacketClientUpdateMonitor.Handler.class, PacketClientUpdateMonitor.class, 3, Side.SERVER);
    }

}
