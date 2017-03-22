package worldcontrolteam.worldcontrol.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import worldcontrolteam.worldcontrol.init.WCItems;

public class ItemFluidKit extends ItemBaseKit {

	public ItemFluidKit() {
		super("fluid_kit");
	}

	@Override
	public Item getCardType(){
		return WCItems.FLUID_CARD;
	}

	@Override
	public boolean canReturnCard(ItemStack stack, World world, BlockPos pos){
		return world.getTileEntity(pos) instanceof IFluidHandler ||
		//TODO: Remove this when forge removes the support for non-Capability types
				world.getTileEntity(pos) instanceof net.minecraftforge.fluids.IFluidHandler;
	}
}
