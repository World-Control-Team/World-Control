package worldcontrolteam.worldcontrol.client.model.infopanel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;
import worldcontrolteam.worldcontrol.blocks.BlockAdvancedInfoPanel;
import worldcontrolteam.worldcontrol.blocks.BlockInfoPanel;
import worldcontrolteam.worldcontrol.client.model.util.ModelUtil;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.xml.soap.Text;
import java.util.*;
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
        return textures;
    }

    static private ArrayList<ResourceLocation> textures_ = new ArrayList<>();

    static {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 16; j++) {
                textures_.add(new ResourceLocation("worldcontrol:blocks/infopanel/on/" + String.valueOf(i) + "/" + String.valueOf(j)));
                textures_.add(new ResourceLocation("worldcontrol:blocks/infopanel/off/" + String.valueOf(i) + "/" + String.valueOf(j)));
            }
        }
    }

    private final ResourceLocation side;
    private final ResourceLocation back;

    public ModelAdvancedInfoPanel(ResourceLocation side, ResourceLocation back) {
        textures = new ArrayList<>();
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

        private final VertexFormat vertexFormat;
        private final ModelAdvancedInfoPanel modelAdvancedInfoPanel;
        private Map<ResourceLocation, TextureAtlasSprite> tas;

        private Map<Pair<BlockAdvancedInfoPanel.State, EnumFacing>, List<BakedQuad>> cache;

        public Baked(VertexFormat vertexFormat, ModelAdvancedInfoPanel modelAdvancedInfoPanel, Function<ResourceLocation, TextureAtlasSprite> function, ResourceLocation side, ResourceLocation back) {

            this.vertexFormat = vertexFormat;
            this.modelAdvancedInfoPanel = modelAdvancedInfoPanel;
            this.tas = new HashMap<>();

            for (ResourceLocation e: modelAdvancedInfoPanel.textures) {
                tas.put(e, function.apply(e));
            }
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState iBlockState, @Nullable EnumFacing enumFacing, long l) {
            BlockAdvancedInfoPanel.State state = new BlockAdvancedInfoPanel.State();
            if (iBlockState instanceof IExtendedBlockState) {
                BlockAdvancedInfoPanel.State state2 = ((IExtendedBlockState) iBlockState).getValue(BlockAdvancedInfoPanel.STATE);
                state = state2 == null? state : state2;
            }
            Pair<BlockAdvancedInfoPanel.State, EnumFacing> theMagicKey = null;
            if (iBlockState != null) {
                theMagicKey = Pair.of(state, iBlockState.getValue(BlockAdvancedInfoPanel.FACING));
            }
            else {
                theMagicKey = Pair.of(state, EnumFacing.UP);
            }

            BlockAdvancedInfoPanel.State finalState = state;
            return cache.computeIfAbsent(theMagicKey, k -> {
                ArrayList<BakedQuad> bbq = new ArrayList<>(6);
                Vec3d[] others = new Vec3d[4];
                addBackFace(k.getRight(), bbq);
                BakedQuad quad = bbq.get(0);
                UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(quad.getFormat());
                final int[] i = {0};
                quad.pipe(new VertexTransformer(builder) {
                    @Override
                    public void put(int element, float... data) {
                        VertexFormatElement.EnumUsage usage = parent.getVertexFormat().getElement(element).getUsage();
                        if(usage == VertexFormatElement.EnumUsage.POSITION && data.length >= 3){
                            others[i[0]++] = new Vec3d(data[0], data[1], data[2]);
                        }
                        super.put(element, data);
                    }
                });
                Vec3d one = getOffset(k.getRight().getOpposite(), false, false, finalState.depth1);
                Vec3d two = getOffset(k.getRight().getOpposite(), false, true, finalState.depth2);
                Vec3d three = getOffset(k.getRight().getOpposite(), true, true, finalState.depth3);
                Vec3d four = getOffset(k.getRight().getOpposite(), true, false, finalState.depth4);
                Vec3d normal = (two.subtract(one).crossProduct(three.subtract(one))).normalize();
                bbq.add(ModelUtil.createQuad(
                        this.vertexFormat,
                        one.x, one.y, one.z,
                        two.x, two.y, two.z,
                        three.x, three.y, three.z,
                        four.x, four.y, four.z,
                        0,
                        0,
                        16,
                        16,
                        normal,
                        getConnectedFace(finalState),
                        0
                ));

                return bbq;
            });
        }

        public TextureAtlasSprite getConnectedFace(BlockAdvancedInfoPanel.State state) {
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
            return this.tas.get(
                    new ResourceLocation("worldcontrol:blocks/infopanel/" + (state.power ? "on" : "off") + "/" + String.valueOf(color) + "/" + String.valueOf(bitfield))
            );
        }

        public void addBackFace(EnumFacing facing, List<BakedQuad> bbq /* get it? listbakedquad? lbq -> bbq? just me? alright... */) {
            bbq.add(ModelUtil.makeCubeFace(this.vertexFormat, facing.getOpposite(), 0, 0, 0, 1, 1, 1, 0, 0, 16, 16, this.tas.get(modelAdvancedInfoPanel.back), 0));
        }

        @SuppressWarnings("Duplicates")
        public Vec3d getOffset(EnumFacing facing, boolean left, boolean right, float depth) {
            Vec3d origin;

            switch (facing) {
                case NORTH:
                    if (left && !right) {
                        origin = new Vec3d(0, 1, 0);
                    } else if (left) {
                        origin = new Vec3d(0, 0, 0);
                    } else if (right) {
                        origin = new Vec3d(1, 0, 0);
                    } else {
                        origin = new Vec3d(1, 1, 0);
                    }
                    break;
                case EAST:
                    if (left && !right) {
                        origin = new Vec3d(1, 1, 0);
                    } else if (left) {
                        origin = new Vec3d(1, 0, 0);
                    } else if (right) {
                        origin = new Vec3d(1, 0, 1);
                    } else {
                        origin = new Vec3d(1, 1, 1);
                    }
                    break;
                case SOUTH:
                    if (left && !right) {
                        origin = new Vec3d(1, 1, 1);
                    } else if (left) {
                        origin = new Vec3d(1, 0, 1);
                    } else if (right) {
                        origin = new Vec3d(0, 0, 1);
                    } else {
                        origin = new Vec3d(0, 1, 1);
                    }
                    break;
                case WEST:
                    if (left && !right) {
                        origin = new Vec3d(0, 1, 1);
                    } else if (left) {
                        origin = new Vec3d(0, 0, 1);
                    } else if (right) {
                        origin = new Vec3d(0, 0, 0);
                    } else {
                        origin = new Vec3d(0, 1, 0);
                    }
                    break;
                case UP:
                    if (left && !right) {
                        origin = new Vec3d(1, 1, 0);
                    } else if (left) {
                        origin = new Vec3d(1, 1, 1);
                    } else if (right) {
                        origin = new Vec3d(0, 1, 1);
                    } else {
                        origin = new Vec3d(0, 1, 0);
                    }
                    break;
                case DOWN:
                    if (left && !right) {
                        origin = new Vec3d(0, 0, 0);
                    } else if (left) {
                        origin = new Vec3d(0, 0, 1);
                    } else if (right) {
                        origin = new Vec3d(1, 0, 1);
                    } else {
                        origin = new Vec3d(1, 0, 0);
                    }
                    break;
                default:
                    return null;
            }
            Vec3d offset = new Vec3d(facing.getOpposite().getDirectionVec());
            offset = offset.scale(depth);
            return origin.add(offset);
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
