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
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIForage extends EntityAIBase {
	static List<Material> materials = new ArrayList<Material>();
	static {
		materials.add(Material.CACTUS);
		materials.add(Material.CAKE);
		materials.add(Material.GOURD);
		materials.add(Material.GRASS);
		materials.add(Material.LEAVES);
		materials.add(Material.PLANTS);
		materials.add(Material.VINE);
	}
	
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
    	if (!this.human.isSleeping() && (this.human.world.getWorldTime() % 24000) < 12000) {
	        Map<UUID, Memory> memories = LearnedData.get(this.human.world).memories;
	        List<String> blocks = new ArrayList<String>();
	        List<String> walks = new ArrayList<String>();
	        for (UUID id : this.human.getMemories()) {
	        	blocks.add(memories.get(id).getBreak());
	        	walks.add(memories.get(id).getWalk());
	        }
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
	        			if (block != Blocks.AIR && block != Blocks.BEDROCK && !(blocks instanceof BlockLiquid)) {
	        				if (blocks.contains(block.getUnlocalizedName())) {
		        				pos.add(check);
		        			}
		        			else if (block.getHarvestTool(state) != "pickaxe"
		        					|| materials.contains(state.getMaterial())) {
			        			if (!walks.contains(block.getUnlocalizedName())) {
			        				pos.add(check);
			        			}
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