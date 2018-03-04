package worldcontrolteam.worldcontrol.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.api.card.IProviderCard;
import worldcontrolteam.worldcontrol.client.model.BakedModelInfoPanel;
import worldcontrolteam.worldcontrol.client.model.base.CustomModelLoader;
import worldcontrolteam.worldcontrol.client.model.base.ModelBlock;
import worldcontrolteam.worldcontrol.init.IModelRegistrar;
import worldcontrolteam.worldcontrol.init.Translator;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;
import worldcontrolteam.worldcontrol.utils.WCUtility;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    private static Translator translator = new Translator.ClientTranslator();

    @Override
    public Translator getSidedTranslator() {
        return translator;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ModelLoaderRegistry.registerLoader(new CustomModelLoader());
        MinecraftForge.EVENT_BUS.register(this);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBaseReactorHeatMonitor.class, new HeatMonitorTESR());
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
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        WCContent.BLOCKS.stream().filter(b -> b instanceof IModelRegistrar).map(b -> (Block & IModelRegistrar) b).forEach(b -> b.registerModels(event));
        WCContent.ITEMS.stream().filter(i -> i instanceof IModelRegistrar).map(i -> (Item & IModelRegistrar) i).forEach(i -> i.registerModels(event));

        CustomModelLoader.BLOCK_MODELS.putIfAbsent(new ResourceLocation(WorldControl.MODID + ":models/block/info_panel"), new ModelBlock(BakedModelInfoPanel.class,
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/on/2/15"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_back"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_side"))
        );
        CustomModelLoader.BLOCK_MODELS.putIfAbsent(new ResourceLocation(WorldControl.MODID + ":models/block/info_panel_advanced"), new ModelBlock(BakedModelInfoPanel.class,
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/on/2/15"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_back"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_side"))
        );
        CustomModelLoader.BLOCK_MODELS.putIfAbsent(new ResourceLocation(WorldControl.MODID + ":models/block/info_panel_extender"), new ModelBlock(BakedModelInfoPanel.class,
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/on/2/15"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_back"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_side"))
        );
        CustomModelLoader.BLOCK_MODELS.putIfAbsent(new ResourceLocation(WorldControl.MODID + ":models/block/info_panel_extender_advanced"), new ModelBlock(BakedModelInfoPanel.class,
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/on/2/15"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_back"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_side"))
        );
    }

    @SubscribeEvent
    public void registerTextures(TextureStitchEvent event) {
        for (int i = 0; i < 15; i++)
            for (int i2 = 0; i2 < 16; i2++) {
                event.getMap().registerSprite(new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/off/" + i + "/" + i2));
                event.getMap().registerSprite(new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/on/" + i + "/" + i2));
            }
    }

    @SubscribeEvent
    public void registerModels(CustomModelLoader.CustomModelReloadEvent event) {
        CustomModelLoader.BLOCK_MODELS.putIfAbsent(new ResourceLocation(WorldControl.MODID + ":models/block/info_panel"), new ModelBlock(BakedModelInfoPanel.class,
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/on/2/15"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_back"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_side"))
        );
        CustomModelLoader.BLOCK_MODELS.putIfAbsent(new ResourceLocation(WorldControl.MODID + ":models/block/info_panel_advanced"), new ModelBlock(BakedModelInfoPanel.class,
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/on/2/15"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_back"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/panel_advanced_side"))
        );
        CustomModelLoader.BLOCK_MODELS.putIfAbsent(new ResourceLocation(WorldControl.MODID + ":models/block/info_panel_extender"), new ModelBlock(BakedModelInfoPanel.class,
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/on/2/15"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_back"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_side"))
        );
        CustomModelLoader.BLOCK_MODELS.putIfAbsent(new ResourceLocation(WorldControl.MODID + ":models/block/info_panel_extender_advanced"), new ModelBlock(BakedModelInfoPanel.class,
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/on/2/15"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_back"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_side"),
                new ResourceLocation(WorldControl.MODID + ":blocks/infopanel/extender_advanced_side"))
        );
    }
}