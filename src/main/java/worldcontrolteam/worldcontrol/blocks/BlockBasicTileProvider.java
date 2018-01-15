package worldcontrolteam.worldcontrol.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.init.IItemBlockFactory;
import worldcontrolteam.worldcontrol.init.IModelRegistrar;
import worldcontrolteam.worldcontrol.utils.WCUtility;

public abstract class BlockBasicTileProvider extends Block implements ITileEntityProvider, IModelRegistrar, IItemBlockFactory {

    public BlockBasicTileProvider(Material blockMaterial, String name) {
        super(blockMaterial);
        this.setBlockName(name);
    }

    @Override
    public ItemBlock createItemBlock() {
        return (ItemBlock) new ItemBlock(this).setRegistryName(getRegistryName());
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return getTile(world, meta);
    }

    public abstract TileEntity getTile(World world, int meta);

    public abstract boolean hasGUI();

    public abstract int guiID();

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (this.hasGUI()) {
            if (!WCUtility.getTileEntity(world, pos).isPresent() || player.isSneaking())
                return false;

            player.openGui(WorldControl.instance, guiID(), world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    protected void setBlockName(String name) {
        this.setUnlocalizedName("worldcontrol." + name);
        this.setRegistryName(name);
    }

    protected void defaultCreativeTab() {
        this.setCreativeTab(WorldControl.TAB);
    }
}
