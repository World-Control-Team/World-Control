package worldcontrolteam.worldcontrol.client.model.infopanel;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import worldcontrolteam.worldcontrol.client.model.base.SimpleBlockModel;
import worldcontrolteam.worldcontrol.client.model.util.TextureArray;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * File created by mincrmatt12 on 6/15/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public class ModelInfoPanel extends SimpleBlockModel {
    static private ImmutableList<ResourceLocation> textures_;

    static {
        ImmutableList.Builder<ResourceLocation> textureBuilder = new ImmutableList.Builder<>();

        textureBuilder.add(new ResourceLocation("worldcontrol:blocks/infopanel/panel_side"));
        textureBuilder.add(new ResourceLocation("worldcontrol:blocks/infopanel/panel_back"));

        for (int i = 0; i < 16; i++ ){
            for (int j = 0; j < 16; j++ ) {
                textureBuilder.add(new ResourceLocation("worldcontrol:blocks/infopanel/on/" + String.valueOf(i) + "/" + String.valueOf(j)));
                textureBuilder.add(new ResourceLocation("worldcontrol:blocks/infopanel/off/" + String.valueOf(i) + "/" + String.valueOf(j)));
            }
        }

        textures_ = textureBuilder.build();
    }
    public ModelInfoPanel() {
        super(textures_);
    }

    @Override
    public IBakedModel bake(IModelState iModelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> function) {
        return new Baked(vertexFormat, this, function);
    }

    public static class Baked extends SimpleBlockModel.Baked {

        private final ResourceLocation side = new ResourceLocation("worldcontrol:blocks/infopanel/panel_side");
        private final ResourceLocation back = new ResourceLocation("worldcontrol:blocks/infopanel/panel_back");

        public Baked(VertexFormat fmt, SimpleBlockModel parent, Function<ResourceLocation, TextureAtlasSprite> func) {
            super(fmt, parent, func);
        }

        @Override
        public TextureArray getTextureArray(@Nullable IBlockState s) {
            return new TextureArray().setDownTexture(this.sprites.get(side))
                    .setParticleTexture(this.sprites.get(side))
                    .setEastTexture(this.sprites.get(side))
                    .setNorthTexture(this.sprites.get(side))
                    .setUpTexture(this.sprites.get(side))
                    .setWestTexture(this.sprites.get(side))
                    .setSouthTexture(this.sprites.get(back));
        }
    }
}
