package worldcontrolteam.worldcontrol.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NBTUtils {

	public static final String BLOCK_POS = "blockPos";
	public static final String ITEM_NAME = "registryName";
	public static final String METADATA = "meta";
	public static final String STACKSIZE = "stacksize";
	public static final String NBTTAG = "tag";
	public static final String TITLE = "title";

	public static NBTTagCompound writeBlockPos(NBTTagCompound tagCompound, BlockPos pos){
		tagCompound.setLong(BLOCK_POS, pos.toLong());
		return tagCompound;
	}

	public static BlockPos getBlockPos(NBTTagCompound tagCompound){
		return BlockPos.fromLong(tagCompound.getLong(BLOCK_POS));
	}

	public static NBTTagCompound writeItemStack(NBTTagCompound tagCompound, ItemStack stack){
		tagCompound.setString(ITEM_NAME, stack.getItem().getRegistryName().toString());
		tagCompound.setInteger(METADATA, stack.getMetadata());
		tagCompound.setInteger(STACKSIZE, stack.stackSize);
		tagCompound.setString(NBTTAG, stack.getTagCompound().toString());
		return tagCompound;
	}

	public static ItemStack getItemStack(NBTTagCompound tagCompound){
		try{
			return GameRegistry.makeItemStack(tagCompound.getString(ITEM_NAME), tagCompound.getInteger(METADATA), tagCompound.getInteger(STACKSIZE), tagCompound.getString(NBTTAG));
		}catch (Exception e){
			WCUtility.error(String.format("Itemstack failed to transfer via NBT. Args are: Item: %s, Meta: %s, Stacksize: %s, Tag: %s", tagCompound.getString(ITEM_NAME),
					tagCompound.getInteger(METADATA), tagCompound.getInteger(STACKSIZE), tagCompound.getString(NBTTAG)));
		}
		return null;
	}

	public static NBTTagCompound writeTitle(NBTTagCompound tagCompound, String title){
		tagCompound.setString(TITLE, title);
		return tagCompound;
	}

	public static String getTitle(NBTTagCompound tagCompound){
		return tagCompound.getString(TITLE);
	}
}
