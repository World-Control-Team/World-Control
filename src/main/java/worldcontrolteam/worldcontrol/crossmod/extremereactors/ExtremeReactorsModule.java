package worldcontrolteam.worldcontrol.crossmod.extremereactors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.core.ModuleBase;
import worldcontrolteam.worldcontrol.api.core.WorldControlAPI;

/**
 * Created by dmf444 on 10/15/2017. Code originally written for World-Control.
 */
public class ExtremeReactorsModule extends ModuleBase{
    @Override
    public void preInit() {
        WorldControlAPI.getInstance().addThermometerModule(new ExtremeReactorHeat());
    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }

    @Override
    public String modID() {
        return "bigreactors";
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
