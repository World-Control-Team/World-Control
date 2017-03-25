package worldcontrolteam.worldcontrol.utils;


import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WCConfig {

    public static Configuration config;

    public static List<String> howlerAlarmSounds;
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


        if(config.hasChanged()){
            config.save();
        }
    }
}

