package worldcontrolteam.worldcontrol.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;

public class BlockInfoPanelExtender extends BlockInfoPanel {
    public BlockInfoPanelExtender(String name, boolean advanced) {
        super(advanced?Material.IRON:Material.WOOD, name, advanced);
    }

    @Override
    public TileEntity createTile(World world, int meta) {
        return new TileEntityInfoPanel(true);
    }

    @Override
    public boolean hasGUI() {
        return false;
    }
}