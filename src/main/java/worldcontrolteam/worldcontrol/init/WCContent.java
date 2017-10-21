package worldcontrolteam.worldcontrol.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import worldcontrolteam.worldcontrol.api.card.CardManager;
import worldcontrolteam.worldcontrol.blocks.BlockHowlerAlarm;
import worldcontrolteam.worldcontrol.blocks.BlockIndustrialAlarm;
import worldcontrolteam.worldcontrol.card.EUStorageManager;
import worldcontrolteam.worldcontrol.card.TimeCardManager;
import worldcontrolteam.worldcontrol.items.*;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber
public class WCContent {

    public static List<Block> BLOCKS = new ArrayList<>();
    public static List<Item> ITEMS = new ArrayList<>();
    public static List<CardManager> CARDS = new ArrayList<>();

    public static void addBlocks(Block... blocks) {
        Collections.addAll(BLOCKS, blocks);
    }
    public static void addItems(Item... items) {
        Collections.addAll(ITEMS, items);
    }
    public static void addCards(CardManager... cards) {
        Collections.addAll(CARDS, cards);
    }

    @ObjectHolder("worldcontrol:remote_panel")
    public static Item REMOTE_PANEL;
    @ObjectHolder("worldcontrol:howler_alarm")
    public static Block HOWLER_ALARM;
    @ObjectHolder("worldcontrol:card")
    public static Item CARD;

    public static void preInit() {
        addItems(
                new ItemThermometer(),
                new ItemCardTime(),
                new ItemRemotePanel(),
                new ItemFluidCard(),
                new ItemFluidKit(),
                new ItemForgeEnergyCard(),
                new ItemForgeEnergyKit(),
                new ItemCard(),
                new ItemKit()
        );
        addBlocks(
                new BlockIndustrialAlarm(),
                new BlockHowlerAlarm()
        );
        addCards(
                new TimeCardManager(),
                new EUStorageManager()
        );

        GameRegistry.registerTileEntity(TileEntityHowlerAlarm.class, "HowlerAlarm");
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

    @SubscribeEvent
    public static void registerCards(RegistryEvent.Register<CardManager> event) {
        CARDS.forEach(event.getRegistry()::register);
    }
}