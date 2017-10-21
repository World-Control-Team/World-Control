package worldcontrolteam.worldcontrol.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockBasicTileProvider extends Block implements ITileEntityProvider {

    public static List<Block> wcBlocks = new ArrayList<>();

    public BlockBasicTileProvider(Material blockMaterial, String name) {
        super(blockMaterial);
        this.setBlockName(name);


        wcBlocks.add(this);
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
            TileEntity tileEntity = WCUtility.getTileEntity(world, pos).orElse(null);
            if (tileEntity == null || player.isSneaking())
                return false;

            player.openGui(WorldControl.instance, guiID(), world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    protected void setBlockName(String name) {
        this.setUnlocalizedName("worldcontrol."+name);
        this.setRegistryName(name);
    }

    protected void defaultCreativeTab() {
        this.setCreativeTab(WorldControl.TAB);
    }
}
