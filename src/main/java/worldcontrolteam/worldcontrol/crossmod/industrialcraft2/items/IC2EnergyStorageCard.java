package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items;

import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.StringWrapper;
import worldcontrolteam.worldcontrol.items.ItemBaseCard;
import worldcontrolteam.worldcontrol.utils.NBTUtils;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.List;

public class IC2EnergyStorageCard extends ItemBaseCard {

    public IC2EnergyStorageCard() {
        super("ic2_energy_storage_card");
    }

    @Override
    public CardState update(World world, ItemStack card) {
        CardState state;
        if (card.hasTagCompound()) {
            BlockPos pos = NBTUtils.getBlockPos(card.getTagCompound());
            TileEntityElectricBlock energySource = WCUtility.getTileEntity(world,pos,TileEntityElectricBlock.class).orElse(null);
            if (energySource!=null) {
                card.getTagCompound().setInteger("max_storage", energySource.getCapacity());
                card.getTagCompound().setInteger("capacity", energySource.getStored());
                card.getTagCompound().setInteger("output", energySource.getOutput());
                state = CardState.OK;
            } else state = CardState.INVALID_CARD;
        } else state = CardState.INVALID_CARD;
        return state;
    }

    @Override
    public List<StringWrapper> getStringData(List<StringWrapper> list, int displaySettings, ItemStack card, boolean showLabels) {
        if (card.hasTagCompound()) {
            NBTTagCompound tag = card.getTagCompound();
            int energy = tag.getInteger("capacity");
            int maxStorage = tag.getInteger("max_storage");

            list.add(new StringWrapper(WCUtility.translateFormatted("eu_energy", energy)));
            list.add(new StringWrapper(WCUtility.translateFormatted("eu_free", maxStorage - energy)));
            list.add(new StringWrapper(WCUtility.translateFormatted("eu_stored", maxStorage)));
            list.add(new StringWrapper(WCUtility.translateFormatted("fill_ratio", Math.round((float) energy / maxStorage * 100))));
            list.add(new StringWrapper(WCUtility.translateFormatted("info_panel_output", tag.getInteger("output"))));
        }
        return list;
    }

    @Override
    public List<String> getGuiData() {
        return null;
    }

    @Override
    public int getCardColor() {
        return WCUtility.RED;
    }
}
