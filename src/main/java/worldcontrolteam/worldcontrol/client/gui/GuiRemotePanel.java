package worldcontrolteam.worldcontrol.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.api.card.ICard;
import worldcontrolteam.worldcontrol.api.card.predefs.StringWrapper;
import worldcontrolteam.worldcontrol.api.screen.IScreenElement;
import worldcontrolteam.worldcontrol.inventory.InventoryItem;
import worldcontrolteam.worldcontrol.inventory.container.ContainerRemotePanel;
import worldcontrolteam.worldcontrol.network.ChannelHandler;
import worldcontrolteam.worldcontrol.network.messages.PacketServerRemotePanel;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.LinkedList;
import java.util.List;

public class GuiRemotePanel extends GuiContainer {

    private InventoryItem inv;
    private ItemStack lastCard = null;
    private IScreenElement isce;
    private EntityPlayer e;

    public GuiRemotePanel(InventoryPlayer inv, ItemStack stack, InventoryItem inventoryItem, EntityPlayer player) {
        super(new ContainerRemotePanel(inv, stack, inventoryItem));
        this.inv = inventoryItem;
        this.e = player;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.xSize += 50;
        this.mc.player.openContainer = this.inventorySlots;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(new ResourceLocation("worldcontrol", "textures/gui/gui_remote_panel.png"));
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, 204, ySize);
    }

    private void updateScreenElement(ItemStack itemInInventory) {
        if (itemInInventory != lastCard) {
            lastCard = itemInInventory;
            isce = null;
            if (itemInInventory.getItem() instanceof ICard) {
                isce = ((ICard) itemInInventory.getItem()).getRenderer(itemInInventory);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        List<StringWrapper> joinedData = new LinkedList<StringWrapper>();
        boolean anyCardFound = true;
        InventoryItem itemInv = new InventoryItem(e.getHeldItemMainhand());

        if (!inv.getStackInSlot(0).isEmpty() && !itemInv.getStackInSlot(0).isEmpty() && itemInv.getStackInSlot(0).getItem() instanceof ICard) {
            ICard card = (ICard) inv.getStackInSlot(0).getItem();
            // CardWrapperImpl helper = new
            // CardWrapperImpl(itemInv.getStackInSlot(0), 0);
            updateScreenElement(itemInv.getStackInSlot(0));
            ChannelHandler.network.sendToServer(new PacketServerRemotePanel(inv.getStackInSlot(0)));
            if (isce != null) {
                //CardState cs = card.update(e.world, inv.getStackInSlot(0));
                isce.onCardUpdate(e.world, itemInv.getStackInSlot(0));
                drawCardStuff(true);
            }
            else {
                // todo: add getRemoteCustomMSG back here
            }
        }
    }

    private List<StringWrapper> getRemoteCustomMSG() {
        StringWrapper line = new StringWrapper();
        List<StringWrapper> result = new LinkedList<StringWrapper>();
        line.textCenter = I18n.format("nc.msg.notValid");
        result.add(line);
        line = new StringWrapper();
        line.textCenter = I18n.format("nc.msg.notValid2");
        result.add(line);
        line = new StringWrapper();
        line.textCenter = "";
        result.add(line);
        line = new StringWrapper();
        line.textCenter = I18n.format("nc.msg.notValid3");
        result.add(line);
        return result;
    }

    private void drawCardStuff(Boolean anyCardFound) {
        if (!anyCardFound) {
            WCUtility.error("This should never happen. If you see this report immediately to WC repo. Include GuiRemoteMonitorError-123 in the report!");
            return;
        }

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        GlStateManager.pushMatrix();
        GlStateManager.translate(9, 0, 0);

        isce.draw(158, ySize); // technically should work... i hope

        GlStateManager.popMatrix();

        // todo: fixme plz
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (this.e.getHeldItemMainhand().isEmpty())
            this.mc.player.closeScreen();
    }
}
