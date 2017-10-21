package worldcontrolteam.worldcontrol.card;

import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.card.Card;
import worldcontrolteam.worldcontrol.api.card.CardManager;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EUStorageManager extends CardManager {
    public EUStorageManager() {
        setRegistryName("eu_storage");
    }

    @Override
    public boolean isValidBlock(World world, BlockPos pos) {
        return WCUtility.getTileEntity(world, pos).filter(t -> t instanceof IEnergyStorage).isPresent();
    }

    @Override
    public Card createCard(@Nonnull ItemStack stack, @Nullable BlockPos pos, @Nullable EnumFacing facing) {
        return new EUStorageCard(stack, this, pos);
    }
}