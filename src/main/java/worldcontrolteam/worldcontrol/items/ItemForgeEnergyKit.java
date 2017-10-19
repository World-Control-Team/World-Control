package worldcontrolteam.worldcontrol.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import worldcontrolteam.worldcontrol.init.WCItems;

public class ItemForgeEnergyKit extends ItemBaseKit {

    public ItemForgeEnergyKit() {
        super("forge_energy_kit");
    }

    @Override
    public Item getCardType() {
        return WCItems.FORGE_ENERGY_CARD;
    }

    @Override
    public boolean canReturnCard(ItemStack stack, World world, BlockPos pos) {
        return world.getTileEntity(pos) instanceof IEnergyStorage;
    }
}
