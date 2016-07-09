package worldcontrolteam.worldcontrol.api.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class ModuleBase {

	public abstract void preInit();

	public abstract void init();

	public abstract void postInit();

	public abstract String modID();

	public abstract Object handleServerGUI(int ID, EntityPlayer player, World world, int x, int y, int z);

	public abstract Object handleClientGUI(int ID, EntityPlayer player, World world, int x, int y, int z);

}
