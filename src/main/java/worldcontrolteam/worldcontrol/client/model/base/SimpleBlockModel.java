package worldcontrolteam.worldcontrol.client.model.base;

import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import worldcontrolteam.worldcontrol.client.model.util.TextureArray;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.xml.soap.Text;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * File created by mincrmatt12 on 6/15/2018.
 * Originally written for WorldControl.
 * <p>
 * See LICENSE.txt for license information.
 */
public abstract class SimpleBlockModel<T extends SimpleBlockModel.Baked> implements IModel {
    private ImmutableList<ResourceLocation> textures;

    public SimpleBlockModel(ImmutableList<ResourceLocation> textures) {
        this.textures = textures;
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return textures;
    }

    public abstract static class Baked implements IBakedModel {
        private final VertexFormat fmt;
        private Map<TextureArray, List<BakedQuad>> cache = new HashMap<>();
        private Map<IBlockState, TextureArray> cache2 = new HashMap<>();

        protected Map<ResourceLocation, TextureAtlasSprite> sprites = new HashMap<>();

        public Baked(VertexFormat fmt, SimpleBlockModel parent, Function<ResourceLocation, TextureAtlasSprite> func) {
            this.fmt = fmt;
            for (Object i_ : parent.getTextures()) {
                ResourceLocation i = (ResourceLocation) i_;
                this.sprites.put(i, func.apply(i));
            }
        }

        public abstract TextureArray getTextureArray(@Nullable IBlockState s);

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState iBlockState, @Nullable EnumFacing enumFacing, long l) {
            return cache.computeIfAbsent(
                    cache2.computeIfAbsent(
                            iBlockState,
                            this::getTextureArray
                    ),
                    textureArray -> ModelUtil.makeCuboid(fmt, 0, 0, 0, 1, 1, 1, null, textureArray, 0)
            );
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
            return this.getTextureArray(null).getParticleTexture();
        }

        @Override
        public ItemOverrideList getOverrides() {
            return ItemOverrideList.NONE;
        }

        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
            if (ModelUtil.BLOCK_TRANSFORMS.containsKey(type)) {
                return Pair.of(this, ModelUtil.BLOCK_TRANSFORMS.get(type).getMatrix());
            } else {
                return ForgeHooksClient.handlePerspective(this, type);
            }
        }
    }
}
