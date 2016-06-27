package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.items;


import ic2.api.reactor.IReactor;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.StringWrapper;
import worldcontrolteam.worldcontrol.crossmod.industrialcraft2.ReactorUtils;
import worldcontrolteam.worldcontrol.items.ItemBaseCard;
import worldcontrolteam.worldcontrol.utils.NBTUtils;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.LinkedList;
import java.util.List;

public class IC2ReactorCard extends ItemBaseCard{

    public IC2ReactorCard() {
        super("IC2ReactorCard");
    }

    @Override
    public CardState update(World world, ItemStack card) {
        if(card.hasTagCompound()){
            BlockPos pos = NBTUtils.getBlockPos(card.getTagCompound());
            IReactor reactor = ReactorUtils.getReactorAt(world, pos);
            if (reactor != null) {
                card.getTagCompound().setInteger("heat", reactor.getHeat());
                card.getTagCompound().setInteger("maxHeat", reactor.getMaxHeat());
                card.getTagCompound().setBoolean("reactorPoweredB", ReactorUtils.isProducing(world, pos));
                card.getTagCompound().setInteger("output", (int) Math.round(reactor.getReactorEUEnergyOutput()));
                //TODO: REIMPLEMENT STEAM REACTORS?
                boolean isSteam = false;
                //card.setBoolean("isSteam", isSteam);
                IInventory inventory = (IInventory) reactor;
                int slotCount = inventory.getSizeInventory();
                int timeLeft = 0;
                for (int i = 0; i < slotCount; i++) {
                    ItemStack rStack = inventory.getStackInSlot(i);
                    if (rStack != null) {
                        timeLeft = Math.max(timeLeft, ReactorUtils.getNuclearCellTimeLeft(rStack));
                    }
                }
                card.getTagCompound().setInteger("timeLeft", timeLeft * (isSteam ? 20 : reactor.getTickRate()) / 20);
                return CardState.OK;
            }
        }
        return CardState.NO_TARGET;
    }

    @Override
    public List<StringWrapper> getStringData(List<StringWrapper> list, int displaySettings, ItemStack card, boolean showLabels) {
        String text;
        StringWrapper line;

        if(card.hasTagCompound()) {
            //Heat
            line = new StringWrapper();
            line.textLeft = WCUtility.translateFormatted("InfoPanelHeat", card.getTagCompound().getInteger("heat"));
            list.add(line);
            //Max Heat
            line = new StringWrapper();
            line.textLeft = WCUtility.translateFormatted("InfoPanelMaxHeat", card.getTagCompound().getInteger("maxHeat"));
            list.add(line);
            //Melting
            line = new StringWrapper();
            line.textLeft = WCUtility.translateFormatted("InfoPanelMelting", card.getTagCompound().getInteger("maxHeat") * 85 / 100, showLabels);
            list.add(line);
            //Output
            line = new StringWrapper();
            line.textLeft = WCUtility.translateFormatted("InfoPanelOutput", card.getTagCompound().getInteger("output"));
            list.add(line);
            //Time Left
            int timeLeft = card.getTagCompound().getInteger("timeLeft");
            int hours = timeLeft / 3600;
            int minutes = (timeLeft % 3600) / 60;
            int seconds = timeLeft % 60;
            line = new StringWrapper();
            String time = String.format("%d:%02d:%02d", hours, minutes, seconds);
            line.textLeft = WCUtility.translateFormatted("InfoPanelTimeRemaining", time, showLabels);
            list.add(line);
            //On/Off
            int txtColor = 0;
            boolean reactorPowered = card.getTagCompound().getBoolean("reactorPoweredB");
            if (reactorPowered) {
                txtColor = 0x00ff00;
                text = WCUtility.translateFormatted("InfoPanelOn");
            } else {
                txtColor = 0xff0000;
                text = WCUtility.translateFormatted("InfoPanelOff");
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


        return list;
    }

    @Override
    public List<String> getGuiData() {
        return null;
    }

    @Override
    public int getCardColor() {
        return 4408131;
    }
}
