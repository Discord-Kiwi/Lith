package mod.akrivus.revolution.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;

public class ItemManMeat extends ItemFood {
	public ItemManMeat(boolean cooked) {
		super(cooked ? 12 : 8, cooked ? 1.2F : 0.8F, true);
		this.setRegistryName(new ResourceLocation("revolution:" + (cooked ? "cooked_" : "") + "man_meat"));
		this.setUnlocalizedName((cooked ? "cooked_" : "") + "man_meat");
		this.setCreativeTab(CreativeTabs.FOOD);
	}
}
