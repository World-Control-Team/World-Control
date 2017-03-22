package worldcontrolteam.worldcontrol.blocks;

import java.util.Random;

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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockBasicRotate extends BlockBasicTileProvider {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public enum RelativeDirection {
		LEFT(-1), RIGHT(1), FRONT(0), DOWN(-10), UP(-11), BACK(-2);

		// Index in EnumFacing.HORIZONTALS relative to facing
		private int relativeIndex;

		RelativeDirection(int relative) {
			relativeIndex = relative;
		}

		public int getRelativeIndex(){
			return this.relativeIndex;
		}

		public EnumFacing getTrueDirection(EnumFacing in){
			int facingIndex = in.getHorizontalIndex();
			if(facingIndex == -1)
				if(in == EnumFacing.DOWN)
					return EnumFacing.DOWN;
				else return EnumFacing.UP;
			facingIndex += relativeIndex;
			facingIndex %= 4;
			return EnumFacing.HORIZONTALS[facingIndex];
		}

		public static RelativeDirection getRelativeDirection(EnumFacing in, EnumFacing forwards){
			int index = in.getHorizontalIndex() - forwards.getHorizontalIndex();
			for(RelativeDirection r : RelativeDirection.values())
				if(r.getRelativeIndex() == index)
					return r;
			if(in == EnumFacing.DOWN)
				return DOWN;
			else return UP;
		}
	}

	public BlockBasicRotate(Material material) {
		super(material);
	}

	public static EnumFacing getFacing(World worldIn, BlockPos blockPos){
		IBlockState blockState = worldIn.getBlockState(blockPos);
		EnumFacing facingIn = blockState.getValue(FACING);
		return facingIn;
	}

	public static EnumFacing getTrueDirectionFromRelative(RelativeDirection relativeDirection, World worldIn, BlockPos blockPos){
		IBlockState blockState = worldIn.getBlockState(blockPos);
		EnumFacing facingIn = blockState.getValue(FACING);
		return relativeDirection.getTrueDirection(facingIn);
	}


	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack){
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		world.setBlockState(pos, state.withProperty(FACING, BlockBasicRotate.getFFE(world, pos, entity, false)), 2);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return this.getDefaultState().withProperty(FACING, BlockBasicRotate.getFFE(worldIn, pos, placer, false));
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state){
		dropItems(world, pos);
		super.breakBlock(world, pos, state);
	}

	private void dropItems(World world, BlockPos pos){
		Random rand = new Random();

		TileEntity tileEntity = world.getTileEntity(pos);
		if(!(tileEntity instanceof IInventory))
			return;
		IInventory inventory = (IInventory) tileEntity;

		for(int i = 0; i < inventory.getSizeInventory(); i++){
			ItemStack item = inventory.getStackInSlot(i);

			if(item != null && item.stackSize > 0){
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world, pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

				if(item.hasTagCompound())
					entityItem.getEntityItem().setTagCompound(item.getTagCompound().copy());

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
		EntityItem e = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this));
		world.spawnEntityInWorld(e);

	}

	//Copy for 1.8
	@SideOnly(Side.CLIENT)
	public IBlockState getStateForEntityRender(IBlockState state){
		return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
	}

	@Override
	public IBlockState getStateFromMeta(int meta){
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if(enumfacing.getAxis() == EnumFacing.Axis.Y)
			enumfacing = EnumFacing.NORTH;

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(FACING).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, new IProperty[]{FACING});
	}

	public static EnumFacing getFFE/*getFacingFromEntity*/(World world, BlockPos clickedBlock, EntityLivingBase entityIn, boolean safe){
		if(MathHelper.abs((float) entityIn.posX - clickedBlock.getX()) < 2.0F && MathHelper.abs((float) entityIn.posZ - clickedBlock.getZ()) < 2.0F){
			double d0 = entityIn.posY + entityIn.getEyeHeight();

			if(d0 - clickedBlock.getY() > 2.0D)
				return safe ? EnumFacing.SOUTH : EnumFacing.UP;

			if(clickedBlock.getY() - d0 > 0.0D)
				return safe ? EnumFacing.SOUTH : EnumFacing.DOWN;
		}

		return entityIn.getHorizontalFacing().getOpposite();
	}

}
