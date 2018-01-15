package worldcontrolteam.worldcontrol.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.blocks.BlockHowlerAlarm;
import worldcontrolteam.worldcontrol.blocks.BlockIndustrialAlarm;
import worldcontrolteam.worldcontrol.items.*;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;

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
                new BlockHowlerAlarm()
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
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ITEMS.stream().filter(i -> i instanceof IModelRegistrar).map(i -> (Item & IModelRegistrar) i).forEach(i -> i.registerModels(event));
        BLOCKS.stream().filter(b -> b instanceof IModelRegistrar).map(b -> (Block & IModelRegistrar) b).forEach(b -> b.registerModels(event));
    }

}