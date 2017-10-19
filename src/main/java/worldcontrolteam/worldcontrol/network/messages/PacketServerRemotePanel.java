package worldcontrolteam.worldcontrol.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import worldcontrolteam.worldcontrol.api.card.Card;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.init.WCCapabilities;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.utils.CardUtils;

import javax.annotation.Nonnull;

public class PacketServerRemotePanel implements IMessage {

    // private Map<String, Object> tag;
    @Nonnull
    public ItemStack itemstack;

    public PacketServerRemotePanel() {
        // DO NOTHING...
    }

    public PacketServerRemotePanel(@Nonnull ItemStack card) {
        this.itemstack = card;
        // NCLog.error(CARD);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        itemstack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, itemstack);

    }

    public static class Handler implements IMessageHandler<PacketServerRemotePanel, IMessage> {

        @Override
        public IMessage onMessage(PacketServerRemotePanel message, MessageContext ctx) {
            ItemStack stack = message.itemstack;
            // NCLog.error(stack);

            if (stack.isEmpty() && stack.hasCapability(WCCapabilities.CARD_HOLDER, null)) {
                Card card = CardUtils.getNullableCard(stack);
                if (card == null)
                    return null;
                card.update(ctx.getServerHandler().player.mcServer.getWorld(card.getDimension()));
                CardState state = card.getState();
                // CardState state = ((IPanelDataSource)
                // stack.getItem()).update(ContainerRemoteMonitor.panel, helper,
                // 100);
                if (state == CardState.OK) {
                    EntityPlayer player = ctx.getServerHandler().player;
                    InventoryItem itemInv = new InventoryItem(player.getHeldItemMainhand());
                    // NCLog.fatal(itemInv.getStackInSlot(0));
                    if (itemInv.getStackInSlot(0).isEmpty() || !(itemInv.getStackInSlot(0).hasCapability(WCCapabilities.CARD_HOLDER, null)))
                        return null;
                    if (stack.hasTagCompound()) {
                        // NCLog.fatal(helper.getUpdateSet().entrySet());
                        ItemStack stackz = itemInv.getStackInSlot(0).copy();
                        if (stackz.getItem().equals(stack.getItem())) {
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
