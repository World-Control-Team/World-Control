package worldcontrolteam.worldcontrol.card;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import worldcontrolteam.worldcontrol.api.card.Card;
import worldcontrolteam.worldcontrol.api.card.CardManager;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.StringWrapper;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import javax.annotation.Nonnull;
import java.util.List;

public class TimeCard extends Card {

    private String time;

    public TimeCard(@Nonnull ItemStack stack, @Nonnull CardManager manager) {
        super(stack, manager);
        setState(CardState.OK);
    }

    @Override
    public int getCardColor() {
        return WCUtility.YELLOW;
    }

    @Override
    public List<StringWrapper> getData(List<StringWrapper> list, int displaySettings, boolean showLabels, int[] visibleLines) {
        StringWrapper text = new StringWrapper();
        text.textLeft = WCUtility.translateFormatted("time_card.time", time);
        list.add(text);
        return list;
    }

    @Override
    public void update(World world) {
        int time = (int) ((world.getWorldTime() + 6000) % 24000);
        int hours = time / 1000;
        int minutes = time % 1000 * 6 / 100;
        String suffix = hours < 12 ? "AM" : "PM";
        hours %= 12;
        if (hours == 0)
            hours += 12;
        this.time = String.format("%02d:%02d %s", hours, minutes, suffix);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        if (time != null)
            tag.setString("time", time);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        if (compound.hasKey("time", Constants.NBT.TAG_STRING))
            time = compound.getString("time");
    }
}