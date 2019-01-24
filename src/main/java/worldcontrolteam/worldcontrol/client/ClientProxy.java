package worldcontrolteam.worldcontrol.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.CommonProxy;
import worldcontrolteam.worldcontrol.api.card.compat.IProviderCard;
import worldcontrolteam.worldcontrol.client.model.infopanel.ModelLoaderInfoPanel;
import worldcontrolteam.worldcontrol.client.render.RenderHeatMonitor;
import worldcontrolteam.worldcontrol.client.render.RenderInfoPanel;
import worldcontrolteam.worldcontrol.init.IModelRegistrar;
import worldcontrolteam.worldcontrol.init.Translator;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.network.img.IImageGrabber;
import worldcontrolteam.worldcontrol.network.img.ImageGrabber;
import worldcontrolteam.worldcontrol.screen.img.ScreenImageMarshaller;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;
import worldcontrolteam.worldcontrol.utils.WCUtility;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    private static Translator translator = new Translator.ClientTranslator();
    private ImageGrabber grabber;

    @Override
    public Translator getSidedTranslator() {
        return translator;
    }

    @Override
    public IImageGrabber getImageGrabber() {
        return grabber;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        //ModelLoaderRegistry.registerLoader(new CustomModelLoader());
        ModelLoaderRegistry.registerLoader(new ModelLoaderInfoPanel());
        MinecraftForge.EVENT_BUS.register(this);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBaseReactorHeatMonitor.class, new RenderHeatMonitor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInfoPanel.class, new RenderInfoPanel());
        AlarmAudioLoader.checkAndCreateFolders(event.getModConfigurationDirectory());
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new AlarmAudioLoader.TextureSetting());
    }

    @Override
    public void init() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            if (tintIndex == 1) {
                InventoryItem inv = new InventoryItem(stack);
                if (!inv.getStackInSlot(0).isEmpty())
                    if (inv.getStackInSlot(0).getItem() instanceof IProviderCard)
                        return ((IProviderCard) inv.getStackInSlot(0).getItem()).getCardColor();
            }
            return -1;
        }, WCContent.REMOTE_PANEL);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) -> world != null && pos != null ? WCUtility.getTileEntity(world, pos, TileEntityHowlerAlarm.class).map(TileEntityHowlerAlarm::getColor).orElse(16777215) : 16777215, WCContent.HOWLER_ALARM);

        grabber = new ImageGrabber();
        ScreenImageMarshaller.INSTANCE.initGL();
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        WCContent.BLOCKS.stream().filter(b -> b instanceof IModelRegistrar).map(b -> (Block & IModelRegistrar) b).forEach(b -> b.registerModels(event));
        WCContent.ITEMS.stream().filter(i -> i instanceof IModelRegistrar).map(i -> (Item & IModelRegistrar) i).forEach(i -> i.registerModels(event));
    }

    @SubscribeEvent
    public void registerTextures(TextureStitchEvent event) {

    }
}
