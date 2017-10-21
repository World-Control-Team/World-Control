package worldcontrolteam.worldcontrol.client;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class ClientUtil {

    public static void registerWithMapper(Block block) {
        if (block != null && block.getRegistryName() != null) {
            final String resourcePath = block.getRegistryName().toString();
            setCustomStateMapper(block, state -> new ModelResourceLocation(resourcePath, getPropertyString(state.getProperties())));
            NonNullList<ItemStack> subBlocks = NonNullList.create();
            block.getSubBlocks(null, subBlocks);
            for (ItemStack stack : subBlocks) {
                IBlockState state = block.getStateFromMeta(1/*stack.getMetadata()*/); //TODO: Find a fix for this, not hard coded
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), stack.getMetadata(), new ModelResourceLocation(resourcePath, getPropertyString(state.getProperties())));
            }
        }
    }

    public static void setCustomStateMapper(Block block, StateMapper mapper) {
        ModelLoader.setCustomStateMapper(block, mapper.getMapper());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> comparable) {
        return property.getName((T) comparable);
    }

    public static String getPropertyString(Map<IProperty<?>, Comparable<?>> values, String... extrasArgs) {
        final StringBuilder stringbuilder = new StringBuilder();
        for (final Map.Entry<IProperty<?>, Comparable<?>> entry : values.entrySet()) {
            if (stringbuilder.length() != 0)
                stringbuilder.append(",");
            final IProperty<?> iProperty = entry.getKey();
            stringbuilder.append(iProperty.getName());
            stringbuilder.append("=");
            stringbuilder.append(getPropertyName(iProperty, entry.getValue()));
        }
        if (stringbuilder.length() == 0)
            stringbuilder.append("inventory");
        for (final String args : extrasArgs) {
            if (stringbuilder.length() != 0)
                stringbuilder.append(",");
            stringbuilder.append(args);
        }
        return stringbuilder.toString();
    }

    public interface StateMapper {
        ModelResourceLocation getModelResourceLocation(IBlockState state);

        default IStateMapper getMapper() {
            return new DefaultStateMapper() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return StateMapper.this.getModelResourceLocation(state);
                }
            };
        }
    }

}
