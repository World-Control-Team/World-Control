package worldcontrolteam.worldcontrol.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.client.gui.features.CompactButton;
import worldcontrolteam.worldcontrol.client.gui.features.GuiThermoInvertRedstone;
import worldcontrolteam.worldcontrol.container.ContainerIC2RemoteReactorMonitor;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks.TileEntityIC2RemoteReactorMonitor;
import worldcontrolteam.worldcontrol.network.ChannelHandler;
import worldcontrolteam.worldcontrol.network.messages.PacketClientUpdateMonitor;
import worldcontrolteam.worldcontrol.utils.WCUtility;

@SideOnly(Side.CLIENT)
public class GuiIC2RemoteReactorHeatMonitor extends GuiContainer{
    private static final String TEXTURE_FILE = "worldcontrol:textures/gui/gui_remote_thermo.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

    private TileEntityIC2RemoteReactorMonitor remoteReactorMonitor;
    private final String name = WCUtility.translate("blockremoteheatmonitor.name");
    private GuiTextField textboxHeat = null;

    public GuiIC2RemoteReactorHeatMonitor(EntityPlayer player, TileEntityIC2RemoteReactorMonitor tile) {
        super(new ContainerIC2RemoteReactorMonitor(player, tile));
        this.remoteReactorMonitor = tile;
        xSize = 214;
        ySize = 166;
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(TEXTURE_LOCATION);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);

        // Charge level progress bar
        int chargeWidth = (int) (76F * remoteReactorMonitor.getEnergy() / remoteReactorMonitor.getMaxStorage());
        if (chargeWidth > 76)
            chargeWidth = 76;

        if (chargeWidth > 0)
            drawTexturedModalRect(left + 55 - 14, top + 54, 8, 166, chargeWidth, 14);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(new CompactButton(0, guiLeft + 47, guiTop - 5 + 20, 22, 12, "-1"));
        buttonList.add(new CompactButton(1, guiLeft + 47, guiTop - 5 + 31, 22, 12, "-10"));
        buttonList.add(new CompactButton(2, guiLeft + 12, guiTop - 5 + 20, 36, 12, "-100"));
        buttonList.add(new CompactButton(3, guiLeft + 12, guiTop - 5 + 31, 36, 12, "-1000"));
        buttonList.add(new CompactButton(4, guiLeft + 12, guiTop - 5 + 42, 57, 12, "-10000"));

        buttonList.add(new CompactButton(5, guiLeft + 122, guiTop - 5 + 20, 22, 12, "+1"));
        buttonList.add(new CompactButton(6, guiLeft + 122, guiTop - 5 + 31, 22, 12, "+10"));
        buttonList.add(new CompactButton(7, guiLeft + 143, guiTop - 5 + 20, 36, 12, "+100"));
        buttonList.add(new CompactButton(8, guiLeft + 143, guiTop - 5 + 31, 36, 12, "+1000"));
        buttonList.add(new CompactButton(9, guiLeft + 122, guiTop - 5 + 42, 57, 12, "+10000"));

        buttonList.add(new GuiThermoInvertRedstone(10, guiLeft + 70, guiTop + 33, remoteReactorMonitor));

        textboxHeat = new GuiTextField(11, fontRenderer, 70, 16, 51, 12);
        textboxHeat.setFocused(true);
        textboxHeat.setText(String.valueOf(remoteReactorMonitor.getThreshhold()));
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
        fontRenderer.drawString(I18n.format("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        if(textboxHeat != null)
            textboxHeat.drawTextBox();
    }

    @Override
    public void onGuiClosed(){
        updateHeat(0);
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton button){
        if (button.id >= 10)
            return;

        int delta = Integer.parseInt(button.displayString.replace("+", ""));
        updateHeat(delta);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if(par2 == 1)//Esc
            mc.player.closeScreen();
        else if(par1 == 13)//Enter
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
            if(heat > 1000000)
                heat = 1000000;
            if(heat < 0)
                heat = 0;

            if (remoteReactorMonitor.getThreshhold() != heat) {
                remoteReactorMonitor.setThreshhold(heat);
                ChannelHandler.network.sendToServer(new PacketClientUpdateMonitor(heat, remoteReactorMonitor.getInversion(), remoteReactorMonitor.getPos()));
            }
            textboxHeat.setText(new Integer(heat).toString());
        }
    }


}
