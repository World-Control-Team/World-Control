package worldcontrolteam.worldcontrol.utils;

import net.minecraft.block.properties.IProperty;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.util.Map;

public class Platform {

    private static boolean dev = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    /**
     * Check if the code is running on the server instance
     *
     * @return True if running on the server
     */
    public static boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }

    /**
     * Check if the code is running on the client instance
     *
     * @return True if running on the client
     */
    public static boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    /**
     * Compare two ItemStacks to see if they are the same
     *
     * @param itemStack1 ItemStack 1 to compare
     * @param itemStack2 ItemStack 2 to compare
     * @return True is they are the same
     */
    public static boolean isSameItem(@Nonnull ItemStack itemStack1, @Nonnull ItemStack itemStack2) {
        return itemStack1.isItemEqual(itemStack2);
    }

    public static EnumFacing rotateAround(final EnumFacing forward) {
        switch (forward) {
            case NORTH:
                return EnumFacing.EAST;
            case EAST:
                return EnumFacing.SOUTH;
            case SOUTH:
                return EnumFacing.WEST;
            case WEST:
                return EnumFacing.NORTH;
            default:
                return forward;
        }
    }

    public static String getPropertyString(Map<IProperty<?>, Comparable<?>> values, String... extrasArgs) {
        StringBuilder stringbuilder = new StringBuilder();

        for (Map.Entry<IProperty<?>, Comparable<?>> entry : values.entrySet()) {
            if (stringbuilder.length() != 0) {
                stringbuilder.append(",");
            }

            IProperty<?> iproperty = entry.getKey();
            stringbuilder.append(iproperty.getName());
            stringbuilder.append("=");
            stringbuilder.append(getPropertyName(iproperty, entry.getValue()));
        }


        if (stringbuilder.length() == 0) {
            stringbuilder.append("inventory");
        }

        for (String args : extrasArgs) {
            if (stringbuilder.length() != 0)
                stringbuilder.append(",");
            stringbuilder.append(args);
        }

        return stringbuilder.toString();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> comparable) {
        return property.getName((T) comparable);
    }

    /**
     * Check if the code is running on a dev-environment instance
     *
     * @return True if running in a dev-environment
     */
    public static boolean isDevEnv() {
        return dev;
    }

    public static boolean disableCache() {
        return isDevEnv();
    }

    public static int hashItemStack(ItemStack stack) {
        if (stack == null)
            return -1;
        String name = stack.getItem().getUnlocalizedName(stack);
        return name.hashCode() << 8 | stack.getItemDamage();
    }
}