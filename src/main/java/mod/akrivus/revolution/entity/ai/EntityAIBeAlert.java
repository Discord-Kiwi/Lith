package mod.akrivus.revolution.entity.ai;

import java.util.List;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIBeAlert extends EntityAIBase {
    protected EntityHuman entity;
    protected Entity closestEntity;
    protected float maxDistance;
    private int lookTime;
    public EntityAIBeAlert(EntityHuman entity, float maxDistance) {
        this.entity = entity;
        this.maxDistance = maxDistance;
        this.setMutexBits(1);
    }
    @Override
    public boolean shouldExecute() {
    	if (!this.entity.isSleeping()) {
	        if (this.entity.getAttackTarget() == null) {
	            List<EntityLivingBase> list = this.entity.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, this.entity.getEntityBoundingBox().grow(this.maxDistance / (this.entity.world.isDaytime() ? 1 : 2), 3.0D, this.maxDistance / (this.entity.world.isDaytime() ? 1 : 2)));
	            double highestMotion = 0;
	            for (int i = 0; i < list.size(); ++i) {
	            	EntityLivingBase e = list.get(i);
	            	if (e != this.entity) { 
		            	if (e.canEntityBeSeen(this.entity) && Math.abs(e.motionX) + Math.abs(e.motionY) + Math.abs(e.motionZ) > highestMotion) {
		            		highestMotion = Math.abs(e.motionX) + Math.abs(e.motionY) + Math.abs(e.motionZ);
		            		this.closestEntity = e;
		            	}
	            	}
	            }
	            return this.closestEntity != null;
	        }
    	}
    	return false;
    }
    @Override
    public boolean shouldContinueExecuting() {
        if (!this.closestEntity.isEntityAlive()) {
            return false;
        }
        else if (this.entity.getDistanceSq(this.closestEntity) > Math.pow(this.maxDistance, 2)) {
            return false;
        }
        else{
            return this.lookTime > 0;
        }
    }
    @Override
    public void startExecuting() {
        this.lookTime = 40 + this.entity.getRNG().nextInt(40);
    }
    @Override
    public void resetTask() {
        this.closestEntity = null;
    }
    @Override
    public void updateTask() {
        this.entity.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + this.closestEntity.getEyeHeight(), this.closestEntity.posZ, this.entity.getHorizontalFaceSpeed(), this.entity.getVerticalFaceSpeed());
        --this.lookTime;
    }
}