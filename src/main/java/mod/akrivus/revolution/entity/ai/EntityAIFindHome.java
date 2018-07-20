package mod.akrivus.revolution.entity.ai;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIFindHome extends EntityAIBase {
    protected EntityHuman human;
    protected boolean homeFound;
    protected boolean homeValid;
    protected BlockPos newPos;
    public EntityAIFindHome(EntityHuman human) {
        this.human = human;
        this.setMutexBits(1);
    }
    @Override
    public boolean shouldExecute() {
    	if (this.human.getTribe().isHomeless()) {
    		return true;
    	}
    	return false;
    }
    @Override
    public boolean shouldContinueExecuting() {
        return this.human.getTribe().isHomeless();
    }
    @Override
    public void startExecuting() {
    	for (int x = -8; x < 8; ++x) {
    		for (int y = -1; y < 1; ++y) {
    			for (int z = -8; z < 8; ++z) {
    				BlockPos pos = this.human.getPosition().add(new BlockPos(x, y, z));
    	    		if (this.human.world.isAirBlock(pos.up(2)) && this.human.world.isAirBlock(pos.up(1)) && !this.human.world.isAirBlock(pos)) {
    	    			if (!this.human.world.canSeeSky(pos.up()) && pos.distanceSq(this.human.getTribe().getOldHome()) > 262144) {
    	    				this.homeFound = true;
    	    				this.newPos = pos;
    	    			}
    	    		}
    	    	}
        	}
    	}
    	if (this.homeFound) { 
    		int blocksPassed = 0;
    		for (int x = -8; x < 8; ++x) {
    			for (int z = -8; z < 8; ++z) {
    				BlockPos pos = this.newPos.add(new BlockPos(x, 0, z));
    	    		if (this.human.world.isAirBlock(pos.up(2)) && this.human.world.isAirBlock(pos.up(1)) && !this.human.world.isAirBlock(pos)) {
    	    			++blocksPassed;
    	    		}
    	    	}
        	}
    		this.homeValid = blocksPassed / 64.0F > 0.5F;
    		if (this.homeValid) {
    			this.human.getNavigator().tryMoveToXYZ(this.newPos.getX(), this.newPos.getY(), this.newPos.getZ(), 1.0D);
    			this.human.getTribe().setHome(this.newPos);
    		}
    		else {
    			Vec3d pos = RandomPositionGenerator.findRandomTarget(this.human, 16, 8);
    	    	if (pos != null) {
    	    		this.human.getNavigator().tryMoveToXYZ(pos.x, pos.y, pos.z, 1.0D);
    	    	}
    		}
    	}
    	else {
	    	Vec3d pos = RandomPositionGenerator.findRandomTarget(this.human, 16, 8);
	    	if (pos != null) {
	    		this.human.getNavigator().tryMoveToXYZ(pos.x, pos.y, pos.z, 1.0D);
	    	}
    	}
    }
    @Override
    public void resetTask() {
    	this.human.getNavigator().clearPath();
    	this.homeFound = false;
    	this.homeValid = false;
    }
}