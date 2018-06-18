package worldcontrolteam.worldcontrol.server;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.CommonProxy;
import worldcontrolteam.worldcontrol.init.Translator;

@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy {

    private static Translator translator = new Translator.ServerTranslator();

    @Override
    public Translator getSidedTranslator() {
        return translator;
    }

}