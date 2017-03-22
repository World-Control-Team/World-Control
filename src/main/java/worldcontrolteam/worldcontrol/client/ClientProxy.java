package worldcontrolteam.worldcontrol.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import worldcontrolteam.worldcontrol.CommonProxy;
import worldcontrolteam.worldcontrol.api.card.IProviderCard;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.init.WCItems;

public class ClientProxy extends CommonProxy {

	@Override
	public void init(){
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor(){
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex){
				if(tintIndex == 1){
					InventoryItem inv = new InventoryItem(stack);
					if(inv.getStackInSlot(0) != null)
						if(inv.getStackInSlot(0).getItem() instanceof IProviderCard)
							return ((IProviderCard) inv.getStackInSlot(0).getItem()).getCardColor();
				}
				return -1;
			}
		}, WCItems.REMOTE_PANEL);
	}
}
