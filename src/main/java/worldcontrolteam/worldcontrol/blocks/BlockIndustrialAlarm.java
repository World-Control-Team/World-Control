package worldcontrolteam.worldcontrol.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.tileentity.TileEntityHowlerAlarm;
import worldcontrolteam.worldcontrol.utils.GuiLib;


public class BlockIndustrialAlarm extends BlockBasicRotate{

    private static final AxisAlignedBB UP_BLOCK_AABB = new AxisAlignedBB( 0.125F, 0, 0.125F, 0.875F, 0.4375F, 0.875F );
    private static final AxisAlignedBB DOWN_BLOCK_AABB = new AxisAlignedBB( 0.125F, 1, 0.125F, 0.875F, 0.5625F, 0.875F );
    private static final AxisAlignedBB EAST_BLOCK_AABB = new AxisAlignedBB( 0F, 0.125F, 0.125F, 0.4375F,0.875F,  0.875F );
    private static final AxisAlignedBB WEST_BLOCK_AABB = new AxisAlignedBB( 1F, 0.125F, 0.125F, 0.5625F,0.875F,  0.875F );
    private static final AxisAlignedBB NORTH_BLOCK_AABB = new AxisAlignedBB(  0.125F,0.125F, 0.5625F, 0.875F,0.875F,  1 );
    private static final AxisAlignedBB SOUTH_BLOCK_AABB = new AxisAlignedBB(  0.125F,0.125F, 0.4375F, 0.875F,0.875F,  0);

    public BlockIndustrialAlarm(){
        this("IndustrialAlarm");
    }

    public BlockIndustrialAlarm(String name) {
        super(Material.IRON, name);
        this.defaultCreativeTab();
        setHardness(0.5F);
        //GameRegistry.register(this);
        //GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
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
        return GuiLib.INDUSTRIAL_ALARM;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        EnumFacing dir = state.getValue(BlockBasicRotate.FACING);
        switch (dir){
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

    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
        if(!(world.isSideSolid(pos.offset(world.getBlockState(pos).getValue(FACING).getOpposite()), world.getBlockState(pos).getValue(FACING).getOpposite(), true))){
            this.dropBlockAsItem((World) world, pos, world.getBlockState(pos), 0);
            ((World) world).setBlockToAir(pos);
        }
    }

    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos){
        if(!(world.isSideSolid(pos.offset(world.getBlockState(pos).getValue(FACING).getOpposite()), world.getBlockState(pos).getValue(FACING).getOpposite()))){
            this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (world.isSideSolid(pos.offset(facing.getOpposite()),facing.getOpposite())){
            return this.getDefaultState().withProperty(FACING, facing);
        }else{
            if(world.getBlockState(pos.down()).getBlock() != Blocks.AIR){
                return this.getDefaultState().withProperty(FACING, EnumFacing.UP);
            }else {
                return null;
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack){
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        world.setBlockState(pos, state, 2);
    }
}
