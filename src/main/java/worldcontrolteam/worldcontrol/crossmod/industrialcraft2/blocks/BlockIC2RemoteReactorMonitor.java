package worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.client.ClientUtil;
import worldcontrolteam.worldcontrol.tileentity.TileEntityBaseReactorHeatMonitor;
import worldcontrolteam.worldcontrol.utils.GuiLib;
import worldcontrolteam.worldcontrol.utils.WCUtility;

import static worldcontrolteam.worldcontrol.crossmod.industrialcraft2.blocks.BlockIC2ReactorMonitor.RENDER_TYPE;

public class BlockIC2RemoteReactorMonitor extends BlockBasicRotate {

    public BlockIC2RemoteReactorMonitor() {
        super(Material.IRON, "ic2_remote_reactor_monitor");
        this.setDefaultState(this.getDefaultState().withProperty(BlockBasicRotate.FACING, EnumFacing.UP));
        this.defaultCreativeTab();
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
        return new TileEntityIC2RemoteReactorMonitor();
    }

    @Override
    public boolean hasGUI() {
        return true;
    }

    @Override
    public int guiID() {
        return GuiLib.IC2_REMOTE_HEAT_MONITOR;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return WCUtility.getTileEntity(world, pos, TileEntityIC2RemoteReactorMonitor.class).map(t -> t.isOverHeated() ? 15 : 0).orElse(0);
    }

    public boolean canProvidePower(IBlockState state)
    {
        return true;
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

    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

}
