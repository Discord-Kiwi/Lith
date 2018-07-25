package mod.akrivus.revolution.item;

import mod.akrivus.revolution.entity.EntityHuman;
import mod.akrivus.revolution.entity.Humans;
import mod.akrivus.revolution.world.WorldGenTribes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpawnWand extends Item {
	public ItemSpawnWand() {
		super();
		this.setRegistryName(new ResourceLocation("revolution:spawn_wand"));
		this.setUnlocalizedName("spawn_wand");
		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxStackSize(1);
	}
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			EntityHuman base = Humans.create(world, player.getPosition());
			for (int count = 0; count < WorldGenTribes.lastDist; ++count) {
				EntityHuman human = Humans.gen(base, EntityHuman.class);
				human.setPosition(player.getPosition().getX() + (world.rand.nextInt(8) - 8), world.getTopSolidOrLiquidBlock(player.getPosition()).getY() + 1, player.getPosition().getZ() + (world.rand.nextInt(8) - 8));
				human.onInitialSpawn(world.getDifficultyForLocation(player.getPosition()), null);
				world.spawnEntity(human);
			}
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		player.swingArm(hand);
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
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
