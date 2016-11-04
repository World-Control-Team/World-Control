package worldcontrolteam.worldcontrol.crossmod.tesla;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.core.ModuleBase;

public class TeslaModule extends ModuleBase {

    public static final Item TESLA_KIT = new TeslaPowerKit();
    public static final Item TESLA_CARD = new TeslaPowerCard();

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
        return "tesla";
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
