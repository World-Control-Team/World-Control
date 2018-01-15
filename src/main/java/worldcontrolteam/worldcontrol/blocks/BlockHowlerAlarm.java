package worldcontrolteam.worldcontrol.blocks;


import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;
import worldcontrolteam.worldcontrol.utils.GuiLib;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public class BlockHowlerAlarm extends BlockIndustrialAlarm {

    public BlockHowlerAlarm() {
        super("howler_alarm");
    }

    @Override
    public TileEntity getTile(World world, int meta) {
        return new TileEntityHowlerAlarm();
    }

    @Override
    public boolean hasGUI() {
        return true;
    }

    @Override
    public int guiID() {
        return GuiLib.HOWLER_ALARM;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem(hand) != ItemStack.EMPTY) {
            if (player.getHeldItem(hand).getItem() instanceof ItemDye) {
                WCUtility.getTileEntity(world, pos, TileEntityHowlerAlarm.class).ifPresent(tile -> tile.setColor(ItemDye.DYE_COLORS[player.getHeldItem(hand).getMetadata()]));
                world.markBlockRangeForRenderUpdate(pos, pos);
                return true;
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
    }

}