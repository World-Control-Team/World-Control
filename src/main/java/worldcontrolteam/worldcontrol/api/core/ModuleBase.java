package worldcontrolteam.worldcontrol.api.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class ModuleBase {

    /**
     * Used to create Instances of Items and Blocks, the registry all instances will be loaded into the registry
     * immediately after this call.
     */
    public abstract void registryEvents();

    public abstract void preInit();

    public abstract void init();

    public abstract void postInit();

    /**
     * Called during preInit, init and postinit. Used to check if
     * Loader.isModLoaded()
     *
     * @return mod id of module
     */
    public abstract String modID();

    /**
     * If module does not have a modID (ex, module is for an api, use this function to load in the integration manually.
     *
     * @return true if module should load, false otherwise
     * <p>
     * ONLY USE THIS FUNCTION IF MODULE CANNOT BE LINKED TO A MOD ID
     */
    public boolean apiAvaliable() {
        return false;
    }

    public abstract Object handleServerGUI(int ID, EntityPlayer player, World world, int x, int y, int z);

    public abstract Object handleClientGUI(int ID, EntityPlayer player, World world, int x, int y, int z);

}
