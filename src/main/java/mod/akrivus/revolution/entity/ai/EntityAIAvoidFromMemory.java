package mod.akrivus.revolution.entity.ai;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import mod.akrivus.revolution.data.LearnedData;
import mod.akrivus.revolution.data.Memory;
import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.Vec3d;

public class EntityAIAvoidFromMemory extends EntityAIBase {
    protected EntityHuman human;
    protected Entity target;
    protected float maxDistance;
    protected double speed;
    private Path flightPath;
    public EntityAIAvoidFromMemory(EntityHuman human, float maxDistance, double speed) {
        this.human = human;
        this.maxDistance = maxDistance;
        this.speed = speed;
        this.setMutexBits(1);
    }
    @Override
    public boolean shouldExecute() {
    	if (!this.human.isSleeping()) {
			List<EntityLivingBase> list = this.human.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, this.human.getEntityBoundingBox().grow(12.0F, 4.0F, 12.0F));
	        Map<UUID, Memory> memories = LearnedData.get(this.human.world).memories;
	        for (UUID id : this.human.getMemories()) {
	        	for (int i = 0; i < list.size(); ++i) {
		        	EntityLivingBase entity = list.get(i);
		        	if (entity.getClass().getSimpleName().equals(memories.get(id).getFear())) {
		        		this.target = entity;
		        		Vec3d pos = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.human, 16, 8, new Vec3d(this.target.posX, this.target.posY, this.target.posZ));
		                if (pos == null ||  this.target.getDistanceSq(pos.x, pos.y, pos.z) < this.target.getDistanceSq(this.human)) {
		                    return false;
		                }
		                else {
		                    this.flightPath = this.human.getNavigator().getPathToXYZ(pos.x, pos.y, pos.z);
		                    return this.flightPath != null;
		                }
		        	}
		        }
	        }
		}
    	return false;
    }
    @Override
    public boolean shouldContinueExecuting() {
    	return !this.human.getNavigator().noPath();
    }
    @Override
    public void startExecuting() {
    	this.human.getNavigator().setPath(this.flightPath, this.speed);
    }
    @Override
    public void resetTask() {
        this.target = null;
    }
}