package worldcontrolteam.worldcontrol.init;


import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import worldcontrolteam.worldcontrol.blocks.BlockHowlerAlarm;
import worldcontrolteam.worldcontrol.blocks.BlockIndustrialAlarm;
import worldcontrolteam.worldcontrol.blocks.fluids.FluidMercury;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;

public class WCBlocks {

    public static Block INDUSTRIAL_ALARM;
    public static Block HOWLER_ALARM;

    public static Fluid MERCURY;

    public static void registerBlocks(){
        INDUSTRIAL_ALARM = new BlockIndustrialAlarm();
        HOWLER_ALARM = new BlockHowlerAlarm();
        GameRegistry.registerTileEntity(TileEntityHowlerAlarm.class, "HowlerAlarm");
        
        MERCURY = new FluidMercury();
    }
}
