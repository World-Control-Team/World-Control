package worldcontrolteam.worldcontrol.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.api.thermometer.IHeatSeeker;

import java.util.List;

public class ItemThermometer extends WCBaseItem{

    private static List<IHeatSeeker> heatTypes;

    public ItemThermometer(){
        super("thermometer");
        this.setMaxStackSize(1);
        this.setMaxDamage(102);
    }

    public static void addInHeatTypes(List<IHeatSeeker> types){
        heatTypes = types;
    }

    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!heatTypes.isEmpty()) {
            if (!stack.hasTagCompound()) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setInteger("TYPE", 0);
                stack.setTagCompound(tagCompound);
            }
            if(stack.getTagCompound() != null){
                int toUse = stack.getTagCompound().getInteger("TYPE");
                IHeatSeeker user = heatTypes.get(toUse);
                if(user.canUse(world, pos, world.getTileEntity(pos))){
                    if(!world.isRemote) {
                        player.addChatComponentMessage(new TextComponentString(net.minecraft.client.resources.I18n.format("item.thermo.chatInfo", user.getHeat(world, pos, world.getTileEntity(pos)))));
                    }//System.out.println(user.getHeat(world, pos, world.getTileEntity(pos)));
                    stack.damageItem(10, player);
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.PASS;
    }


    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if(!heatTypes.isEmpty()){
            if( itemStack.hasTagCompound()){
                NBTTagCompound tag =itemStack.getTagCompound();
                int currentType = tag.getInteger("TYPE");
                if(currentType+1 < heatTypes.size()){
                    tag.setInteger("TYPE", currentType++);
                }else{
                    tag.setInteger("TYPE", 0);
                }
            }else {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setInteger("TYPE", 0);
                itemStack.setTagCompound(tagCompound);
            }
            return new ActionResult(EnumActionResult.SUCCESS, itemStack);
        }
        return new ActionResult(EnumActionResult.PASS, itemStack);
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        String name;
        if(stack.hasTagCompound()){
            name = I18n.translateToLocal(heatTypes.get(stack.getTagCompound().getInteger("TYPE")).getUnloalizedName());
        }else{
            name = I18n.translateToLocal("item.worldcontrol.thermo_base.name");
        }
        return name;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
        tooltip.add(net.minecraft.client.resources.I18n.format("item.thermo.tooltip", heatTypes.size()));
    }
}
