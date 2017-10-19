package worldcontrolteam.worldcontrol.crossmod.extremereactors.reactormonitor;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.crossmod.extremereactors.ExtremeReactorsModule;
import worldcontrolteam.worldcontrol.items.ItemBaseKit;

/**
 * Created by dmf444 on 10/16/2017. Code originally written for World-Control.
 */
public class ExtremeReactorsKit extends ItemBaseKit {

    public ExtremeReactorsKit() {
        super("er_kit");
    }

    @Override
    public Item getCardType() {
        return ExtremeReactorsModule.REACTOR_CARD;
    }

    @Override
    public boolean canReturnCard(ItemStack stack, World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == ExtremeReactorsModule.REACTOR_MONITOR) {
            TileReactorMonitor monitor = (TileReactorMonitor) world.getTileEntity(pos);
            monitor.startFetching();
            return true;
        }
        return false;
    }
}
