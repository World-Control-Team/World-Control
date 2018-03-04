package worldcontrolteam.worldcontrol.init;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.WorldControl;

public interface Translator {

    static Translator getSidedTranslator() {
        return WorldControl.PROXY.getSidedTranslator();
    }

    String translate(String key);

    String translate(String key, Object... format);


    @SideOnly(Side.CLIENT)
    class ClientTranslator implements Translator {

        @Override
        public String translate(String key) {
            key = "msg.worldcontrol." + key;
            return net.minecraft.client.resources.I18n.format(key);
        }

        @Override
        public String translate(String key, Object... format) {
            key = "msg.worldcontrol." + key;
            return net.minecraft.client.resources.I18n.format(key, format);
        }
    }

    class ServerTranslator implements Translator {

        @Override
        public String translate(String key) {
            key = "msg.worldcontrol." + key;
            return net.minecraft.util.text.translation.I18n.translateToLocal(key);
        }

        @Override
        public String translate(String key, Object... format) {
            key = "msg.worldcontrol." + key;
            return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(key, format);
        }
    }
}