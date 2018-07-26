package worldcontrolteam.worldcontrol.screen;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * File created by mincrmatt12 on 6/15/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public interface IScreenContainer {
    EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos);

    BlockPos getOrigin(IBlockAccess worldIn, BlockPos pos);

    default void setOrigin(World worldIn, BlockPos posBlock, BlockPos posOrigin) {

    }

    boolean isValid(World worldIn, BlockPos pos);
    default boolean isAdvanced() {return false;}
}
