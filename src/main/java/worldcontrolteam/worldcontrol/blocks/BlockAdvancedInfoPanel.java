package worldcontrolteam.worldcontrol.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import worldcontrolteam.worldcontrol.screen.IScreenContainer;

import java.util.StringJoiner;

/**
 * File created by mincrmatt12 on 6/19/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public class BlockAdvancedInfoPanel extends BlockBasicRotate implements IScreenContainer
{
    public static class State {
        public float depth1 = 1;
        public float depth2 = 1;
        public float depth3 = 1;
        public float depth4 = 1;

        public boolean up = false, down = false, left = false, right = false;
        public boolean power = true;
        public int color = 3;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            State state = (State) o;

            return new EqualsBuilder()
                    .append(depth1, state.depth1)
                    .append(depth2, state.depth2)
                    .append(depth3, state.depth3)
                    .append(depth4, state.depth4)
                    .append(up, state.up)
                    .append(down, state.down)
                    .append(left, state.left)
                    .append(right, state.right)
                    .append(power, state.power)
                    .append(color, state.color)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(depth1)
                    .append(depth2)
                    .append(depth3)
                    .append(depth4)
                    .append(up)
                    .append(down)
                    .append(left)
                    .append(right)
                    .append(power)
                    .append(color)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", "AdvancedInfoState[", "]")
                    .add("depth1=" + depth1)
                    .add("depth2=" + depth2)
                    .add("depth3=" + depth3)
                    .add("depth4=" + depth4)
                    .add("up=" + up)
                    .add("down=" + down)
                    .add("left=" + left)
                    .add("right=" + right)
                    .add("power=" + power)
                    .add("color=" + color)
                    .toString();
        }
    }

    public static IUnlistedProperty<State> STATE = new IUnlistedProperty<State>() {
        @Override
        public String getName() {
            return "state";
        }

        @Override
        public boolean isValid(State state) {
            return true;
        }

        @Override
        public Class<State> getType() {
            return State.class;
        }

        @Override
        public String valueToString(State state) {
            return state.toString();
        }
    };

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

    @Override
    public boolean isAdvanced() {
        return true;
    }
}
