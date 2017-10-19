package worldcontrolteam.worldcontrol.api.card;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class CardManager extends IForgeRegistryEntry.Impl<CardManager> {

    public abstract boolean isValidBlock(World world, BlockPos pos);

    public boolean hasKit() {
        return true;
    }

    public abstract Card createCard(ItemStack stack, BlockPos pos, EnumFacing facing);
}