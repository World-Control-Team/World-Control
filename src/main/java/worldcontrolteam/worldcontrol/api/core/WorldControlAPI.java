package worldcontrolteam.worldcontrol.api.core;

import worldcontrolteam.worldcontrol.api.thermometer.IHeatSeeker;

public class WorldControlAPI {
    public static IWorldControlAPI instance;

    public static IWorldControlAPI getInstance() {
        return instance;
    }

    public static void init(IWorldControlAPI inst) {
        if (instance == null)
            instance = inst;
    }

    public interface IWorldControlAPI {

        /**
         * Must be called in PRE-Init, as modules are loaded in INIT
         *
         * @param m Instance of an IHeatSeeker
         */
        public void addThermometerModule(IHeatSeeker m);

        public void removeModule(Class<? extends ModuleBase> mod);

    }
}
