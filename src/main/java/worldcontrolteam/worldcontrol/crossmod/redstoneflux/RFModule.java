package worldcontrolteam.worldcontrol.crossmod.redstoneflux;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.core.ModuleBase;

public class RFModule extends ModuleBase{
    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }

    @Override
    public String modID() {
        return null;
    }

    public boolean apiAvaliable(){
        try {
            Class.forName("cofh.api.energy.IEnergyHandler.class");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Object handleServerGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object handleClientGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
