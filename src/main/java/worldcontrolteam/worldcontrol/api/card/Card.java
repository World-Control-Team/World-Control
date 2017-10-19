package worldcontrolteam.worldcontrol.api.card;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class Card implements INBTSerializable<NBTTagCompound> {
    @Nonnull
    protected CardState state = CardState.INVALID_CARD;
    @Nonnull
    protected ItemStack stack;
    @Nonnull
    protected CardManager manager;

    public Card(@Nonnull ItemStack stack, @Nonnull CardManager manager) {
        this.stack = stack;
        this.manager = manager;
    }

    public abstract List<StringWrapper> getData(List<StringWrapper> list, int displaySettings, boolean showLabels, int[] invisibleLines);

    public abstract void update(World world);

    public void addTooltip(List<String> tooltip, World world, ITooltipFlag flag) {
        tooltip.add(state.name());
    }

    @SideOnly(Side.CLIENT)
    public abstract int getCardColor();

    public int getDimension() {
        return 0;
    }

    @Nonnull
    public CardState getState() {
        return state;
    }

    public void setState(@Nonnull CardState state) {
        this.state = state;
    }
}