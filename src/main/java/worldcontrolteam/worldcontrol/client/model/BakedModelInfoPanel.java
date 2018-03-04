package worldcontrolteam.worldcontrol.client.model;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import worldcontrolteam.worldcontrol.WorldControl;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.blocks.BlockInfoPanel;
import worldcontrolteam.worldcontrol.client.model.base.BakedModelBase;
import worldcontrolteam.worldcontrol.client.model.base.ModelBase;
import worldcontrolteam.worldcontrol.client.model.base.ModelUtil;
import worldcontrolteam.worldcontrol.client.model.base.TextureArray;
import worldcontrolteam.worldcontrol.client.model.part.BakedCuboid;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class BakedModelInfoPanel extends BakedModelBase {
    private BakedCuboid item;

    public BakedModelInfoPanel(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ModelBase model) {
        super(state, format, bakedTextureGetter, model);
        item = ModelUtil.makeCuboid(format, 0, 0, 0, 1, 1, 1, ModelUtil.FULL_FACES, textureArray, -1);
    }

    @Override
    public List<BakedQuad> buildQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = Lists.newArrayList();
        if (state == null) {
            item.addToList(quads, side);
            return quads;
        } else {
            TextureArray arr = new TextureArray(this.textureArray.getParticleTexture());
            int screenTex = 0;
            if (!state.getValue(BlockInfoPanel.CONNECTED_LEFT))
                screenTex += 1;
            if (!state.getValue(BlockInfoPanel.CONNECTED_RIGHT))
                screenTex += 2;
            if (!state.getValue(BlockInfoPanel.CONNECTED_UP))
                screenTex += 4;
            if (!state.getValue(BlockInfoPanel.CONNECTED_DOWN))
                screenTex += 8;
            boolean isOn = state.getValue(BlockInfoPanel.POWERED);
            int color = state.getValue(BlockInfoPanel.COLOR);
            TextureAtlasSprite screen = this.getter.apply(new ResourceLocation(String.format("%s:blocks/infopanel/%s/%d/%d", WorldControl.MODID, !isOn ? "on" : "off", color, screenTex)));
            TextureAtlasSprite back = this.getter.apply(new ResourceLocation(String.format("%s:blocks/infopanel/panel_advanced_back", WorldControl.MODID)));
            EnumFacing facing = state.getValue(BlockBasicRotate.FACING);

            if (facing == EnumFacing.NORTH)
                arr.setNorthTexture(screen).setSouthTexture(back);
            if (facing == EnumFacing.SOUTH)
                arr.setSouthTexture(screen).setNorthTexture(back);
            if (facing == EnumFacing.UP)
                arr.setUpTexture(screen).setDownTexture(back);
            if (facing == EnumFacing.DOWN)
                arr.setDownTexture(screen).setUpTexture(back);
            if (facing == EnumFacing.EAST)
                arr.setEastTexture(screen).setWestTexture(back);
            if (facing == EnumFacing.WEST)
                arr.setWestTexture(screen).setEastTexture(back);
            ModelUtil.makeCuboid(format, 0, 0, 0, 1, 1, 1, ModelUtil.FULL_FACES, arr, -1).addToList(quads, side);
        }
        return quads;
    }
}