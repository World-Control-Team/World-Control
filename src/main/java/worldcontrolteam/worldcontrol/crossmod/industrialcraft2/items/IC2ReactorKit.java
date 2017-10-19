package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.IC2Module;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.ReactorUtils;
import worldcontrolteam.worldcontrol.items.ItemBaseKit;

public class IC2ReactorKit extends ItemBaseKit {
    public IC2ReactorKit() {
        super("ic2_reactor_kit");
    }

    @Override
    public Item getCardType() {
        return IC2Module.reactorCard;
    }

    @Override
    public boolean canReturnCard(ItemStack stack, World world, BlockPos pos) {
        return ReactorUtils.getReactorAt(world, pos) != null;
    }

}
