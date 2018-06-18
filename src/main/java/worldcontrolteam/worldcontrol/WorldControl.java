package worldcontrolteam.worldcontrol;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import worldcontrolteam.worldcontrol.api.core.WorldControlAPI;
import worldcontrolteam.worldcontrol.api.thermometer.IHeatSeeker;
import worldcontrolteam.worldcontrol.crossmod.Modules;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.items.ItemThermometer;
import worldcontrolteam.worldcontrol.network.ChannelHandler;
import worldcontrolteam.worldcontrol.network.GuiHandler;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.ArrayList;

@Mod(modid = WorldControl.MODID, name = WorldControl.NAME, version = "@VERSION@")
public class WorldControl {

    public static final String MODID = "worldcontrol";
    public static final String NAME = "World Control";
    @Mod.Instance("worldcontrol")
    public static WorldControl INSTANCE;
    @SidedProxy(clientSide = "worldcontrolteam.worldcontrol.client.ClientProxy", serverSide = "worldcontrolteam.worldcontrol.server.ServerProxy")
    public static CommonProxy PROXY;
    public static WCCreativeTab TAB = new WCCreativeTab();

    public static Side SIDE; // As in client vs server
    public static Modules MODULES = new Modules();
    protected static ArrayList<IHeatSeeker> HEAT_SOURCES = new ArrayList<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SIDE = event.getSide();
        ProgressManager.ProgressBar bar = ProgressManager.push("World Control", 1);
        WCUtility.info("We are in pre-init!");
        bar.step("Initializing API");
        WorldControlAPI.init(new WCapiImpl());
        PROXY.preInit(event);
        MODULES.registryEvents();
        WCContent.preInit();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        ChannelHandler.init();
        MODULES.preInit();
        ProgressManager.pop(bar);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        WCUtility.info("We are in init!");

        MODULES.init();

        ItemThermometer.addInHeatTypes(HEAT_SOURCES);

        PROXY.init();

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        WCUtility.info("We are in post-init!");

        MODULES.postInit();
    }
}
