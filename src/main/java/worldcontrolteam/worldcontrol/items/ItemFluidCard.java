package worldcontrolteam.worldcontrol.items;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import worldcontrolteam.worldcontrol.api.card.CardState;
import worldcontrolteam.worldcontrol.api.card.StringWrapper;
import worldcontrolteam.worldcontrol.utils.NBTUtils;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.List;

public class ItemFluidCard extends ItemBaseCard{

    public ItemFluidCard() {
        super("ItemFluidCard");
    }

    @Override
    public CardState update(World world, ItemStack card) {
        if(card.hasTagCompound()){
            BlockPos pos = NBTUtils.getBlockPos(card.getTagCompound());
            if(world.getTileEntity(pos) instanceof IFluidHandler){
                IFluidTank tank = (IFluidTank) world.getTileEntity(pos);
                card.getTagCompound().setInteger("capacity", tank.getCapacity());
                card.getTagCompound().setInteger("amount", tank.getFluidAmount());
                if(tank.getFluid() != null){
                    card.getTagCompound().setTag("fluid", tank.getFluid().writeToNBT(new NBTTagCompound()));
                }
                return CardState.OK;
            }
        }
        return CardState.NO_TARGET;
    }

    @Override
    public List<StringWrapper> getStringData(List<StringWrapper> list, int displaySettings, ItemStack card, boolean showLabels) {
        if(card.hasTagCompound()){
            FluidStack fluid = null;
            if(card.getTagCompound().hasKey("fluid")){
                fluid = FluidStack.loadFluidStackFromNBT(card.getTagCompound().getCompoundTag("fluid"));
            }
            int capacity = card.getTagCompound().getInteger("capacity");
            int amount = card.getTagCompound().getInteger("amount");

            if(fluid != null){
                list.add(new StringWrapper(WCUtility.translateFormatted("LiquidName", fluid.getLocalizedName())));
            }else{
                list.add(new StringWrapper(WCUtility.translateFormatted("LiquidName", WCUtility.translate("msg.worldcontrol.None"))));
            }
            list.add(new StringWrapper(WCUtility.translateFormatted("LiquidAmount", amount)));
            list.add(new StringWrapper(WCUtility.translateFormatted("LiquidFree", capacity - amount)));
            list.add(new StringWrapper(WCUtility.translateFormatted("LiquidCapacity", capacity)));
            list.add(new StringWrapper(WCUtility.translateFormatted("LiquidPercentage", capacity == 0 ? 100 : ((amount / capacity) * 100))));
        }
        return null;
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
