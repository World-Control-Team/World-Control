package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks;

import ic2.api.reactor.IReactor;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.ReactorUtils;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;

public class TileEntityIC2ReactorMonitor extends TileEntityBaseReactorHeatMonitor{


    @Override
    public int getCurrentHeat() {
        IReactor reactor = ReactorUtils.getReactorAt(world, referenceBlock);
        if(reactor != null){
            return reactor.getHeat();
        }
        return -1;
    }
}
