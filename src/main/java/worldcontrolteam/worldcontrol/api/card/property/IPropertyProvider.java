package worldcontrolteam.worldcontrol.api.card.property;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IPropertyProvider {
    List<Property> getProperties();

    Properties getPropertyData(ItemStack itemStack);
}
