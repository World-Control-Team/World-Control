package worldcontrolteam.worldcontrol.client.gui.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;

public class GuiInfoPanelShowLabels extends GuiButton {
  private static final String TEXTURE_FILE = "worldcontrol:textures/gui/gui_info_panel.png";
  private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

  private boolean checked;

  public GuiInfoPanelShowLabels(int id, int x, int y, TileEntityInfoPanel panel) {
    super(id, x, y, 0, 0, "");
    height = 9;
    width = 18;
    checked = panel.shouldShowLabels();
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    if(this.visible){
      Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_LOCATION);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int delta = checked ? 12 : 21;
      drawTexturedModalRect(this.x, this.y + 1, 176, delta, 18, 9);
    }
  }

  @Override
  public int getHoverState(boolean flag) {
    return 0;
  }

  @Override
  public boolean mousePressed(Minecraft minecraft, int i, int j) {
    if (super.mousePressed(minecraft, i, j)) {
      checked = !checked;
      return true;
    } else {
      return false;
    }
  }

}
