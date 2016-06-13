package xbony2.thechiefssurprise;

import org.apache.logging.log4j.Level;

import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLLog;

public class CSUtility {
	public static void log(Level logLevel, Object object){
		FMLLog.log(TheChiefsSurprise.MODID, logLevel, String.valueOf(object));
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
		key = "tcs." + key;
		if(I18n.canTranslate(key))
			return I18n.translateToLocal(key);
		else
			return I18n.translateToFallback(key);
	}
}
