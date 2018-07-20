package mod.akrivus.revolution.entity.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAISpeak extends EntityAIBase {
    protected EntityHuman entity;
    protected EntityHuman closestEntity;
    protected float maxDistance;
    public EntityAISpeak(EntityHuman entity, float maxDistance) {
        this.entity = entity;
        this.maxDistance = maxDistance;
        this.setMutexBits(7);
    }
    @Override
    public boolean shouldExecute() {
    	if (this.entity.isSleeping()) {
	        if (this.entity.getAttackTarget() == null && this.entity.isOldEnoughToBreed()) {
	            List<EntityHuman> list = this.entity.world.<EntityHuman>getEntitiesWithinAABB(EntityHuman.class, this.entity.getEntityBoundingBox().grow(this.maxDistance / (this.entity.world.isDaytime() ? 1 : 2), 3.0D, this.maxDistance / (this.entity.world.isDaytime() ? 1 : 2)));
	            for (int i = 0; i < list.size(); ++i) {
	            	EntityHuman e = list.get(i);
	            	if (e != this.entity) { 
		            	if (e.canEntityBeSeen(this.entity)) {
		            		e.setSickness(this.entity.getSickness());
		            		if (e.getTribeID().equals(this.entity.getTribeID())) {
			            		for (UUID mem : this.entity.getMemories()) {
			            			if (!e.getMemories().contains(mem)) {
			            				this.closestEntity = e;
			            				break;
			            			}
			            		}
		            		}
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
    	return false;
    }
    @Override
    public void startExecuting() {
        List<UUID> untaughtMemories = new ArrayList<UUID>();
        untaughtMemories.addAll(this.entity.getMemories());
        untaughtMemories.removeAll(this.closestEntity.getMemories());
        for (UUID id : untaughtMemories) {
        	this.closestEntity.learnMemory(id);
        }
    }
    @Override
    public void resetTask() {
        this.closestEntity = null;
    }
}