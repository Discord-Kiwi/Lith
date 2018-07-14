package mod.akrivus.revolution.entity.ai;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAISleep extends EntityAIBase {
	protected EntityHuman human;
    public EntityAISleep(EntityHuman human) {
        this.human = human;
        this.setMutexBits(7);
    }
    @Override
    public boolean shouldExecute() {
    	if (!this.human.getTribe().isHomeless() && this.human.getDistanceSq(this.human.getTribe().getHome()) < 16) {
    		if ((this.human.world.getWorldTime() % 24000) > 14000 && this.human.getRevengeTarget() == null) {
    			return true;
    		}
    	}
    	return false;
    }
    @Override
    public boolean shouldContinueExecuting() {
    	return (this.human.world.getWorldTime() % 24000) > 14000 && this.human.getRevengeTarget() == null;
    }
    @Override
    public void startExecuting() {
    	System.out.println(this.getClass().getSimpleName());
    	this.human.getLookHelper().setLookPosition(this.human.posX, this.human.posY - 1, this.human.posZ, 30.0F, 30.0F);
    	this.human.setIsSleeping(true);
    }
    @Override
    public void resetTask() {
    	this.human.setIsSleeping(false);
    }
}
