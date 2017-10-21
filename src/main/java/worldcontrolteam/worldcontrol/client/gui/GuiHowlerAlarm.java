package worldcontrolteam.worldcontrol.client.gui;


import com.google.common.collect.Lists;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.client.gui.features.HowlerAlarmListBox;
import worldcontrolteam.worldcontrol.client.gui.features.HowlerAlarmSlider;
import worldcontrolteam.worldcontrol.container.ContainerEmpty;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;
import worldcontrolteam.worldcontrol.utils.WCConfig;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiHowlerAlarm extends GuiContainer {
    private static final String TEXTURE_FILE = "worldcontrol:textures/gui/gui_howler_alarm.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

    private TileEntityHowlerAlarm alarm;
    private HowlerAlarmSlider slider;
    private HowlerAlarmListBox listBox;
    private String name;

    public GuiHowlerAlarm(TileEntityHowlerAlarm alarm) {
        super(new ContainerEmpty(alarm));
        this.alarm = alarm;
        xSize = 131;
        ySize = 136;
        name = I18n.format("tile.HowlerAlarm.name");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        guiLeft = (this.width - xSize) / 2;
        guiTop = (this.height - ySize) / 2;
        buttonList.clear();
        slider = new HowlerAlarmSlider(3, guiLeft + 12, guiTop + 33, "msg.worldcontrol.HowlerAlarmSoundRange", alarm);
        String[] items = WCConfig.howlerAlarmSounds;


        listBox = new HowlerAlarmListBox(4, guiLeft + 13, guiTop + 63, 105, 65, Lists.newArrayList(items), alarm);
        buttonList.add(slider);
        buttonList.add(listBox);

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString(name, (xSize - fontRenderer.getStringWidth(name)) / 2, 6, 0x404040);
        fontRenderer.drawString(I18n.format("msg.worldcontrol.HowlerAlarmSound"), 12, 53, 0x404040);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int which) {
        super.mouseReleased(mouseX, mouseY, which);
        if ((which == 0 || which == 1) && (slider.dragging || listBox.dragging)) {
            slider.mouseReleased(mouseX, mouseY);
            listBox.mouseReleased(mouseX, mouseY);
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
