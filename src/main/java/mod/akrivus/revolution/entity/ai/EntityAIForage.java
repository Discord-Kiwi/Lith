package mod.akrivus.revolution.entity.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mod.akrivus.revolution.data.LearnedData;
import mod.akrivus.revolution.data.Memory;
import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIForage extends EntityAIBase {
    protected EntityHuman human;
    protected BlockPos lastPos;
    protected BlockPos home;
    protected boolean wandering;
    public EntityAIForage(EntityHuman human) {
        this.human = human;
        this.setMutexBits(1);
    }
    @Override
    public boolean shouldExecute() {
    	if (!this.human.isSleeping() && (this.human.world.getWorldTime() % 24000) < 6000) {
	        Map<UUID, Memory> memories = LearnedData.get(this.human.world).memories;
	        List<String> blocks = new ArrayList<String>();
	        for (UUID id : this.human.getMemories()) {
	        	blocks.add(memories.get(id).getBreak());
	        }
	        List<BlockPos> pos = new ArrayList<BlockPos>();
	        double maxDistance = 262144;
	        this.lastPos = this.home;
	        for (int x = -8; x < 8; ++x) {
	        	for (int y = -4; y < 4; ++y) {
	        		for (int z = -8; z < 8; ++z) {
	        			BlockPos check = this.human.getPosition().add(x, y, z);
	        			Block block = this.human.world.getBlockState(check).getBlock();
	        			if (blocks.contains(block.getUnlocalizedName())) {
	        				pos.add(check);
	        			}
	        			else if (block != Blocks.AIR && !(block instanceof BlockLiquid)) {
	        				if (block.getHarvestTool(this.human.world.getBlockState(check)) == null) {
	        					pos.add(check);
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
			        	if (dist > 2.0F && dist < maxDistance) {
			        		maxDistance = this.human.getPosition().distanceSq(loc);
			        		this.home = loc;
			        	}
		        	}
		        }
	        }
    	}
    	return this.home != this.lastPos;
    }
    @Override
    public boolean shouldContinueExecuting() {
    	return (!this.human.world.isAirBlock(this.home) || this.wandering) && (this.human.getTribe().isHomeless() || this.human.getDistanceSq(this.human.getTribe().getHome()) < 262144);
    }
    @Override
    public void startExecuting() {
    	this.human.getNavigator().tryMoveToXYZ(this.home.getX(), this.home.getY(), this.home.getZ(), 1.0D);
    }
    @Override
    public void updateTask() {
    	if (this.human.getNavigator().noPath()) {
    		if (!this.wandering) {
    			if (this.human.getDistanceSq(this.home) < 2.0D) {
		    		Item item = this.human.world.getBlockState(this.home).getBlock().getItemDropped(this.human.world.getBlockState(this.home), this.human.world.rand, 1);
		    		if (item instanceof ItemFood) {
		    			boolean learned = true;
						Map<UUID, Memory> collective = LearnedData.get(human.world).memories;
						for (UUID id : this.human.getMemories()) {
							if (item.getUnlocalizedName().equals(collective.get(id).getBreak())) {
								learned = false;
								break;
							}
						}
						if (learned) {
							this.human.addMemory("BREAK", item);
						}
		    		}
		    		this.human.world.destroyBlock(this.home, true);
    			}
    			else {
    		    	this.human.getNavigator().tryMoveToXYZ(this.home.getX(), this.home.getY(), this.home.getZ(), 1.0D);
    			}
    		}
    	}
    }
    @Override
    public void resetTask() {
    	this.human.getNavigator().clearPath();
    	this.wandering = false;
    	this.home = null;
    }
}