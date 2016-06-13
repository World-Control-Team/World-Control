package worldcontrolteam.worldcontrol;

import org.apache.logging.log4j.Level;

import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLLog;

public class WCUtility {
	public static void log(Level logLevel, Object object){
		FMLLog.log(WorldControl.MODID, logLevel, String.valueOf(object));
	}
	
	public static void debug(Object object){
		log(Level.DEBUG, object);
	}
	
	public static void error(Object object){
		log(Level.ERROR, object);
	}
	
	public static void info(Object object){
		log(Level.INFO, object);
	}
	
	public static String translate(String key){
		key = "msg.worldcontrol." + key;
		if(I18n.canTranslate(key))//TODO: used undepreciated methods
			return I18n.translateToLocal(key);
		else
			return I18n.translateToFallback(key);
	}
	
	public static String translateFormatted(String key, Object... format){
		key = "msg.worldcontrol." + key;
		if(I18n.canTranslate(key))//TODO: used undepreciated methods
			return String.format(I18n.translateToLocal(key), format);//I18n.translateToLocalFormatted(key, format);
		else
			return String.format(I18n.translateToFallback(key), format);
	}
}
