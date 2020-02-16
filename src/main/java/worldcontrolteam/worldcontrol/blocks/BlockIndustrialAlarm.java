package worldcontrolteam.worldcontrol.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.client.ClientUtil;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;
import worldcontrolteam.worldcontrol.utils.GuiLib;


public class BlockIndustrialAlarm extends BlockBasicRotate {

    private static final AxisAlignedBB UP_BLOCK_AABB = new AxisAlignedBB(0.125F, 0, 0.125F, 0.875F, 0.4375F, 0.875F);
    private static final AxisAlignedBB DOWN_BLOCK_AABB = new AxisAlignedBB(0.125F, 1, 0.125F, 0.875F, 0.5625F, 0.875F);
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB(0F, 0.125F, 0.125F, 0.4375F, 0.875F, 0.875F);
    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB(1F, 0.125F, 0.125F, 0.5625F, 0.875F, 0.875F);
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(0.125F, 0.125F, 0.5625F, 0.875F, 0.875F, 1);
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(0.125F, 0.125F, 0.4375F, 0.875F, 0.875F, 0);

    public BlockIndustrialAlarm() {
        this("industrial_alarm");
    }

    public BlockIndustrialAlarm(String name) {
        super(Material.IRON, name);
        this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.UP));
        this.defaultCreativeTab();
        setHardness(0.5F);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        final String resourcePath = getRegistryName().toString();
        ClientUtil.setCustomStateMapper(this, state -> new ModelResourceLocation(resourcePath, ClientUtil.getPropertyString(state.getProperties())));
        NonNullList<ItemStack> subBlocks = NonNullList.create();
        getSubBlocks(null, subBlocks);
        for (ItemStack stack : subBlocks) {
            IBlockState state = getDefaultState();
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), stack.getMetadata(), new ModelResourceLocation(resourcePath, ClientUtil.getPropertyString(state.getProperties())));
        }
    }

    @Override
    public TileEntity createTile(World world, int meta) {
        return new TileEntityHowlerAlarm();
    }

    @Override
    public boolean hasGUI() {
        return true;
    }

    @Override
    public int guiID() {
        return GuiLib.INDUSTRIAL_ALARM;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing dir = state.getValue(BlockBasicRotate.FACING);
        switch (dir) {
            case NORTH:
                return NORTH_BLOCK_AABB;
            case SOUTH:
                return SOUTH_BLOCK_AABB;
            case EAST:
                return EAST_BLOCK_AABB;
            case WEST:
                return WEST_BLOCK_AABB;
            case DOWN:
                return DOWN_BLOCK_AABB;
            default:
                return UP_BLOCK_AABB;
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if (!(world.isSideSolid(pos.offset(world.getBlockState(pos).getValue(FACING).getOpposite()), world.getBlockState(pos).getValue(FACING).getOpposite(), true))) {
            this.dropBlockAsItem((World) world, pos, world.getBlockState(pos), 0);
            ((World) world).setBlockToAir(pos);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!(world.isSideSolid(pos.offset(world.getBlockState(pos).getValue(FACING).getOpposite()), world.getBlockState(pos).getValue(FACING).getOpposite()))) {
            this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (world.isSideSolid(pos.offset(facing.getOpposite()), facing.getOpposite())) {
            return this.getDefaultState().withProperty(FACING, facing);
        } else {
            if (world.getBlockState(pos.down()).getBlock() != Blocks.AIR) {
                return this.getDefaultState().withProperty(FACING, EnumFacing.UP);
            } else if(world.getBlockState(pos.up()).getBlock() != Blocks.AIR){
                return this.getDefaultState().withProperty(FACING, EnumFacing.DOWN);
            }
        }
        return null;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing facing) {
        if (world.isSideSolid(pos.offset(facing.getOpposite()), facing.getOpposite())) {
            return true;
        } else {
            if (world.getBlockState(pos.down()).getBlock() != Blocks.AIR || world.getBlockState(pos.up()).getBlock() != Blocks.AIR) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        world.setBlockState(pos, state, 2);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if(!world.isRemote){
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileEntityHowlerAlarm) {
                TileEntityHowlerAlarm alarm = (TileEntityHowlerAlarm) tile;
                alarm.forceSoundStop();
            }
        }
        super.breakBlock(world, pos, state);
    }
}
