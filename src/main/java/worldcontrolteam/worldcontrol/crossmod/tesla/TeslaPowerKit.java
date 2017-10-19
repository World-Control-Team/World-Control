package worldcontrolteam.worldcontrol.crossmod.tesla;

import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.items.ItemBaseKit;

public class TeslaPowerKit extends ItemBaseKit {

    public TeslaPowerKit() {
        super("tesla_power_kit");
    }

    @Override
    public Item getCardType() {
        return TeslaModule.TESLA_CARD;
    }

    @Override
    public boolean canReturnCard(ItemStack stack, World world, BlockPos pos) {
        if (world.getTileEntity(pos).hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN))
            return true;
        return false;
    }
}
