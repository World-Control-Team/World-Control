package worldcontrolteam.worldcontrol.init;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import worldcontrolteam.worldcontrol.blocks.BlockBasicTileProvider;
import worldcontrolteam.worldcontrol.blocks.BlockHowlerAlarm;
import worldcontrolteam.worldcontrol.blocks.BlockIndustrialAlarm;
import worldcontrolteam.worldcontrol.blocks.fluids.FluidMercury;
import worldcontrolteam.worldcontrol.items.WCBaseItem;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;

public class WCBlocks {

    public static Block INDUSTRIAL_ALARM = new BlockIndustrialAlarm();
    public static Block HOWLER_ALARM = new BlockHowlerAlarm();

    public static Fluid MERCURY = new FluidMercury();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Block block : BlockBasicTileProvider.wcBlocks) {
            event.getRegistry().register(block);
        }

        GameRegistry.registerTileEntity(TileEntityHowlerAlarm.class, "HowlerAlarm");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        BlockBasicTileProvider.wcBlocks.stream().map(block -> new ItemBlock(block).setRegistryName(block.getRegistryName())).forEach(event.getRegistry()::register);

        WCBaseItem.wcItems.forEach(event.getRegistry()::register);
    }
}