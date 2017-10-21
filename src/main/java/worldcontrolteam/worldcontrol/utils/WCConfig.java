package worldcontrolteam.worldcontrol.utils;


import com.google.common.collect.Lists;
import net.minecraftforge.common.config.Config;
import worldcontrolteam.worldcontrol.WorldControl;

import java.util.List;

@Config(modid = WorldControl.MODID)
public class WCConfig {

    public static List<String> howlerAlarmSounds = Lists.newArrayList("default", "sci-fi");
    public static int maxAlarmRange = 128;
    public static int alarmRange = 64;
    public static int SMPmaxAlarmRange = 256;
    public static boolean useCustomSounds = true;
}