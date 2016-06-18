package worldcontrolteam.worldcontrol.crossmod;

public abstract class ModuleBase {

    public abstract void preInit();
    public abstract void init();
    public abstract void postInit();
    public abstract String modID();

}
