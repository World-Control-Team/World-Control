package worldcontrolteam.worldcontrol.init;


import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import worldcontrolteam.worldcontrol.blocks.BlockHowlerAlarm;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;

public class WCBlocks {

    public static Block HOWLER_ALARM;


    public static void registerBlocks(){
        HOWLER_ALARM = new BlockHowlerAlarm();
        GameRegistry.registerTileEntity(TileEntityHowlerAlarm.class, "HowlerAlarm");
    }
}
