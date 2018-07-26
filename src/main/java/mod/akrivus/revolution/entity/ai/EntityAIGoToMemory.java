package mod.akrivus.revolution.entity.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mod.akrivus.revolution.data.LearnedData;
import mod.akrivus.revolution.data.Memory;
import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIGoToMemory extends EntityAIBase {
    protected EntityHuman human;
    protected BlockPos lastPos;
    protected BlockPos home;
    public EntityAIGoToMemory(EntityHuman human) {
        this.human = human;
        this.setMutexBits(1);
    }
    @Override
    public boolean shouldExecute() {
    	if (!this.human.isSleeping() && (this.human.world.getWorldTime() % 24000) < 8000) {
	        Map<UUID, Memory> memories = LearnedData.get(this.human.world).memories;
	        List<BlockPos> pos = new ArrayList<BlockPos>();
	        for (UUID id : this.human.getMemories()) {
	        	if (memories.get(id).getGoto().getY() > 0) {
	        		pos.add(memories.get(id).getGoto());
	        	}
	        }
	        double maxDistance = 262144;
	        this.lastPos = this.home;
	        for (BlockPos loc : pos) {
	        	double dist = this.human.getPosition().distanceSq(loc);
	        	if (loc != this.lastPos) {
		        	if (dist > Math.min(256.0D, Math.pow(2.0D, pos.size())) && dist < maxDistance) {
		        		maxDistance = this.human.getPosition().distanceSq(loc);
		        		this.home = loc;
		        	}
	        	}
	        }
    	}
    	return this.home != this.lastPos;
    }
    @Override
    public boolean shouldContinueExecuting() {
    	return this.human.getDistanceSq(this.home) > 1.0F || !this.human.getNavigator().noPath();
    }
    @Override
    public void startExecuting() {
    	if (this.human.getDistanceSq(this.home) > 256) {
    		Vec3d pos = RandomPositionGenerator.findRandomTargetBlockTowards(this.human, 16, 4, new Vec3d(this.home.getX(), this.home.getY(), this.home.getZ()));
            if (pos != null) {
                this.human.getNavigator().tryMoveToXYZ(pos.x, pos.y, pos.z, 1.0D);
                this.lastPos = null;
            }
    	}
    	else {
    		this.human.getNavigator().tryMoveToXYZ(this.home.getX(), this.home.getY(), this.home.getZ(), 1.0D);
    	}
    }
    @Override
    public void resetTask() {
    	this.home = null;
    }
}