package worldcontrolteam.worldcontrol.utils;


import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WCConfig {

    public static Configuration config;

    public static List<String> howlerAlarmSounds;
    public static int maxAlarmRange;
    public static int alarmRange;
    public static int SMPmaxAlarmRange;
    public static boolean useCustomSounds;
    private static String[] defaultAudio = {"default", "sci-fi"};

    public static void init(File configFile){
        if(config == null){
            config = new Configuration(configFile);
            loadConfig();
        }

    }

    public static void loadConfig(){
        //Put config here
        howlerAlarmSounds = new ArrayList<String>(Arrays.asList(config.get("Alarms", "Howler Alarm Sounds", defaultAudio, "Add a new sound to the howler alarm.").getStringList()));
        maxAlarmRange = config.get("Alarms", "maxAlarmRange", 128, "Maximum audio range for alarms").getInt(128);
        alarmRange = config.get("Alarms", "alarmRange", 64, "Default alarm range").getInt(64);
        SMPmaxAlarmRange = config.get("Alarms", "SMPMaxAlarmRange", 256).getInt(256);
        useCustomSounds = config.get("Alarms", "UseCustomSounds", true, "Allow the use of custom sounds").getBoolean(true);

        if(config.hasChanged()){
            config.save();
        }
    }
}

