package worldcontrolteam.worldcontrol.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.IProviderCard;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.items.ItemBaseCard;

public class PacketServerRemotePanel implements IMessage {

	// private Map<String, Object> tag;
	public ItemStack itemstack;

	public PacketServerRemotePanel() {
		// DO NOTHING...
	}

	public PacketServerRemotePanel(ItemStack card) {
		this.itemstack = card;
		// NCLog.error(CARD);
	}

	@Override
	public void fromBytes(ByteBuf buf){
		itemstack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf){
		ByteBufUtils.writeItemStack(buf, itemstack);

	}

	public static class Handler implements IMessageHandler<PacketServerRemotePanel, IMessage> {

		@Override
		public IMessage onMessage(PacketServerRemotePanel message, MessageContext ctx){
			ItemStack stack = message.itemstack;
			// NCLog.error(stack);

			if(stack != null && stack.getItem() instanceof ItemBaseCard){
				ItemBaseCard card = (ItemBaseCard) stack.getItem();
				CardState state = card.update(ctx.getServerHandler().player.mcServer.worlds[0], stack);
				// CardState state = ((IPanelDataSource)
				// stack.getItem()).update(ContainerRemoteMonitor.panel, helper,
				// 100);
				if(state == CardState.OK){
					EntityPlayer player = ctx.getServerHandler().player;
					InventoryItem itemInv = new InventoryItem(player.getHeldItemMainhand());
					// NCLog.fatal(itemInv.getStackInSlot(0));
					if(itemInv.getStackInSlot(0) == null || !(itemInv.getStackInSlot(0).getItem() instanceof IProviderCard))
						return null;
					if(stack.hasTagCompound()){
						// NCLog.fatal(helper.getUpdateSet().entrySet());
						ItemStack stackz = itemInv.getStackInSlot(0).copy();
						if(stackz.getItem().equals(stack.getItem())){
							stackz.setTagCompound(stack.getTagCompound());
							itemInv.setInventorySlotContents(0, stackz);
							return new PacketClientRemotePanel(stack);
						}
					}
				}
			}
			return null;
		}

		/*
		 * private ItemStack processCard(ItemStack CARD, int upgradeCountRange,
		 * int slot) { if (CARD == null) { return null; } Item item =
		 * CARD.getItem(); if (item instanceof IProviderCard) { boolean
		 * needUpdate = true; if (upgradeCountRange > 7) { upgradeCountRange =
		 * 7; } int range = 100 * (int) Math.pow(2, upgradeCountRange);
		 * CardWrapperImpl cardHelper = new CardWrapperImpl(CARD, slot);
		 *
		 * if (item instanceof IRemoteSensor) { ChunkCoordinates target =
		 * cardHelper.getTarget(); if (target == null) { needUpdate = false;
		 * cardHelper.setState(CardState.INVALID_CARD); } else {
		 * cardHelper.setState(CardState.OUT_OF_RANGE); } } if (needUpdate) {
		 * CardState state = null; state = ((IProviderCard)
		 * item).update(FMLServerHandler.instance().getServer().worldServers[0],
		 * CARD);
		 *
		 * } return cardHelper; } return null; }
		 */
	}
}
