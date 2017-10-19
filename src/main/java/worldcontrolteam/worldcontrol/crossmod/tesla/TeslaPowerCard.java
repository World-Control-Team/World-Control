package worldcontrolteam.worldcontrol.crossmod.tesla;

import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.StringWrapper;
import worldcontrolteam.worldcontrol.items.ItemBaseCard;
import worldcontrolteam.worldcontrol.utils.NBTUtils;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.List;

public class TeslaPowerCard extends ItemBaseCard {

    public TeslaPowerCard() {
        super("tesla_power_card");
    }

    @Override
    public CardState update(World world, ItemStack card) {
        if (card.hasTagCompound()) {
            BlockPos pos = NBTUtils.getBlockPos(card.getTagCompound());
            if (world.getTileEntity(pos).hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN)) {
                ITeslaHolder power = world.getTileEntity(pos).getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.DOWN);
                card.getTagCompound().setLong("stored_power", power.getStoredPower());
                card.getTagCompound().setLong("capacity", power.getCapacity());
                return CardState.OK;
            }
        }
        return CardState.NO_TARGET;
    }

    @Override
    public List<StringWrapper> getStringData(List<StringWrapper> list, int displaySettings, ItemStack card, boolean showLabels) {
        if (card.hasTagCompound()) {
            long capacity = card.getTagCompound().getLong("capacity");
            long storedPower = card.getTagCompound().getLong("stored_power");

            list.add(new StringWrapper(WCUtility.translateFormatted("forge_energy", storedPower)));
            list.add(new StringWrapper(WCUtility.translateFormatted("forge_free", capacity - storedPower)));
            list.add(new StringWrapper(WCUtility.translateFormatted("forge_stored", capacity)));
            list.add(new StringWrapper(WCUtility.translateFormatted("fill_ratio", Math.round((float) storedPower / capacity * 100))));
        }
        return list;
    }

    @Override
    public List<String> getGuiData() {
        return null;
    }

    @Override
    public int getCardColor() {
        return WCUtility.GREEN;
    }
}
