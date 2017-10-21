package worldcontrolteam.worldcontrol.utils;


import net.minecraftforge.common.config.Config;
import worldcontrolteam.worldcontrol.WorldControl;

@Config(modid = WorldControl.MODID)
public class WCConfig {

    public static String[] howlerAlarmSounds = {"default", "sci-fi"};
    public static int maxAlarmRange = 128;
    public static int alarmRange = 64;
    public static int SMPmaxAlarmRange = 256;
    public static boolean useCustomSounds = true;
}