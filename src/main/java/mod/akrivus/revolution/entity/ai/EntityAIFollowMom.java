package mod.akrivus.revolution.entity.ai;

import java.util.List;

import mod.akrivus.revolution.entity.EntityFemale;
import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFollowMom extends EntityAIBase {
    private EntityHuman mother;
    private EntityHuman child;
    private double moveSpeed;
    private int delayCounter;
    public EntityAIFollowMom(EntityHuman child, double speed) {
        this.moveSpeed = speed;
        this.child = child;
    }
    public boolean shouldExecute() {
        if (this.child.isOldEnoughToBreed()) {
            return false;
        }
        else {
            List<EntityFemale> list = this.child.world.<EntityFemale>getEntitiesWithinAABB(EntityFemale.class, this.child.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D));
            double maxDistance = Double.MAX_VALUE;
            this.mother = null;
            for (EntityFemale human : list) {
                if (human.isOldEnoughToBreed() && human.getTribeID().equals(this.child.getTribeID())) {
                    double dist = this.child.getDistanceSq(human);
                    if (dist <= maxDistance) {
                    	this.mother = human;
                    	maxDistance = dist;
                    }
                }
            }
            if (this.mother == null) {
                return false;
            }
            else if (maxDistance < 9.0D) {
                return false;
            }
            else {
                return true;
            }
        }
    }
    public boolean shouldContinueExecuting() {
        if (this.child.isOldEnoughToBreed()) {
            return false;
        }
        else if (!this.mother.isEntityAlive()) {
            return false;
        }
        else {
            double dist = this.child.getDistanceSq(this.mother);
            return dist >= 9.0D && dist <= 256.0D;
        }
    }
    public void startExecuting() {
        this.delayCounter = 0;
    }
    public void resetTask() {
        this.mother = null;
    }
    public void updateTask() {
        if (--this.delayCounter <= 0) {
            this.child.getNavigator().tryMoveToEntityLiving(this.mother, this.moveSpeed);
            this.mother.setIsFertile(false);
            this.delayCounter = 10;
        }
    }
}