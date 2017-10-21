package worldcontrolteam.worldcontrol.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import java.util.Random;

public abstract class BlockBasicRotate extends BlockBasicTileProvider {

    public static final PropertyDirection FACING = PropertyDirection.create("facing");


    public BlockBasicRotate(Material material, String name) {
        super(material, name);
    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        world.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, entity)), 2);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        dropItems(world, pos);
        super.breakBlock(world, pos, state);
    }

    private void dropItems(World world, BlockPos pos) {
        Random rand = new Random();

        TileEntity tileEntity = WCUtility.getTileEntity(world, pos).orElse(null);
        //TODO: for inventory rewrite
        if (!(tileEntity instanceof IInventory))
            return;
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);

            if (item != ItemStack.EMPTY && item.getCount() > 0) {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz, new ItemStack(item.getItem(), item.getCount(), item.getItemDamage()));

                if (item.hasTagCompound())
                    entityItem.getItem().setTagCompound(item.getTagCompound().copy());

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntity(entityItem);
                item.setCount(0);
            }
        }
        EntityItem e = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this));
        world.spawnEntity(e);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

}
