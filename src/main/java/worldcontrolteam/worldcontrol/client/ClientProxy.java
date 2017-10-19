package worldcontrolteam.worldcontrol.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import worldcontrolteam.worldcontrol.CommonProxy;
import worldcontrolteam.worldcontrol.blocks.BlockBasicTileProvider;
import worldcontrolteam.worldcontrol.init.WCBlocks;
import worldcontrolteam.worldcontrol.init.WCItems;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.items.WCBaseItem;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;
import worldcontrolteam.worldcontrol.utils.CardUtils;

public class ClientProxy extends CommonProxy {

	@Override
	public void preinit(FMLPreInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(this);
		AlarmAudioLoader.checkAndCreateFolders(event.getModConfigurationDirectory());
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new AlarmAudioLoader.TextureSetting());
	}

	@Override
	public void init(){
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            if(tintIndex == 1){
                InventoryItem inv = new InventoryItem(stack);
                if(inv.getStackInSlot(0) != null)
                    if(inv.getStackInSlot(0).getItem() instanceof IProviderCard)
                        return ((IProviderCard) inv.getStackInSlot(0).getItem()).getCardColor();
            }
            return -1;
        }, WCItems.REMOTE_PANEL);
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) -> {
            if(world != null && pos != null) {
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TileEntityHowlerAlarm) {
                    return ((TileEntityHowlerAlarm) tile).getColor();
                }
            }
            return 16777215;
        }, WCBlocks.HOWLER_ALARM);

    @Override
    public void init() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            if (tintIndex == 1) {
                InventoryItem inv = new InventoryItem(stack);
                if (!inv.getStackInSlot(0).isEmpty())
                    if (inv.getStackInSlot(0).hasCapability(WCCapabilities.CARD_HOLDER, null))
                        return CardUtils.getCard(inv.getStackInSlot(0)).map(c -> c.getCardColor()).orElse(-1);
            }
            return -1;
        }, WCItems.REMOTE_PANEL);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
                if (world != null && pos != null) {
                    TileEntity tile = world.getTileEntity(pos);
                    if (tile instanceof TileEntityHowlerAlarm) {
                        return ((TileEntityHowlerAlarm) tile).getColor();
                    }
                }
                return 16777215;
            }
        }, WCBlocks.HOWLER_ALARM);

	@SubscribeEvent
	public void registerTextures(ModelRegistryEvent event){
		registerItemTextures();
		registerBlockTextures();
	}

	private void registerItemTextures() {
		for(Item item : WCBaseItem.wcItems){
			String name = item.getUnlocalizedName().substring(18); //"item.worldcontrol.".length()
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("worldcontrol:"+ name, "inventory"));
		}
	}

	public void registerBlockTextures() {
		for(Block field : BlockBasicTileProvider.wcBlocks){
			Item itemB = Item.REGISTRY.getObject(field.getRegistryName());
			ModelLoader.setCustomModelResourceLocation(itemB, 0, new ModelResourceLocation(field.getRegistryName(), "inventory"));
		}
	}
}