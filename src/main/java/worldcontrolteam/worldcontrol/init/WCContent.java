package worldcontrolteam.worldcontrol.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.blocks.BlockHowlerAlarm;
import worldcontrolteam.worldcontrol.blocks.BlockIndustrialAlarm;
import worldcontrolteam.worldcontrol.blocks.BlockInfoPanel;
import worldcontrolteam.worldcontrol.blocks.BlockInfoPanelExtender;
import worldcontrolteam.worldcontrol.items.*;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanelExtender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber
public class WCContent {

    public static List<Block> BLOCKS = new ArrayList<>();
    public static List<Item> ITEMS = new ArrayList<>();
    @ObjectHolder("worldcontrol:remote_panel")
    public static Item REMOTE_PANEL;
    @ObjectHolder("worldcontrol:fluid_card")
    public static Item FLUID_CARD;
    @ObjectHolder("worldcontrol:forge_energy_card")
    public static Item FORGE_ENERGY_CARD;
    @ObjectHolder("worldcontrol:upgrade_cards")
    public static Item UPGRADE;
    @ObjectHolder("worldcontrol:howler_alarm")
    public static Block HOWLER_ALARM;
    @ObjectHolder("worldcontrol:info_panel")
    public static Block INFO_PANEL;
    @ObjectHolder("worldcontrol:info_panel_advanced")
    public static Block INFO_PANEL_ADVANCED;
    @ObjectHolder("worldcontrol:info_panel_extender")
    public static Block INFO_PANEL_EXTENDER;
    @ObjectHolder("worldcontrol:info_panel_extender_advanced")
    public static Block INFO_PANEL_EXTENDER_ADVANCED;

    public static void addBlocks(Block... blocks) {
        Collections.addAll(BLOCKS, blocks);
    }

    public static void addItems(Item... items) {
        Collections.addAll(ITEMS, items);
    }

    public static void preInit() {
        addItems(
                new ItemThermometer(),
                new ItemCardTime(),
                new ItemRemotePanel(),
                new ItemFluidCard(),
                new ItemFluidKit(),
                new ItemForgeEnergyCard(),
                new ItemForgeEnergyKit(),
                new ItemUpgrade()
        );
        addBlocks(
                new BlockIndustrialAlarm(),
                new BlockHowlerAlarm(),
                new BlockInfoPanel(Material.IRON, "info_panel", false),
                new BlockInfoPanel(Material.IRON, "info_panel_advanced", true),
                new BlockInfoPanelExtender("info_panel_extender", false),
                new BlockInfoPanelExtender("info_panel_extender_advanced", true)
        );

        GameRegistry.registerTileEntity(TileEntityHowlerAlarm.class, WorldControl.MODID + ":howleralarm");
        GameRegistry.registerTileEntity(TileEntityInfoPanel.class, WorldControl.MODID + ":infopanel");
        GameRegistry.registerTileEntity(TileEntityInfoPanelExtender.class, WorldControl.MODID + ":infopanelext");
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BLOCKS.forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        BLOCKS.stream().filter(b -> b instanceof IItemBlockFactory).map(b -> (Block & IItemBlockFactory) b).forEach(b -> event.getRegistry().register(b.createItemBlock()));
        ITEMS.forEach(event.getRegistry()::register);
    }

}
