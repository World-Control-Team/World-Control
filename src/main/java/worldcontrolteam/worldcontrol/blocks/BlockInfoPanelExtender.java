package worldcontrolteam.worldcontrol.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import worldcontrolteam.worldcontrol.client.ClientUtil;
import worldcontrolteam.worldcontrol.screen.IScreenContainer;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanel;
import worldcontrolteam.worldcontrol.tileentity.TileEntityInfoPanelExtender;

public class BlockInfoPanelExtender extends BlockBasicRotate implements IScreenContainer {
    public BlockInfoPanelExtender(String name, boolean advanced) {
            super(Material.IRON, name);
    }

    @Override
    public TileEntity createTile(World world, int meta) {
        return new TileEntityInfoPanelExtender();
    }

    @Override
    public boolean hasGUI() {
        return false;
    }

    @Override
    public int guiID() {
        return 0;
    }

    @Override
    public EnumFacing getFacing(World worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public BlockPos getOrigin(World worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public void registerModels(ModelRegistryEvent event) {
        ClientUtil.registerToNormalWithoutMapper(this);
    }
}
