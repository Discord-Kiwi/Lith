package mod.akrivus.revolution.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMutator extends Item {
	public ItemMutator() {
		super();
		this.setRegistryName(new ResourceLocation("revolution:mutator"));
		this.setUnlocalizedName("mutator");
		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxStackSize(1);
	}
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.swingArm(hand);
		return super.onItemRightClick(world, player, hand);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add("Simulates random selection over generations.");
	}
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
	@Override
	@SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }
}
