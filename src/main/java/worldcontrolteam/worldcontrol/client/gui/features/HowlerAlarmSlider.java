package worldcontrolteam.worldcontrol.client.gui.features;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.network.ChannelHandler;
import worldcontrolteam.worldcontrol.network.messages.PacketUpdateHowlerAlarm;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;
import worldcontrolteam.worldcontrol.utils.WCConfig;


@SideOnly(Side.CLIENT)
public class HowlerAlarmSlider extends GuiButton {
    private static final String TEXTURE_FILE = "worldcontrol:textures/gui/gui_howler_alarm.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

    public float sliderValue;
    public boolean dragging;
    private int minValue = 0;
    private int maxValue = 256;
    private int step = 8;
    private String label;
    private TileEntityHowlerAlarm alarm;

    public HowlerAlarmSlider(int id, int x, int y, String label, TileEntityHowlerAlarm alarm) {
        super(id, x, y, 107, 16, label);
        this.alarm = alarm;
        dragging = false;
        this.label = label;
        if (alarm.getWorld().isRemote)
            maxValue = WCConfig.maxAlarmRange;
        int currentRange = alarm.getRange();
        if (alarm.getWorld().isRemote && currentRange > maxValue)
            currentRange = maxValue;
        sliderValue = ((float) currentRange - minValue) / (maxValue - minValue);
        displayString = I18n.format(label, getNormalizedValue());
    }

    private int getNormalizedValue() {
        return (minValue + (int) Math.floor((maxValue - minValue) * sliderValue)) / step * step;
    }

    private void setSliderPos(int targetX) {
        sliderValue = (float) (targetX - (x + 4)) / (float) (width - 8);

        if (sliderValue < 0.0F)
            sliderValue = 0.0F;

        if (sliderValue > 1.0F)
            sliderValue = 1.0F;

        int newValue = getNormalizedValue();
        if (alarm.getRange() != newValue) {
            alarm.setRange(newValue);
            ChannelHandler.network.sendToServer(new PacketUpdateHowlerAlarm(newValue, "default", alarm.getPos()));
        }
        displayString = I18n.format(label, newValue);
    }

    @Override
    public void drawButton(Minecraft minecraft, int targetX, int targetY, float f) {
        if (visible) {
            minecraft.renderEngine.bindTexture(TEXTURE_LOCATION);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (dragging)
                setSliderPos(targetX);

            drawTexturedModalRect(x + (int) (sliderValue * (width - 8)), y, 131, 0, 8, 16);
            minecraft.fontRenderer.drawString(displayString, x, y - 12, 0x404040);
        }
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int targetX, int j) {
        if (super.mousePressed(minecraft, targetX, j)) {
            setSliderPos(targetX);
            dragging = true;
            return true;
        }else
            return false;
    }

    @Override
    public void mouseReleased(int i, int j) {
        super.mouseReleased(i, j);
        dragging = false;
    }
}
