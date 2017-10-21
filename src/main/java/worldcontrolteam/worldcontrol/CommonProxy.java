package worldcontrolteam.worldcontrol;


import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import worldcontrolteam.worldcontrol.init.Translator;


public abstract class CommonProxy {

	public void init(){}

    public void registerItemTextures() {
    }

    public void preInit(FMLPreInitializationEvent event) {
    }

    public abstract Translator getSidedTranslator();
}
