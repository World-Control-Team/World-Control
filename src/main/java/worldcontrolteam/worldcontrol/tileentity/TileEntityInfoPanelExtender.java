package worldcontrolteam.worldcontrol.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import worldcontrolteam.worldcontrol.blocks.BlockInfoPanel;

/**
 * File created by mincrmatt12 on 6/15/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public class TileEntityInfoPanelExtender extends TileEntity {
    public BlockPos origin;
    public EnumFacing facing;

    public TileEntityInfoPanelExtender() {

    }

    public void init() {
        this.facing = (EnumFacing) world.getBlockState(getPos()).getProperties().get(BlockInfoPanel.FACING);
    }
}
