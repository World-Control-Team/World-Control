package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Created by dmf444 on 8/15/2017. Code originally written for World-Control.
 */
public class TileEntityInfoPanel extends TileEntity{

    public boolean getPowered() {
        return true;
    }

    public EnumFacing getFacing() {
        return EnumFacing.NORTH;
    }
}
