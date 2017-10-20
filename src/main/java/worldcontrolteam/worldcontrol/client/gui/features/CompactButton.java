package worldcontrolteam.worldcontrol.client.gui.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class CompactButton extends GuiButton {
    private static final String TEXTURE_FILE = "worldcontrol:textures/gui/guithermalmonitor.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

    public CompactButton(int par1, int par2, int par3, int par4, int par5, String par6Str) {
        super(par1, par2, par3, par4, par5, par6Str);
    }

    @Override
    public void drawButton(Minecraft minecraft, int par2, int par3, float doesStuff) {
        if (this.visible) {
            FontRenderer fontRenderer = minecraft.fontRenderer;
            minecraft.renderEngine.bindTexture(TEXTURE_LOCATION);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean var5 = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height;
            int var6 = this.getHoverState(var5);
            this.drawTexturedModalRect(this.x, this.y, 0, 64 + var6 * 12, this.width / 2 + width % 2, this.height);
            this.drawTexturedModalRect(this.x + this.width / 2 + width % 2, this.y, 200 - this.width / 2, 64 + var6 * 12, this.width / 2, this.height);
            this.mouseDragged(minecraft, par2, par3);
            fontRenderer.drawString(displayString, x + (width - fontRenderer.getStringWidth(displayString)) / 2, y + 2, 0x303030);
        }
    }

}
