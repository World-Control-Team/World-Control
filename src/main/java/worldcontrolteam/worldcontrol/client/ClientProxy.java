package worldcontrolteam.worldcontrol.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import worldcontrolteam.worldcontrol.CommonProxy;
import worldcontrolteam.worldcontrol.api.card.IProviderCard;
import worldcontrolteam.worldcontrol.init.WCBlocks;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.init.WCItems;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;

import java.lang.reflect.Field;

public class ClientProxy extends CommonProxy {

	@Override
	public void preinit(FMLPreInitializationEvent event){
		AlarmAudioLoader.checkAndCreateFolders(event.getModConfigurationDirectory());
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new AlarmAudioLoader.TextureSetting());
	}

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
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor()
		{
			public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex)
			{
				if(world != null && pos != null) {
					TileEntity tile = world.getTileEntity(pos);
					if (tile instanceof TileEntityHowlerAlarm) {
						return ((TileEntityHowlerAlarm) tile).getColor();
					}
				}
				return 16777215;
			}
		}, WCBlocks.HOWLER_ALARM);

	}

	public void registerItemTextures(){

		registerBlockTextures();
	}

	public static void registerBlockTextures() {
		try{
			for(Field field : WCBlocks.class.getDeclaredFields()){
				if(field.get(null) instanceof Block){
					Item itemB = Item.REGISTRY.getObject(((Block) field.get(null)).getRegistryName());
					ModelLoader.setCustomModelResourceLocation(itemB, 0, new ModelResourceLocation(((Block) field.get(null)).getRegistryName(), "inventory"));

				}
			}
		}catch (Exception e){

		}
	}
}
