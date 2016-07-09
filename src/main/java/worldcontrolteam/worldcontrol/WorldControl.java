package worldcontrolteam.worldcontrol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import worldcontrolteam.worldcontrol.api.card.IProviderCard;
import worldcontrolteam.worldcontrol.api.core.WorldControlAPI;
import worldcontrolteam.worldcontrol.api.thermometer.IHeatSeeker;
import worldcontrolteam.worldcontrol.network.GuiHandler;
import worldcontrolteam.worldcontrol.crossmod.Modules;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.items.ItemThermometer;
import worldcontrolteam.worldcontrol.items.WCItems;
import worldcontrolteam.worldcontrol.network.ChannelHandler;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.ArrayList;

@Mod(modid = WorldControl.MODID, version = "@VERSION@")
public class WorldControl {

	@Mod.Instance(value = "worldcontrol")
	public static WorldControl instance;

	public static final String MODID = "worldcontrol";

	public static WCCreativeTab TAB = new WCCreativeTab();

	public static Side side; // As in client vs server

	protected static ArrayList<IHeatSeeker> heatTypez = new ArrayList<IHeatSeeker>();

	public static Modules modules = new Modules();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		WCUtility.info("We are in pre-init!");
		side = event.getSide();
		WorldControlAPI.init(new WCapiImpl());

		WCItems.registerItems();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		ChannelHandler.init();
		modules.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		WCUtility.info("We are in init!");

		modules.init();

		((ItemThermometer) WCItems.thermometer).addInHeatTypes(heatTypez);

		if(event.getSide() == Side.CLIENT){
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor(){
				@Override
				public int getColorFromItemstack(ItemStack stack, int tintIndex) {
					if(tintIndex == 1){
						InventoryItem inv = new InventoryItem(stack);
						if(inv.getStackInSlot(0) != null){
							if(inv.getStackInSlot(0).getItem() instanceof IProviderCard){
								return ((IProviderCard) inv.getStackInSlot(0).getItem()).getCardColor();
							}
						}
					}
					return -1;
				}
			}, WCItems.remotePanel);
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		WCUtility.info("We are in post-init!");

		modules.postInit();
	}
}
