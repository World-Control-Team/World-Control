package worldcontrolteam.worldcontrol.init;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import worldcontrolteam.worldcontrol.api.card.ICardHolder;

import javax.annotation.Nullable;

public class WCCapabilities {

    @CapabilityInject(ICardHolder.class)
    public static Capability<ICardHolder> CARD_HOLDER;

    public static class Storage implements Capability.IStorage<ICardHolder> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<ICardHolder> capability, ICardHolder iCardHolder, EnumFacing enumFacing) {
            return iCardHolder.getCard().serializeNBT();
        }

        @Override
        public void readNBT(Capability<ICardHolder> capability, ICardHolder iCardHolder, EnumFacing enumFacing, NBTBase nbtBase) {
            iCardHolder.getCard().deserializeNBT((NBTTagCompound) nbtBase);
        }
    }
}
