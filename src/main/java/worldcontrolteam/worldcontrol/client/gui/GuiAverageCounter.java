package worldcontrolteam.worldcontrol.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.inventory.container.ContainerAverageCounter;
import worldcontrolteam.worldcontrol.utils.WCUtility;


@SideOnly(Side.CLIENT)
public class GuiAverageCounter extends GuiContainer {
    private static final String TEXTURE_FILE = "worldcontrol:textures/gui/GUIEnergyCounter.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

    private String name;
    private ContainerAverageCounter container;

    public GuiAverageCounter(Container container){
        super(container);
        this.container = (ContainerAverageCounter)container;
        name = I18n.format("tile.blockAverageCounter.name");
    }

    @SuppressWarnings("unchecked")
    private void initControls(){
        buttonList.clear();
        buttonList.add(new GuiButton(1, guiLeft + 35, guiTop + 42, 30, 20, I18n.format("1")));
        buttonList.add(new GuiButton(2, guiLeft + 35 + 30, guiTop + 42, 30, 20, I18n.format("3")));
        buttonList.add(new GuiButton(3, guiLeft + 35 + 60, guiTop + 42, 30, 20, I18n.format("5")));
        buttonList.add(new GuiButton(4, guiLeft + 35 + 90, guiTop + 42, 30, 20, I18n.format("10")));
    }

    @Override
    public void initGui(){
        super.initGui();
        initControls();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        fontRendererObj.drawString(name, (xSize - fontRendererObj.getStringWidth(name)) / 2, 6, 0x404040);
        fontRendererObj.drawString(I18n.format("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
        String key = container.averageCounter.getPowerTranslateKey();
        String value = WCUtility.translateFormatted(key, container.averageCounter.getAverage());
        fontRendererObj.drawString(value, (xSize - fontRendererObj.getStringWidth(value)) / 2, 22, 0x404040);
        value = WCUtility.translateFormatted("AverageCounterPeriod", container.averageCounter.period);
        fontRendererObj.drawString(value, (xSize - fontRendererObj.getStringWidth(value)) / 2, 32, 0x404040);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(TEXTURE_LOCATION);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
    }

    @Override
    public void updateScreen(){
        super.updateScreen();
        initControls();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton){
        short event = 0;
        switch(guiButton.id){
            case 1:
                event = 1; break;
            case 2:
                event = 3; break;
            case 3:
                event = 5; break;
            case 4:
                event = 10; break;
        }
        container.averageCounter.setPeriod(event);
    }
}