package worldcontrolteam.worldcontrol.api.card;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IProviderCard {

    CardState update(World world, ItemStack card);

    List<StringWrapper> getStringData(List<StringWrapper> list, int displaySettings, ItemStack card, boolean showLabels);

    List<String> getGuiData();

    @SideOnly(Side.CLIENT)
    int getCardColor();
}
