package worldcontrolteam.worldcontrol.items;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.wrappers.FluidHandlerWrapper;
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
            IFluidTankProperties[] properties;
            if(world.getTileEntity(pos) instanceof IFluidHandler){
                properties = ((IFluidHandler) world.getTileEntity(pos)).getTankProperties();
            }else{
                properties = new FluidHandlerWrapper(((net.minecraftforge.fluids.IFluidHandler)world.getTileEntity(pos)), EnumFacing.DOWN).getTankProperties();
            }
            if(properties != null) {
                card.getTagCompound().setInteger("capacity", properties[0].getCapacity());
                if (properties[0].getContents() != null) {
                    card.getTagCompound().setInteger("amount", properties[0].getContents().amount);
                    card.getTagCompound().setString("fluid", properties[0].getContents().getFluid().getName());
                }
                return CardState.OK;
            }
        }
        return CardState.NO_TARGET;
    }

    @Override
    public List<StringWrapper> getStringData(List<StringWrapper> list, int displaySettings, ItemStack card, boolean showLabels) {
        if(card.hasTagCompound()){
            String fluid = null;
            if(card.getTagCompound().hasKey("fluid")){
                fluid = card.getTagCompound().getString("fluid");
            }
            int capacity = card.getTagCompound().getInteger("capacity");
            int amount = card.getTagCompound().hasKey("amount") ? card.getTagCompound().getInteger("amount") : 0;

            if(fluid != null){
                list.add(new StringWrapper(WCUtility.translateFormatted("LiquidName", fluid)));
            }else{
                list.add(new StringWrapper(WCUtility.translateFormatted("LiquidName", WCUtility.translate("msg.worldcontrol.None"))));
            }
            list.add(new StringWrapper(WCUtility.translateFormatted("LiquidAmount", amount)));
            list.add(new StringWrapper(WCUtility.translateFormatted("LiquidFree", capacity - amount)));
            list.add(new StringWrapper(WCUtility.translateFormatted("LiquidCapacity", capacity)));
            list.add(new StringWrapper(WCUtility.translateFormatted("LiquidPercentage", capacity == 0 ? 100 : ((amount / capacity) * 100))));
        }
        return list;
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
