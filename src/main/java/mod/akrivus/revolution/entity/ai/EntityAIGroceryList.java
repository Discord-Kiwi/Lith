package mod.akrivus.revolution.entity.ai;

import java.util.ArrayList;
import java.util.List;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIGroceryList extends EntityAIBase {
    protected EntityHuman human;
    protected BlockPos lastPos;
    protected BlockPos home;
    protected boolean wandering;
    protected int delay = 0;
    public EntityAIGroceryList(EntityHuman human) {
        this.human = human;
        this.setMutexBits(1);
    }
    @Override
    public boolean shouldExecute() {
    	if (this.delay > 50) {
	    	if (!this.human.isSleeping() && (this.human.world.getWorldTime() % 24000) < 12000) {
		        List<BlockPos> pos = new ArrayList<BlockPos>();
		        double maxDistance = 262144;
		        this.lastPos = this.home;
		        for (int x = -8; x < 8; ++x) {
		        	for (int y = -4; y < 4; ++y) {
		        		for (int z = -8; z < 8; ++z) {
		        			if (x == 0 && y < 0 && z == 0) {
		        				continue;
		        			}
		        			BlockPos check = this.human.getPosition().add(x, y, z);
		        			IBlockState state = this.human.world.getBlockState(check);
		        			Block block = state.getBlock();
		        			// TODO: Fix.
		        			for (Item item : this.human.groceryList) {
		        				try {
			        				if (block.getUnlocalizedName().equals(Block.getBlockFromItem(item).getUnlocalizedName())
			        				 || block.getItemDropped(state, this.human.world.rand, 1).getUnlocalizedName().equals(item.getUnlocalizedName())) {
			        					pos.add(check);
			        				}
		        				}
		        				catch (Exception e) {
		        					continue;
		        				}
		        			}
		    	        }
			        }
		        }
		        if (pos.isEmpty()) {
		        	Vec3d vec = RandomPositionGenerator.findRandomTarget(this.human, 16, 4);
		        	if (vec == null) {
		        		return false;
		        	}
		        	this.home = new BlockPos(vec);
		        	this.wandering = true;
		        }
		        else {
			        for (BlockPos loc : pos) {
			        	double dist = this.human.getPosition().distanceSq(loc);
			        	if (loc != this.lastPos) {
				        	if (dist < maxDistance) {
				        		maxDistance = this.human.getPosition().distanceSq(loc);
				        		IBlockState state = this.human.world.getBlockState(loc);
				        		String tool = state.getBlock().getHarvestTool(state);
				        		if (tool != null) {
					        		List<ItemStack> stacks = this.human.getStackList();
					        		for (ItemStack stack : stacks) {
					        			if (stack.canHarvestBlock(state)) {
					        				this.human.setHeldItem(EnumHand.MAIN_HAND, stack.copy());
					        			}
					        		}
				        		}
				        		else {
				        			this.human.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
				        		}
				        		this.home = loc;
				        	}
			        	}
			        }
		        }
	    	}
    	}
    	++this.delay;
    	return this.home != this.lastPos;
    }
    @Override
    public boolean shouldContinueExecuting() {
    	return (!this.human.world.isAirBlock(this.home) || this.wandering) && (this.human.getTribe().isHomeless() || this.human.getDistanceSq(this.human.getTribe().getHome()) < 262144);
    }
    @Override
    public void startExecuting() {
    	this.human.getNavigator().tryMoveToXYZ(this.home.getX(), this.home.getY(), this.home.getZ(), 0.6D);
    }
    @Override
    public void updateTask() {
    	if (this.human.getNavigator().noPath()) {
    		if (!this.wandering) {
    			if (this.human.getDistanceSq(this.home) < 16.0D) {
    				Block block = this.human.world.getBlockState(this.home).getBlock();
		    		this.human.world.destroyBlock(this.home, true);
		    		this.human.swingArm(EnumHand.MAIN_HAND);
		    		this.human.addMemory("WALK", block);
		    		this.human.lastBlockBreak = block;
		    		this.human.resetBlockTicks();
		        	this.delay = 0;
    			}
    			else {
    		    	this.human.getNavigator().tryMoveToXYZ(this.home.getX(), this.home.getY(), this.home.getZ(), 0.6D);
    			}
    		}
    	}
    }
    @Override
    public void resetTask() {
    	this.wandering = false;
    	this.home = null;
    }
}