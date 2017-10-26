package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks;

import ic2.api.reactor.IReactor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.ReactorUtils;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items.IC2ReactorCard;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;
import worldcontrolteam.worldcontrol.utils.NBTUtils;

/**
 * Created by dmf444 on 10/25/2017. Code originally written for World-Control.
 */
public class TileEntityIC2RemoteReactorMonitor extends TileEntityBaseReactorHeatMonitor {

    private NonNullList<ItemStack> itemStack = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
    private static final double MIN_RANGE = 20;
    //TODO: Cap upgrade stacksize to 16 blocks
    private static final double ADDITIONAL_RANGE = 2.75f;

    public TileEntityIC2RemoteReactorMonitor(){

    }


    @Override
    public int getCurrentHeat() {
        BlockPos ref = this.getReferenceBlock();
        if(ref != null) {
            IReactor reactor = ReactorUtils.getReactorAt(world, ref);
            if (reactor != null) {
                return reactor.getHeat();
            }
        }
        return -1;
    }

    @Override
    public boolean isConnectionValid() {
        if(itemStack.get(0) != ItemStack.EMPTY){
            if(itemStack.get(0).getItem() instanceof IC2ReactorCard){
                ItemStack card = itemStack.get(0);
                if (card.hasTagCompound()) {
                    BlockPos pos = NBTUtils.getBlockPos(card.getTagCompound());
                    double distance = this.getPos().getDistance(pos.getX(), pos.getY(), pos.getZ());
                    if(distance <= ((itemStack.get(1).getCount() - 1) * ADDITIONAL_RANGE) + MIN_RANGE) {
                        this.setReferenceBlock(pos);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
