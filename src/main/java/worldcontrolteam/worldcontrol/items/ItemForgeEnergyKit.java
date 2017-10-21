package worldcontrolteam.worldcontrol.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public class ItemForgeEnergyKit extends ItemBaseKit {

    public ItemForgeEnergyKit() {
        super("forge_energy_kit");
    }

    @Override
    public Item getCardType() {
        return null;
    }

    @Override
    public boolean canReturnCard(ItemStack stack, World world, BlockPos pos) {
        return WCUtility.getTileEntity(world,pos).filter(t -> t.hasCapability(CapabilityEnergy.ENERGY,null)).isPresent();
    }
}
