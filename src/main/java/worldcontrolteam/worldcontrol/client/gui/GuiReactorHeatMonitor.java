package worldcontrolteam.worldcontrol.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.client.gui.features.CompactButton;
import worldcontrolteam.worldcontrol.client.gui.features.GuiThermoInvertRedstone;
import worldcontrolteam.worldcontrol.container.ContainerEmpty;
import worldcontrolteam.worldcontrol.network.ChannelHandler;
import worldcontrolteam.worldcontrol.network.messages.PacketClientUpdateMonitor;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public class GuiReactorHeatMonitor extends GuiContainer{

    private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/guithermalmonitor.png";
 	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);


    private TileEntityBaseReactorHeatMonitor heatMonitor;
    String name;
    private GuiTextField textboxHeat = null;

    public GuiReactorHeatMonitor(TileEntityBaseReactorHeatMonitor tile) {
        super(new ContainerEmpty(tile));
        this.xSize = 191;
        this.ySize = 64;
        this.heatMonitor = tile;
        name = WCUtility.translate("thermalmonitorgui.name");
    }


    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(new CompactButton(0, guiLeft + 47, guiTop + 20, 22, 12, "-1"));
        buttonList.add(new CompactButton(1, guiLeft + 47, guiTop + 31, 22, 12, "-10"));
        buttonList.add(new CompactButton(2, guiLeft + 12, guiTop + 20, 36, 12, "-100"));
        buttonList.add(new CompactButton(3, guiLeft + 12, guiTop + 31, 36, 12, "-1000"));
        buttonList.add(new CompactButton(4, guiLeft + 12, guiTop + 42, 57, 12, "-10000"));

        buttonList.add(new CompactButton(5, guiLeft + 122, guiTop + 20, 22, 12, "+1"));
        buttonList.add(new CompactButton(6, guiLeft + 122, guiTop + 31, 22, 12, "+10"));
        buttonList.add(new CompactButton(7, guiLeft + 143, guiTop + 20, 36, 12, "+100"));
        buttonList.add(new CompactButton(8, guiLeft + 143, guiTop + 31, 36, 12, "+1000"));
        buttonList.add(new CompactButton(9, guiLeft + 122, guiTop + 42, 57, 12, "+10000"));

        buttonList.add(new GuiThermoInvertRedstone(10, guiLeft + 70, guiTop + 38, heatMonitor));

        textboxHeat = new GuiTextField(11, fontRenderer, 70, 21, 51, 12);
        textboxHeat.setFocused(true);
        textboxHeat.setText(String.valueOf(heatMonitor.getThreshhold()));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (textboxHeat != null)
            textboxHeat.updateCursorCounter();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString(name, (xSize - fontRenderer.getStringWidth(name)) / 2, 6, 0x404040);

        if (textboxHeat != null)
            textboxHeat.drawTextBox();
    }

    @Override
    public void onGuiClosed() {
        updateHeat(0);
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id >= 10)
            return;

        int delta = Integer.parseInt(button.displayString.replace("+", ""));
        updateHeat(delta);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(TEXTURE_LOCATION);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if(par2 == 1)// Esc button
            mc.player.closeScreen();
        else if(par1 == 13)// Enter
            updateHeat(0);
        else if(textboxHeat != null && textboxHeat.isFocused() && (Character.isDigit(par1) || par1 == 0 || par1 == 8))
            textboxHeat.textboxKeyTyped(par1, par2);
    }

    private void updateHeat(int delta) {
        if (textboxHeat != null) {
            int heat = 0;
            try {
                String value = textboxHeat.getText();
                if (!"".equals(value))
                    heat = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // do nothing
            }
            heat += delta;
            if (heatMonitor.getThreshhold() != heat) {
                ChannelHandler.network.sendToServer(new PacketClientUpdateMonitor(heat, heatMonitor.getInversion(), heatMonitor.getPos()));
            }
            textboxHeat.setText(new Integer(heat).toString());
        }
    }

}
