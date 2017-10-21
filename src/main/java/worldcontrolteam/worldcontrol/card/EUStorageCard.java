package worldcontrolteam.worldcontrol.card;

import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.card.CardManager;
import worldcontrolteam.worldcontrol.api.card.StringWrapper;
import worldcontrolteam.worldcontrol.api.card.WorldCard;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import javax.annotation.Nonnull;
import java.util.List;

public class EUStorageCard extends WorldCard {
    public EUStorageCard(@Nonnull ItemStack stack, @Nonnull CardManager manager, @Nonnull BlockPos pos) {
        super(stack, manager, pos);
    }

    private IEnergyStorage storage;

    @Override
    public void updateData(World world, BlockPos pos) {
        storage = (IEnergyStorage) WCUtility.getTileEntity(world, pos).filter(t -> t instanceof IEnergyStorage).orElse(null);
    }

    @Override
    public List<StringWrapper> getData(List<StringWrapper> list, int displaySettings, boolean showLabels, int[] invisibleLines) {
        if (storage != null) {
            int energy = storage.getStored();
            int maxStorage = storage.getCapacity();
            list.add(new StringWrapper(WCUtility.translateFormatted("eu_energy", energy)));
            list.add(new StringWrapper(WCUtility.translateFormatted("eu_free", maxStorage - energy)));
            list.add(new StringWrapper(WCUtility.translateFormatted("eu_stored", maxStorage)));
            list.add(new StringWrapper(WCUtility.translateFormatted("fill_ratio", Math.round((float) energy / maxStorage * 100))));
            list.add(new StringWrapper(WCUtility.translateFormatted("info_panel_output", storage.getOutput())));
        } else {
            list.add(new StringWrapper("ErrorNo"));
        }
        return list;
    }

    @Override
    public int getCardColor() {
        return WCUtility.RED;
    }
}