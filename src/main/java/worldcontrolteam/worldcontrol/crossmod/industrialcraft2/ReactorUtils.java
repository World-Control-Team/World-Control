package worldcontrolteam.worldcontrol.crossmod.industrialcraft2;

import ic2.api.item.ICustomDamageItem;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import ic2.core.item.reactor.AbstractDamageableReactorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReactorUtils {

    public static IReactor getReactorAt(World world, BlockPos pos) {
        if (world == null)
            return null;
        TileEntity entity = world.getTileEntity(pos);
        if (entity instanceof IReactor)
            return (IReactor) entity;
        if (entity instanceof IReactorChamber)
            return ((IReactorChamber) entity).getReactorInstance();
        return null;
    }

    public static boolean isProducing(World world, BlockPos pos) {
        return world.isBlockIndirectlyGettingPowered(pos) > 0;
    }

    public static int getNuclearCellTimeLeft(ItemStack rStack) {
        if (rStack == null)
            return 0;
        if (rStack.getItem() instanceof AbstractDamageableReactorComponent) {
            if (rStack.getItem() instanceof ICustomDamageItem)
                return ((ICustomDamageItem) rStack.getItem()).getMaxCustomDamage(rStack) - ((ICustomDamageItem) rStack.getItem()).getCustomDamage(rStack);
            return rStack.getMaxDamage() - rStack.getItemDamage();
        }

        return 0;
    }

}
