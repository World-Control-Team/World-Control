package worldcontrolteam.worldcontrol.crossmod.extremereactors.reactormonitor;

import erogenousbeef.bigreactors.common.multiblock.MultiblockReactor;
import erogenousbeef.bigreactors.common.multiblock.PartTier;
import erogenousbeef.bigreactors.common.multiblock.interfaces.ITickableMultiblockPart;
import erogenousbeef.bigreactors.common.multiblock.tileentity.TileEntityReactorPartBase;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by dmf444 on 10/16/2017. Code originally written for World-Control.
 */
public class TileReactorMonitor extends TileEntityReactorPartBase implements ITickableMultiblockPart {

    private boolean isReactorOn;
    private float energyStored;
    private float energyGen;
    private float EstoredPer;
    private int temp;
    private boolean shouldBlockCache;
    private int meta;

    public TileReactorMonitor() {
        this.meta = 0;
        shouldBlockCache = false;
    }

    public TileReactorMonitor(int meta) {
        shouldBlockCache = false;
        this.meta = meta;
    }

    public boolean isGoodForSides(IMultiblockValidator validatorCallback) {
        return true;
    }

    public boolean isGoodForTop(IMultiblockValidator validatorCallback) {
        return true;
    }

    public boolean isGoodForBottom(IMultiblockValidator validatorCallback) {
        return true;
    }


    @Override
    public void onMultiblockServerTick() {
        if (shouldBlockCache) {
            isReactorOn = this.getReactorController().getActive();
            energyStored = this.getReactorController().getEnergyStored();
            energyGen = this.getReactorController().getEnergyGeneratedLastTick();
            EstoredPer = this.getReactorController().getEnergyStoredPercentage();
            temp = (int) this.getReactorController().getFuelHeat();
            //NCLog.fatal(this.getReactorController().getEnergyStored());//Current Stored Energy
            //NCLog.fatal(energyStored);
            //NCLog.fatal(this.getReactorController().getActive());//On or Off
            //NCLog.fatal(this.getReactorController().getEnergyGeneratedLastTick());//String.format("%.2f flux per tick", this.getReactorController().getEnergyGeneratedLastTick())
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        shouldBlockCache = tag.getBoolean("cache");
        this.meta = tag.getInteger("meta");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("cache", shouldBlockCache);
        tag.setInteger("meta", this.meta);
        return tag;
    }

    public float getEnergyStored() {
        return energyStored;
    }

    public float getEnergyGenerated() {
        return energyGen;
    }

    public boolean isReactorOnline() {
        return isReactorOn;
    }

    public void startFetching() {
        shouldBlockCache = true;
    }

    public float getEnergyOutPercent() {
        return EstoredPer;
    }

    public int getTemp() {
        return temp;
    }

    public MultiblockReactor getReactor() {
        return this.getReactorController();
    }

    public PartTier getPartTier() {
        return PartTier.fromMeta(this.meta);
    }
}
