package worldcontrolteam.worldcontrol.client.model.base;

import com.google.common.collect.Lists;
import mcp.MethodsReturnNonnullByDefault;
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
import net.minecraftforge.common.model.IModelState;
import org.apache.commons.lang3.tuple.Pair;
import worldcontrolteam.worldcontrol.utils.Platform;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Originally from CLib
 *
 * @author Coded
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class BakedModelBase implements IBakedModel {
    public Function<ResourceLocation, TextureAtlasSprite> getter;
    public VertexFormat format;
    public TextureArray textureArray;
    private Map<Pair<IBlockState, EnumFacing>, List<BakedQuad>> quadCache = new HashMap<>();

    public BakedModelBase(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ModelBase model) {
        this.getter = bakedTextureGetter;
        this.format = format;
        textureArray = new TextureArray(
                this.getter.apply(model.textures.get("particle")),
                this.getter.apply(model.textures.get("north")),
                this.getter.apply(model.textures.get("south")),
                this.getter.apply(model.textures.get("up")),
                this.getter.apply(model.textures.get("down")),
                this.getter.apply(model.textures.get("east")),
                this.getter.apply(model.textures.get("west"))
        );
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (Platform.disableCache()) {
            List<BakedQuad> quads = buildQuads(state, side, rand);
            quadCache.put(Pair.of(state, side), quads);
            return quads;
        }
        if (quadCache.containsKey(Pair.of(state, side))) {
            return Lists.newArrayList(quadCache.get(Pair.of(state, side)));
        } else {
            List<BakedQuad> quads = buildQuads(state, side, rand);
            quadCache.put(Pair.of(state, side), quads);
            return quads;
        }
    }

    public abstract List<BakedQuad> buildQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand);

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.textureArray.getParticleTexture();
    }

    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(Collections.emptyList());
    }

    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
        if (ModelUtil.BLOCK_TRANSFORMS.containsKey(type)) {
            return Pair.of(this, ModelUtil.BLOCK_TRANSFORMS.get(type).getMatrix());
        } else {
            return ForgeHooksClient.handlePerspective(this, type);
        }
    }
}
