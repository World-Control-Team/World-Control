package worldcontrolteam.worldcontrol;

import worldcontrolteam.worldcontrol.api.core.WorldControlAPI;
import worldcontrolteam.worldcontrol.api.thermometer.IHeatSeeker;

public class WCapiImpl implements WorldControlAPI.IWorldControlAPI {


    @Override
    public void addThermometerModule(IHeatSeeker m) {
        WorldControl.heatTypez.add(m);
    }
}
