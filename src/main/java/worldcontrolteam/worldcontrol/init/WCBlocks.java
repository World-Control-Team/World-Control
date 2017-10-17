package worldcontrolteam.worldcontrol.init;


import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import worldcontrolteam.worldcontrol.blocks.BlockBasicTileProvider;
import worldcontrolteam.worldcontrol.blocks.BlockHowlerAlarm;
import worldcontrolteam.worldcontrol.blocks.BlockIndustrialAlarm;
import worldcontrolteam.worldcontrol.blocks.fluids.FluidMercury;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;

public class WCBlocks {

    public static Block INDUSTRIAL_ALARM = new BlockIndustrialAlarm();
    public static Block HOWLER_ALARM = new BlockHowlerAlarm();

    public static Fluid MERCURY = new FluidMercury();

    //@SubscribeEvent
    public static void registerBlocks(/*RegistryEvent.Register<Block> event*/){
        //IForgeRegistry<Block> registry = event.getRegistry();
        for (Block block : BlockBasicTileProvider.wcBlocks){
            GameRegistry.register(block);
            GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }



        GameRegistry.registerTileEntity(TileEntityHowlerAlarm.class, "HowlerAlarm");
    }
}
