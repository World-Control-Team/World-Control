package worldcontrolteam.worldcontrol.crossmod.tesla;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.utils.GuiLib;

public class BlockTeslaAverageCounter extends BlockBasicRotate {

    public BlockTeslaAverageCounter() {
        super(Material.ANVIL, "tesla_average_counter");
        this.setCreativeTab(WorldControl.TAB);
    }

    @Override
    public TileEntity createTile(World world, int meta) {
        return new TileEntityTeslaAverageCounter();
    }

    @Override
    public boolean hasGUI() {
        return true;
    }

    @Override
    public int guiID() {
        return GuiLib.TESLA_AVERAGE_COUNTER;
    }
}
