package mod.akrivus.revolution.entity.ai;

import java.util.List;

import mod.akrivus.revolution.entity.EntityFemale;
import mod.akrivus.revolution.entity.EntityMale;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;

public class EntityAIBreedIntra extends EntityAIBase {
    private EntityMale candidate;
    private EntityFemale female;
    private double moveSpeed;
    public EntityAIBreedIntra(EntityFemale female, double speed) {
        this.moveSpeed = speed;
        this.female = female;
        this.setMutexBits(1);
    }
    public boolean shouldExecute() {
        if (this.female.isOldEnoughToBreed() && this.female.isAroused() && this.female.ticksExisted % 100 == 0) {
		    List<EntityMale> list = this.female.world.<EntityMale>getEntitiesWithinAABB(EntityMale.class, this.female.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D));
		    double maxDistance = 0;
		    this.candidate = null;
		    for (EntityMale human : list) {
		        if (human.isOldEnoughToBreed()) {
		        	double dist = this.female.getGeneticDistance(human);
		            if (dist > maxDistance) {
		            	this.candidate = human;
		            	maxDistance = dist;
		            }
		        }
		    }
		    return this.candidate != null;
        }
        return false;
    }
    public void startExecuting() {
    	this.female.getNavigator().tryMoveToEntityLiving(this.candidate, this.moveSpeed);
    }
    public boolean shouldContinueExecuting() {
    	return false;
    }
    public void resetTask() {
        this.candidate = null;
    }
}