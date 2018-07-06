package mod.akrivus.revolution.entity.ai;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mod.akrivus.revolution.data.LearnedData;
import mod.akrivus.revolution.data.Memory;
import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAIRememberTarget extends EntityAITarget {
	public EntityHuman human;
	public EntityAIRememberTarget(EntityHuman human) {
		super(human, true, true);
		this.human = human;
	}
	@Override
	public boolean shouldExecute() {
		if (!this.human.isSleeping() && this.human.isOldEnoughToBreed()) {
			List<EntityLivingBase> list = this.taskOwner.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, this.taskOwner.getEntityBoundingBox().grow(12.0F, 4.0F, 12.0F));
	        Map<UUID, Memory> memories = LearnedData.get(this.taskOwner.world).memories;
			Iterator<UUID> it = memories.keySet().iterator();
			while (it.hasNext()) {
				Memory memory = memories.get(it.next());
				for (int i = 0; i < list.size(); ++i) {
		        	EntityLivingBase entity = list.get(i);
					if (memory.destroy && memory.item == entity.getClass().getSimpleName()) {
		        		this.target = entity;
		        		return true;
		        	}
		        }
			}
		}
		return false;
	}
	@Override
	public void startExecuting() {
        this.human.setAttackTarget(this.target);
        super.startExecuting();
    }
}
