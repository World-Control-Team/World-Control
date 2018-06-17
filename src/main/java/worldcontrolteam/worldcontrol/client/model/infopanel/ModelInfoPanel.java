package worldcontrolteam.worldcontrol.client.model.infopanel;

import com.google.common.collect.ImmutableList;
import javafx.util.Pair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import worldcontrolteam.worldcontrol.blocks.BlockBasicRotate;
import worldcontrolteam.worldcontrol.blocks.BlockInfoPanel;
import worldcontrolteam.worldcontrol.client.model.base.SimpleBlockModel;
import worldcontrolteam.worldcontrol.client.model.util.TextureArray;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * File created by mincrmatt12 on 6/15/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public class ModelInfoPanel extends SimpleBlockModel {
    static private ArrayList<ResourceLocation> textures_ = new ArrayList<>();

    static {
        for (int i = 0; i < 15; i++ ){
            for (int j = 0; j < 16; j++ ) {
                textures_.add(new ResourceLocation("worldcontrol:blocks/infopanel/on/" + String.valueOf(i) + "/" + String.valueOf(j)));
                textures_.add(new ResourceLocation("worldcontrol:blocks/infopanel/off/" + String.valueOf(i) + "/" + String.valueOf(j)));
            }
        }
    }

    private final ResourceLocation side;
    private final ResourceLocation back;

    public ModelInfoPanel(ResourceLocation side, ResourceLocation back) {
        super(textures_);
        textures.add(side);
        textures.add(back);

        this.side = side;
        this.back = back;
    }

    @Override
    public IBakedModel bake(IModelState iModelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> function) {
        return new Baked(vertexFormat, this, function, side, back);
    }

    public static class Baked extends SimpleBlockModel.Baked {
        private Map<Pair<BlockInfoPanel.InfoPanelState, Integer>, TextureArray> cache = new HashMap<>();

        private final ResourceLocation side;
        private final ResourceLocation back;

        public Baked(VertexFormat fmt, SimpleBlockModel parent, Function<ResourceLocation, TextureAtlasSprite> func, ResourceLocation side, ResourceLocation back) {
            super(fmt, parent, func);
            this.side = side;
            this.back = back;
        }

        public TextureAtlasSprite getConnectedFace(BlockInfoPanel.InfoPanelState state) {
            // 1 - left
            // 2 - right
            // 4 - up
            // 8 - down
            // true = noconnect

            int color = state.color;
            int bitfield = 0;
            bitfield += (state.left ? 0 : 1);
            bitfield += (state.right ? 0 : 2);
            bitfield += (state.up ? 0 : 4);
            bitfield += (state.down ? 0 : 8);
            return this.sprites.get(
                    new ResourceLocation("worldcontrol:blocks/infopanel/" + (state.power ? "on" : "off") + "/" + String.valueOf(color) + "/" + String.valueOf(bitfield))
            );
        }

        @Override
        public TextureArray getTextureArray(@Nullable IBlockState s) {
            BlockInfoPanel.InfoPanelState state = new BlockInfoPanel.InfoPanelState();
            EnumFacing f = (EnumFacing) (s != null ? s.getProperties().get(BlockBasicRotate.FACING) : EnumFacing.NORTH);
            if (s instanceof IExtendedBlockState) {
                state = (BlockInfoPanel.InfoPanelState) ((IExtendedBlockState) s).getUnlistedProperties().get(BlockInfoPanel.STATE).get();
            }
            Pair<BlockInfoPanel.InfoPanelState, Integer> key = new Pair<>(state, f.getIndex());
            if (cache.containsKey(key)) return cache.get(key);
            TextureAtlasSprite sideSprite = this.sprites.get(side);
            TextureArray textureArray = new TextureArray().setDownTexture(sideSprite)
                    .setParticleTexture(sideSprite)
                    .setEastTexture(sideSprite)
                    .setNorthTexture(sideSprite)
                    .setUpTexture(sideSprite)
                    .setWestTexture(sideSprite)
                    .setSouthTexture(sideSprite);
            textureArray.setTexture(f, getConnectedFace(state));
            textureArray.setTexture(f.getOpposite(), this.sprites.get(back));
            cache.put(key, textureArray);
            return textureArray;
        }
    }
}
