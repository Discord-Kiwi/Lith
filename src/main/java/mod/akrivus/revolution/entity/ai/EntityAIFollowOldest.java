package mod.akrivus.revolution.entity.ai;

import java.util.List;

import mod.akrivus.revolution.entity.EntityFemale;
import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFollowOldest extends EntityAIBase {
    private EntityHuman elder;
    private EntityHuman follower;
    private double moveSpeed;
    private int delayCounter;
    public EntityAIFollowOldest(EntityHuman follower, double speed) {
        this.moveSpeed = speed;
        this.follower = follower;
    }
    public boolean shouldExecute() {
        if (this.follower.getTribe().isHomeless()) {
            List<EntityHuman> list = this.follower.world.<EntityHuman>getEntitiesWithinAABB(EntityHuman.class, this.follower.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D));
            double maxDistance = Double.MAX_VALUE;
            this.elder = null;
            for (EntityHuman human : list) {
                if (human.isOldEnoughToBreed() && human.getTribeID().equals(this.follower.getTribeID())) {
                    double dist = this.follower.getDistanceSq(human);
                    if (dist <= maxDistance && human.getAge() > this.follower.getAge()) {
                    	this.elder = human;
                    	maxDistance = dist;
                    }
                }
            }
            if (this.elder == null) {
                return false;
            }
            else if (maxDistance < 100.0D) {
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }
    public boolean shouldContinueExecuting() {
        if (!this.elder.isEntityAlive() || !this.follower.getTribe().isHomeless()) {
            return false;
        }
        else {
            double dist = this.follower.getDistanceSq(this.elder);
            return dist >= 100.0D && dist <= 256.0D;
        }
    }
    public void startExecuting() {
        this.delayCounter = 0;
    }
    public void resetTask() {
        this.elder = null;
    }
    public void updateTask() {
        if (--this.delayCounter <= 0) {
            this.follower.getNavigator().tryMoveToEntityLiving(this.elder, this.moveSpeed);
            this.delayCounter = 10;
        }
    }
}