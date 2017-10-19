package worldcontrolteam.worldcontrol.api.card;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.utils.NBTUtils;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class WorldCard extends Card {
    protected BlockPos pos;

    public WorldCard(@Nonnull ItemStack stack, @Nonnull CardManager manager, @Nonnull BlockPos pos) {
        super(stack, manager);
        this.pos = pos;
    }

    public WorldCard(@Nonnull ItemStack stack, @Nonnull CardManager manager) {
        super(stack, manager);
    }

    @Override
    public void update(World world) {
        if (!this.manager.isValidBlock(world, pos)) {
            state = CardState.INVALID_CARD;
            return;
        }

        updateData(world, pos);
    }

    @Override
    public void addTooltip(List<String> tooltip, World world, ITooltipFlag flag) {
        if (pos == null) {
            tooltip.add(WCUtility.translate("card.no_cords"));
        } else {
            tooltip.add(WCUtility.translateFormatted("card.cords", pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    public abstract void updateData(World world, BlockPos pos);

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTUtils.writeBlockPos(compound, pos);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        pos = NBTUtils.getBlockPos(compound);
    }
}