package worldcontrolteam.worldcontrol.crossmod.industrialcraft2;

import ic2.api.item.ICustomDamageItem;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import ic2.api.reactor.IReactorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public class ReactorUtils {

    public static IReactor getReactorAt(World world, BlockPos pos) {
        if (world == null)
            return null;
        TileEntity tile = WCUtility.getTileEntity(world, pos).orElse(null);
        if (tile == null){
            return null;
        } else if (tile instanceof IReactor) {
            return (IReactor) tile;
        }else {
            return tile instanceof IReactorChamber ? ((IReactorChamber) tile).getReactorInstance() : null;
        }

    }

    public static boolean isProducing(World world, BlockPos pos) {
        return world.isBlockIndirectlyGettingPowered(pos) > 0;
    }

    public static int getNuclearCellTimeLeft(ItemStack rStack) {
        if (rStack.isEmpty()){
            return 0;
        } else if (rStack.getItem() instanceof IReactorComponent){
            return rStack.getItem() instanceof ICustomDamageItem ? ((ICustomDamageItem) rStack.getItem()).getMaxCustomDamage(rStack) - ((ICustomDamageItem) rStack.getItem()).getCustomDamage(rStack) : rStack.getMaxDamage() - rStack.getItemDamage();
        } else{
            return 0;
        }

    }

}