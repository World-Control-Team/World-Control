package xbony2.thechiefssurprise.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xbony2.thechiefssurprise.TheChiefsSurprise;

public class CSFood extends ItemFood {
	
	public CSFood(String name, int amount) {
		this(name, amount, 0.6F, false);
	}
	
	public CSFood(String name, int amount, float saturation) {
		this(name, amount, saturation, false);
	}
	
	public CSFood(String name, int amount, boolean isWolfFood) {
		this(name, amount, 0.6F, isWolfFood);
	}

	public CSFood(String name, int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
		
		this.setCreativeTab(TheChiefsSurprise.tab);
		this.setRegistryName("tcs." + name);
		this.setUnlocalizedName("tcs." + name);
		
		GameRegistry.register(this);
		
		if(TheChiefsSurprise.side == Side.CLIENT) ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("thechiefssurprise:" + name, "inventory"));
	}
}
