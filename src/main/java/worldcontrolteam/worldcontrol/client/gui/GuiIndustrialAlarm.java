package worldcontrolteam.worldcontrol.client.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.client.gui.features.HowlerAlarmSlider;
import worldcontrolteam.worldcontrol.container.ContainerEmpty;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiIndustrialAlarm extends GuiContainer {

    private static final String TEXTURE_FILE = "worldcontrol:textures/gui/gui_industrial_alarm.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

    private TileEntityHowlerAlarm alarm;
    private HowlerAlarmSlider slider;
    private String name;

    public GuiIndustrialAlarm(TileEntityHowlerAlarm alarm) {
        super(new ContainerEmpty(alarm));
        xSize = 131;
        ySize = 64;
        this.alarm = alarm;
        name = I18n.format("tile.IndustrialAlarm.name");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        guiLeft = (this.width - xSize) / 2;
        guiTop = (this.height - ySize) / 2;
        buttonList.clear();
        slider = new HowlerAlarmSlider(3, guiLeft + 12, guiTop + 33,"msg.worldcontrol.HowlerAlarmSoundRange", alarm);
        buttonList.add(slider);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(name, (xSize - fontRendererObj.getStringWidth(name)) / 2, 6, 0x404040);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int which) {
        super.mouseReleased(mouseX, mouseY, which);
        if ((which == 0 || which == 1) && slider.dragging) {
            slider.mouseReleased(mouseX, mouseY);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(TEXTURE_LOCATION);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
    }
}

