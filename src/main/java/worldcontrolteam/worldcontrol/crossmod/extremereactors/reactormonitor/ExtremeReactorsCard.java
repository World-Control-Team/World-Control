package worldcontrolteam.worldcontrol.crossmod.extremereactors.reactormonitor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.compat.StringWrapper;
import worldcontrolteam.worldcontrol.items.ItemBaseCard;
import worldcontrolteam.worldcontrol.utils.NBTUtils;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.List;

/**
 * Created by dmf444 on 10/16/2017. Code originally written for World-Control.
 */
public class ExtremeReactorsCard extends ItemBaseCard {

    public ExtremeReactorsCard() {
        super("er_card");
    }

    @Override
    public CardState update(World world, ItemStack card) {
        if (card.hasTagCompound()) {
            BlockPos pos = NBTUtils.getBlockPos(card.getTagCompound());
            TileReactorMonitor reactorMonitor = WCUtility.getTileEntity(world, pos, TileReactorMonitor.class).orElse(null);
            if (reactorMonitor != null) {
                NBTTagCompound tag = new NBTTagCompound();

                tag.setBoolean("Online", reactorMonitor.isReactorOnline());
                tag.setDouble("storedEnergy", (double) reactorMonitor.getEnergyStored());
                tag.setDouble("createdEnergy", (double) reactorMonitor.getEnergyGenerated());
                tag.setInteger("Temp", reactorMonitor.getTemp());
                tag.setDouble("FillPercent", (double) reactorMonitor.getEnergyOutPercent());
                tag.setBoolean("isPassive", reactorMonitor.getReactorController().isPassivelyCooled());

                if (reactorMonitor.getReactorController().getCoolantContainer().getVaporType() != null) {
                    tag.setString("VaporType", reactorMonitor.getReactorController().getCoolantContainer().getVaporType().getLocalizedName(new FluidStack(reactorMonitor.getReactorController().getCoolantContainer().getVaporType(), 1)));
                    tag.setInteger("VaporAmount", reactorMonitor.getReactorController().getCoolantContainer().getVaporAmount());
                } else {
                    tag.setString("VaporType", "Empty");
                    tag.setInteger("VaporAmount", 0);
                }
                if (reactorMonitor.getReactorController().getCoolantContainer().getCoolantType() != null) {
                    tag.setString("CoolantType", new FluidStack(reactorMonitor.getReactorController().getCoolantContainer().getCoolantType(), 1).getLocalizedName());
                    tag.setInteger("CoolantAmount", reactorMonitor.getReactorController().getCoolantContainer().getCoolantAmount());
                } else {
                    tag.setString("CoolantType", "Empty");
                    tag.setInteger("CoolantAmount", 0);
                }

                card.getTagCompound().setTag("reactorData", tag);
                return CardState.OK;

            } else {
                return CardState.INVALID_CARD;
            }
        }
        return CardState.NO_TARGET;
    }

    @Override
    public List<StringWrapper> getStringData(List<StringWrapper> list, int displaySettings, ItemStack card, boolean showLabels) {
        if (card.hasTagCompound()) {
            if (card.getTagCompound().hasKey("reactorData")) {
                NBTTagCompound tag = card.getTagCompound().getCompoundTag("reactorData");

                double perOut = tag.getDouble("FillPercent");
                double energyStored = tag.getDouble("storedEnergy");
                double outputlvl = tag.getDouble("createdEnergy");
                double coreTemp = tag.getDouble("Temp");

                int ioutputlvl = (int) outputlvl;
                int ienergyStored = (int) energyStored;
                boolean passive = tag.getBoolean("isPassive");

                String text;
                StringWrapper line;

                if (passive) {
                    //Temperature
                    line = new StringWrapper();
                    line.textLeft = WCUtility.translateFormatted("exreactors.temp", coreTemp);
                    list.add(line);
                    //Stored Energy
                    line = new StringWrapper();
                    line.textLeft = WCUtility.translateFormatted("exreactors.energystored", ienergyStored);
                    list.add(line);
                    //Energy Created Frequency
                    line = new StringWrapper();
                    line.textLeft = WCUtility.translateFormatted("exreactors.createdenergy", ioutputlvl);
                    list.add(line);
                    //Output Percentage
                    line = new StringWrapper();
                    line.textLeft = WCUtility.translateFormatted("exreactors.percentage", perOut);
                    list.add(line);
                } else {
                    //Temperature
                    line = new StringWrapper();
                    line.textLeft = WCUtility.translateFormatted("exreactors.temp", coreTemp);
                    list.add(line);
                    //Energy Created Frequency
                    line = new StringWrapper();
                    line.textLeft = WCUtility.translateFormatted("exreactors.steamoutput", ioutputlvl);
                    list.add(line);
                    //Stored Energy
                    line = new StringWrapper();
                    line.textLeft = WCUtility.translateFormatted("exreactors.coolanttank", tag.getString("CoolantType"), tag.getInteger("CoolantAmount"));
                    list.add(line);
                    //Vapor Tank
                    line = new StringWrapper();
                    line.textLeft = WCUtility.translateFormatted("exreactors.outputtank", tag.getString("VaporType"), tag.getInteger("VaporAmount"));
                    list.add(line);
                }

                //Active
                int txtColor;
                boolean reactorPowered = tag.getBoolean("Online");
                if (reactorPowered) {
                    txtColor = 0x00ff00;
                    text = WCUtility.translateFormatted("info_panel_on");
                } else {
                    txtColor = 0xff0000;
                    text = WCUtility.translateFormatted("info_panel_off");
                }
                if (list.size() > 0) {
                    StringWrapper firstLine = list.get(0);
                    firstLine.textRight = text;
                    firstLine.colorRight = txtColor;
                } else {
                    line = new StringWrapper();
                    line.textLeft = text;
                    line.colorLeft = txtColor;
                    list.add(line);
                }
            }
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
