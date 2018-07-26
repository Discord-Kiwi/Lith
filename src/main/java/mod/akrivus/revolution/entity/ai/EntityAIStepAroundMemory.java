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

public class EntityAIStepAroundMemory extends EntityAIBase {
    protected EntityHuman human;
    protected BlockPos home;
    public EntityAIStepAroundMemory(EntityHuman human) {
        this.human = human;
        this.setMutexBits(1);
    }
    @Override
    public boolean shouldExecute() {
    	if (!this.human.isSleeping()) {
	        Map<UUID, Memory> memories = LearnedData.get(this.human.world).memories;
	        List<BlockPos> pos = new ArrayList<BlockPos>();
	        for (UUID id : this.human.getMemories()) {
	        	if (memories.get(id).getAvoid().getY() > 0) {
	        		pos.add(memories.get(id).getAvoid());
	        	}
	        }
	        double maxDistance = 256;
	        for (BlockPos loc : pos) {
	        	double dist = this.human.getPosition().distanceSq(loc);
	        	if (dist < maxDistance) {
	        		maxDistance = this.human.getPosition().distanceSq(loc);
	        		this.home = loc;
	        	}
	        }
    	}
    	return this.home != null;
    }
    @Override
    public void startExecuting() {
		Vec3d pos = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.human, 16, 4, new Vec3d(this.home.getX(), this.home.getY(), this.home.getZ()));
        if (pos != null) {
            this.human.getNavigator().tryMoveToXYZ(pos.x, pos.y, pos.z, 1.0D);
        }
    }
    @Override
    public boolean shouldContinueExecuting() {
    	return this.human.getDistanceSq(this.home) > 256.0F || !this.human.getNavigator().noPath();
    }
    @Override
    public void resetTask() {
        this.home = null;
    }
}