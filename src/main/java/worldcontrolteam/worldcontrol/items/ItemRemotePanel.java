package worldcontrolteam.worldcontrol.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.utils.GuiLib;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;

import javax.annotation.Nullable;

public class ItemRemotePanel extends WCBaseItem {

	public ItemRemotePanel() {
		super("remotePanel");

		this.addPropertyOverride(new ResourceLocation("noCard"), new IItemPropertyGetter(){
			@Override
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn){
				InventoryItem inv = new InventoryItem(stack);
				if(inv.getStackInSlot(0) == null){
					return 1;
				}
				return 0;
			}
		});
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand){
		if(!player.isSneaking()){
			player.openGui(WorldControl.instance, GuiLib.REMOTE_PANEL, world, 0, 0, 0);
			return new ActionResult(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack){
		return 1;
	}

}
