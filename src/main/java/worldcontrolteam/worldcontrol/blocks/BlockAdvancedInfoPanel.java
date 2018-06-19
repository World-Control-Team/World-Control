package worldcontrolteam.worldcontrol.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.screen.IScreenContainer;

/**
 * File created by mincrmatt12 on 6/19/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public class BlockAdvancedInfoPanel extends BlockBasicRotate implements IScreenContainer
{
    public BlockAdvancedInfoPanel() {
        super(Material.IRON, "worldcontrol:info_panel_advanced");
    }

    @Override
    public TileEntity createTile(World world, int meta) {
        return null;
    }

    @Override
    public boolean hasGUI() {
        return false;
    }

    @Override
    public int guiID() {
        return 0;
    }

    @Override
    public EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public BlockPos getOrigin(IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isValid(World worldIn, BlockPos pos) {
        return false;
    }
}
