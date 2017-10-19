package worldcontrolteam.worldcontrol;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.RegistryBuilder;
import worldcontrolteam.worldcontrol.api.card.CardManager;
import worldcontrolteam.worldcontrol.api.card.ICardHolder;
import worldcontrolteam.worldcontrol.api.core.WorldControlAPI;
import worldcontrolteam.worldcontrol.api.thermometer.IHeatSeeker;
import worldcontrolteam.worldcontrol.crossmod.Modules;
import worldcontrolteam.worldcontrol.init.WCBlocks;
import worldcontrolteam.worldcontrol.init.WCCapabilities;
import worldcontrolteam.worldcontrol.init.WCItems;
import worldcontrolteam.worldcontrol.init.WCRegistries;
import worldcontrolteam.worldcontrol.items.ItemCard;
import worldcontrolteam.worldcontrol.items.ItemThermometer;
import worldcontrolteam.worldcontrol.network.ChannelHandler;
import worldcontrolteam.worldcontrol.network.GuiHandler;
import worldcontrolteam.worldcontrol.utils.WCConfig;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.io.File;
import java.util.ArrayList;

@Mod(modid = WorldControl.MODID, version = "@VERSION@")
public class WorldControl {

    public static final String MODID = "worldcontrol";
    @Mod.Instance(value = "worldcontrol")
    public static WorldControl instance;
    @SidedProxy(clientSide = "worldcontrolteam.worldcontrol.client.ClientProxy", serverSide = "worldcontrolteam.worldcontrol.CommonProxy")
    public static CommonProxy proxy;
    public static WCCreativeTab TAB = new WCCreativeTab();

    public static Side side; // As in client vs server
    public static Modules modules = new Modules();
    protected static ArrayList<IHeatSeeker> heatSources = new ArrayList<IHeatSeeker>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        WCUtility.info("We are in pre-init!");
        side = event.getSide();
        WCConfig.init(new File(event.getModConfigurationDirectory(), MODID + ".cfg"));
        WorldControlAPI.init(new WCapiImpl());
        CapabilityManager.INSTANCE.register(ICardHolder.class, new WCCapabilities.Storage(), ItemCard.Caps.class);
        proxy.preinit(event);
        WCRegistries.REGISTRY = new RegistryBuilder<CardManager>().setType(CardManager.class).setName(new ResourceLocation(WorldControl.MODID, "card")).setMaxID(255).create();

        modules.registryEvents();
        MinecraftForge.EVENT_BUS.register(WCBlocks.class);
        WCItems.registerItems();

        proxy.registerItemTextures();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        ChannelHandler.init();
        modules.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        WCUtility.info("We are in init!");

        modules.init();

        ItemThermometer.addInHeatTypes(heatSources);

        proxy.init();

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        WCUtility.info("We are in post-init!");

        modules.postInit();
    }
}
