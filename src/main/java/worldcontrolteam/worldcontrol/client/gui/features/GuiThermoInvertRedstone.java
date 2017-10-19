package worldcontrolteam.worldcontrol.client.gui.features;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.network.ChannelHandler;
import worldcontrolteam.worldcontrol.network.messages.PacketClientUpdateMonitor;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;

@SideOnly(Side.CLIENT)
public class GuiThermoInvertRedstone extends GuiButton {

    private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/guithermalmonitor.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

    TileEntityBaseReactorHeatMonitor thermo;
    private boolean checked;

    public GuiThermoInvertRedstone(int id, int x, int y, TileEntityBaseReactorHeatMonitor thermo){
        super(id, x, y, 0, 0, "");
        height = 15;
        width = 51;
        this.thermo = thermo;
        checked = thermo.getInversion();
    }

    @Override
    public void drawButton(Minecraft minecraft, int par2, int par3, float iDoThings) {
        if(this.visible){
            minecraft.renderEngine.bindTexture(TEXTURE_LOCATION);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int delta = checked ? 15 : 0;
            drawTexturedModalRect(x, y + 1, 199, delta, 51, 15);
        }
    }

    @Override
    public int getHoverState(boolean flag) {
        return 0;
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int i, int j) {
        if(super.mousePressed(minecraft, i, j)){
            checked = !checked;
            ChannelHandler.network.sendToServer(new PacketClientUpdateMonitor(thermo.getThreshhold(), checked, thermo.getPos()));
            return true;
        }else
            return false;
    }
}
