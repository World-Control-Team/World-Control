package worldcontrolteam.worldcontrol.crossmod.tesla;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.blocks.BlockBasicTileProvider;
import worldcontrolteam.worldcontrol.utils.GuiLib;

public class BlockTeslaAverageCounter extends BlockBasicTileProvider {

	public BlockTeslaAverageCounter() {
		super(Material.ANVIL);
		this.setRegistryName("tesla_average_counter");
		this.setCreativeTab(WorldControl.TAB);

		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public TileEntity getTile(World world, int meta){
		return new TileEntityTeslaAverageCounter();
	}

	@Override
	public boolean hasGUI(){
		return true;
	}

	@Override
	public int guiID(){
		return GuiLib.TESLA_AVERAGE_COUNTER;
	}
}
