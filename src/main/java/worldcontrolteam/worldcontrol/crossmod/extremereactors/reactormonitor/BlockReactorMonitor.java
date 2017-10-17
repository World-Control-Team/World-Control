package worldcontrolteam.worldcontrol.crossmod.extremereactors.reactormonitor;

import erogenousbeef.bigreactors.common.multiblock.PartTier;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.blocks.BlockBasicTileProvider;

import java.util.List;

/**
 * Created by dmf444 on 10/16/2017. Code originally written for World-Control.
 */
public class BlockReactorMonitor extends BlockBasicTileProvider {

    public BlockReactorMonitor() {
        super(Material.IRON, "extremereactormonitor");
        this.defaultCreativeTab();

    }

    @Override
    public TileEntity getTile(World world, int meta) {
        return new TileReactorMonitor(meta);
    }

    @Override
    public boolean hasGUI() {
        return false;
    }

    @Override
    public int guiID() {
        return -1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(itemIn));
        //TODO: keep up with ER Dev and re-add when needed
        //list.add(new ItemStack(itemIn, 1, 1));
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.add(PartTier.fromMeta(stack.getMetadata()).toString());
    }

}