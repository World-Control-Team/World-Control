package worldcontrolteam.worldcontrol.crossmod.extremereactors;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import worldcontrolteam.worldcontrol.api.core.ModuleBase;
import worldcontrolteam.worldcontrol.api.core.WorldControlAPI;
import worldcontrolteam.worldcontrol.crossmod.extremereactors.reactormonitor.BlockReactorMonitor;
import worldcontrolteam.worldcontrol.crossmod.extremereactors.reactormonitor.ExtremeReactorsCard;
import worldcontrolteam.worldcontrol.crossmod.extremereactors.reactormonitor.ExtremeReactorsKit;
import worldcontrolteam.worldcontrol.crossmod.extremereactors.reactormonitor.TileReactorMonitor;

/**
 * Created by dmf444 on 10/15/2017. Code originally written for World-Control.
 */
public class ExtremeReactorsModule extends ModuleBase {

    public static Block REACTOR_MONITOR;
    public static Item REACTOR_KIT, REACTOR_CARD;

    @Override
    public void registryEvents() {
        REACTOR_MONITOR = new BlockReactorMonitor();
        REACTOR_CARD = new ExtremeReactorsCard();
        REACTOR_KIT = new ExtremeReactorsKit();
    }

    @Override
    public void preInit() {
        WorldControlAPI.getInstance().addThermometerModule(new ExtremeReactorHeat());

        GameRegistry.registerTileEntity(TileReactorMonitor.class, "exReactorMonitor");
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
