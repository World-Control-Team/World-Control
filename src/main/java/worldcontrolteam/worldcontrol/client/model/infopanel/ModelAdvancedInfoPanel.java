package worldcontrolteam.worldcontrol.client.model.infopanel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import org.apache.commons.lang3.tuple.Pair;
import worldcontrolteam.worldcontrol.client.model.util.ModelUtil;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * File created by mincrmatt12 on 6/18/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public class ModelAdvancedInfoPanel implements IModel {
    private ArrayList<ResourceLocation> textures;

    @Override
    public Collection<ResourceLocation> getTextures() {
        return null;
    }
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

    public ModelAdvancedInfoPanel(ResourceLocation side, ResourceLocation back) {
            textures.addAll(textures_);
            textures.add(side);
            textures.add(back);

            this.side = side;
            this.back = back;
        }

    @Override
    public IBakedModel bake(IModelState iModelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> function) {
        return new Baked(vertexFormat, this, function, side, back);
    }

    public static class Baked implements IBakedModel {

        public Baked(VertexFormat vertexFormat, ModelAdvancedInfoPanel modelAdvancedInfoPanel, Function<ResourceLocation, TextureAtlasSprite> function, ResourceLocation side, ResourceLocation back) {

        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState iBlockState, @Nullable EnumFacing enumFacing, long l) {
            return null;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return null;
        }

        @Override
        public ItemOverrideList getOverrides() {
            return ItemOverrideList.NONE;
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
            if (ModelUtil.BLOCK_TRANSFORMS.containsKey(type)) {
                return Pair.of(this, ModelUtil.BLOCK_TRANSFORMS.get(type).getMatrix());
            } else {
                return ForgeHooksClient.handlePerspective(this, type);
            }
        }
    }
}
