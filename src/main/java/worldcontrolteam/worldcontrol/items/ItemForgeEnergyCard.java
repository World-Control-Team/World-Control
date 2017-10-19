package worldcontrolteam.worldcontrol.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.StringWrapper;
import worldcontrolteam.worldcontrol.utils.NBTUtils;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.List;

public class ItemForgeEnergyCard extends ItemBaseCard {

    public ItemForgeEnergyCard() {
        super("forge_energy_card");
    }

    @Override
    public CardState update(World world, ItemStack card) {
        if (card.hasTagCompound()) {
            BlockPos pos = NBTUtils.getBlockPos(card.getTagCompound());
            if (world.getTileEntity(pos) instanceof IEnergyStorage) {
                IEnergyStorage storage = (IEnergyStorage) world.getTileEntity(pos);
                card.getTagCompound().setInteger("stored_power", storage.getEnergyStored());
                card.getTagCompound().setInteger("capacity", storage.getMaxEnergyStored());
                return CardState.OK;
            }
        }
        return CardState.NO_TARGET;
    }

    @Override
    public List<StringWrapper> getStringData(List<StringWrapper> list, int displaySettings, ItemStack card, boolean showLabels) {
        if (card.hasTagCompound()) {
            int capacity = card.getTagCompound().getInteger("capacity");
            int storedPower = card.getTagCompound().getInteger("stored_power");

            list.add(new StringWrapper(WCUtility.translateFormatted("tesla_energy", storedPower)));
            list.add(new StringWrapper(WCUtility.translateFormatted("tesla_free", capacity - storedPower)));
            list.add(new StringWrapper(WCUtility.translateFormatted("tesla_stored", capacity)));
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
        return 0;
    }
}
