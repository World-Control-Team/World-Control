package worldcontrolteam.worldcontrol.utils;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import worldcontrolteam.worldcontrol.WorldControl;

public class WCUtility {

	public static final int RED = RGBToInt(255, 0, 0);
	public static final int BLACK = RGBToInt(0, 0, 0);
	public static final int GREEN = RGBToInt(0, 255, 0);
	public static final int BROWN = RGBToInt(165, 42, 42);
	public static final int BLUE = RGBToInt(0, 0, 255);
	public static final int PURPLE = RGBToInt(128, 0, 128);
	public static final int CYAN = RGBToInt(0, 255, 255);
	public static final int SILVER = RGBToInt(192, 192, 192);
	public static final int GRAY = RGBToInt(105, 105, 105);
	public static final int PINK = RGBToInt(255, 20, 147);
	public static final int LIME = RGBToInt(50, 205, 50);
	public static final int YELLOW = RGBToInt(255, 215, 0);
	public static final int LIGHT_BLUE = RGBToInt(173, 216, 230);
	public static final int MAGENTA = RGBToInt(255, 0, 255);
	public static final int ORANGE = RGBToInt(255, 69, 0);
	public static final int WHITE = RGBToInt(255, 255, 255);

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

	@SideOnly(Side.CLIENT)
	public static String translate(String key){
		key = "msg.worldcontrol." + key;
		if(I18n.hasKey(key))// TODO: used undepreciated methods
			return I18n.format(key);
		else return I18n.format(key);
	}

	@SideOnly(Side.CLIENT)
	public static String translateFormatted(String key, Object... format){
		key = "msg.worldcontrol." + key;
		if(I18n.hasKey(key))// TODO: used undepreciated methods
			return I18n.format(key, format);// I18n.translateToLocalFormatted(key, format);
		else return I18n.format(key, format);
	}

	public static int RGBToInt(final int r, final int g, final int b){
		int color = 0;
		color = color | b;
		color = color | g << 8;
		color = color | r << 16;

		return color;
	}
}
