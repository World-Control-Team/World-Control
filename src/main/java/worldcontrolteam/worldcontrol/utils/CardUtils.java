package worldcontrolteam.worldcontrol.utils;

import net.minecraft.item.ItemStack;
import worldcontrolteam.worldcontrol.api.card.Card;
import worldcontrolteam.worldcontrol.api.card.CardManager;
import worldcontrolteam.worldcontrol.init.WCCapabilities;
import worldcontrolteam.worldcontrol.init.WCRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.text.html.Option;
import java.util.Optional;

public class CardUtils {

    @Nonnull
    public static Optional<Card> getCard(ItemStack stack) {
        return !stack.hasCapability(WCCapabilities.CARD_HOLDER, null) ? Optional.empty() : Optional.of(stack.getCapability(WCCapabilities.CARD_HOLDER, null).getCard());
    }

    @Nonnull
    public static Optional<CardManager> getCardManager(ItemStack stack) {
        return Optional.ofNullable(WCRegistries.REGISTRY.getValues().get(stack.getMetadata()));
    }

    @Nullable
    public static Card getNullableCard(ItemStack stack) {
        return getCard(stack).orElse(null);
    }
}