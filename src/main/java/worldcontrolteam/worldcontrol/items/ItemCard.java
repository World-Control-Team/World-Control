package worldcontrolteam.worldcontrol.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import worldcontrolteam.worldcontrol.api.card.Card;
import worldcontrolteam.worldcontrol.api.card.CardManager;
import worldcontrolteam.worldcontrol.api.card.ICardHolder;
import worldcontrolteam.worldcontrol.init.WCCapabilities;
import worldcontrolteam.worldcontrol.init.WCContent;
import worldcontrolteam.worldcontrol.init.WCRegistries;
import worldcontrolteam.worldcontrol.utils.CardUtils;
import worldcontrolteam.worldcontrol.utils.NBTUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCard extends WCBaseItem {
    public ItemCard() {
        super("card");
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    public static ItemStack createCard(CardManager manager, NBTTagCompound click) {
        return new ItemStack(WCContent.CARD, 1, WCRegistries.REGISTRY.getValues().indexOf(manager), click);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        CardUtils.getCard(stack).ifPresent(c -> c.addTooltip(tooltip,worldIn,flagIn));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            List<CardManager> values = WCRegistries.REGISTRY.getValues();
            for (int i = 0; i < values.size(); i++) {
                CardManager manager = values.get(i);
                if (!manager.hasKit())
                    items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack);
    }

    @Nullable
    @Override
    public String getCreatorModId(ItemStack itemStack) {
        return CardUtils.getCardManager(itemStack).map(m -> m.getRegistryName().getResourceDomain()).orElse("worldcontrol");
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        CardManager manager = CardUtils.getCardManager(stack).orElse(null);
        if (manager!=null&&manager.hasKit()) {
            if (nbt != null && nbt.getBoolean("click"))
                return new Caps(manager.createCard(stack, NBTUtils.getBlockPos(nbt), NBTUtils.getEnumFacing(nbt)));
        }
        return new Caps(manager.createCard(stack, null, null));
    }

    public static class Caps implements ICapabilitySerializable<NBTTagCompound>, ICardHolder {
        private Card card;

        public Caps(Card card) {
            this.card = card;
        }

        @Override
        public Card getCard() {
            return card;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing) {
            return capability == WCCapabilities.CARD_HOLDER;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing) {
            return capability == WCCapabilities.CARD_HOLDER ? WCCapabilities.CARD_HOLDER.cast(this) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return card.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound) {
            if (!compound.getBoolean("click"))
                card.deserializeNBT(compound);
        }
    }
}