package mod.akrivus.revolution.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;

public class ItemHumanFlesh extends ItemFood {
	public ItemHumanFlesh() {
		super(8, 20.4F, true);
		this.setRegistryName(new ResourceLocation("revolution:human_flesh"));
		this.setUnlocalizedName("human_flesh");
		this.setCreativeTab(CreativeTabs.FOOD);
	}
}
