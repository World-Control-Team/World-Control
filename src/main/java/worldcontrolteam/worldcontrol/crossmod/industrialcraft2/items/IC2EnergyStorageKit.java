package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items;

import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.IC2Module;
import worldcontrolteam.worldcontrol.items.ItemBaseKit;

public class IC2EnergyStorageKit extends ItemBaseKit {

    public IC2EnergyStorageKit() {
        super("ic2_energy_storage_kit");
    }

    @Override
    public Item getCardType() {
        return IC2Module.energyCard;
    }

    @Override
    public boolean canReturnCard(ItemStack stack, World world, BlockPos pos) {
        return world.getTileEntity(pos) instanceof TileEntityElectricBlock;
    }
}
