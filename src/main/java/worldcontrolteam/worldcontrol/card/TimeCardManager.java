package worldcontrolteam.worldcontrol.card;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.card.Card;
import worldcontrolteam.worldcontrol.api.card.CardManager;

public class TimeCardManager extends CardManager {
    @Override
    public boolean isValidBlock(World world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean hasKit() {
        return false;
    }

    @Override
    public Card createCard(ItemStack stack, BlockPos pos, EnumFacing facing) {
        return new TimeCard(stack, this);
    }
}