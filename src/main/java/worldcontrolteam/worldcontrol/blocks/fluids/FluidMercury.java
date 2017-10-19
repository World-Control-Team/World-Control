package worldcontrolteam.worldcontrol.blocks.fluids;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidMercury extends Fluid {

    public FluidMercury() {
        super("mercury", new ResourceLocation("worldcontrol:blocks/merucry_still"), new ResourceLocation("worldcontrol:blocks/merucry_flowing"));
        this.setRarity(EnumRarity.RARE);

        FluidRegistry.registerFluid(this);
        FluidRegistry.addBucketForFluid(this);
    }

}
