package mod.akrivus.revolution.entity.ai;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAITillFarmland extends EntityAIMoveToBlock {
	private final EntityHuman human;
	private final World world;
	private int delay = 0;
	public EntityAITillFarmland(EntityHuman humanIn, double speedIn) {
		super(humanIn, speedIn, 16);
		this.human = humanIn;
		this.world = humanIn.world;
	}
	public boolean shouldExecute() {
		if (this.human.getHeldItemMainhand().getItem() instanceof ItemHoe) {
			if (delay > 20 + this.human.getRNG().nextInt(20)) {
				this.runDelay = 0;
				return super.shouldExecute() && this.hasSeeds();
			}
			else {
				++this.delay;
			}
		}
		return false;
	}
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && this.human.getHeldItemMainhand().getItem() instanceof ItemHoe && !this.human.getNavigator().noPath() && this.hasSeeds();
	}
	public void startExecuting() {
		super.startExecuting();
	}
	public void resetTask() {
		super.resetTask();
	}
	public void updateTask() {
		super.updateTask();
		this.human.getLookHelper().setLookPosition((double) this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double) this.destinationBlock.getZ() + 0.5D, 10.0F, (float) this.human.getVerticalFaceSpeed());
		if (this.getIsAboveDestination()) {
            this.human.world.setBlockState(this.destinationBlock, Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7));
            this.world.playSound(null, this.human.getPosition(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	protected boolean shouldMoveTo(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		if ((block == Blocks.DIRT || block == Blocks.GRASS) && this.hasWater(world, pos)) {
			return true;
		}
		return false;
	}
	private boolean hasWater(World worldIn, BlockPos pos) {
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
            if (worldIn.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.WATER) {
                return worldIn.isAirBlock(pos.up());
            }
        }
        return false;
    }
	private boolean hasSeeds() {
		InventoryBasic inventory = this.human.getInventory();
		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			Item item = inventory.getStackInSlot(i).getItem();
			if (item instanceof ItemSeeds || item instanceof ItemSeedFood) {
				return true;
			}
		}
		return false;
	}
}