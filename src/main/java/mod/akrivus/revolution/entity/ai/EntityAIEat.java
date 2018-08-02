package mod.akrivus.revolution.entity.ai;

import java.util.List;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class EntityAIEat extends EntityAIBase {
    protected EntityHuman entity;
    protected ItemStack stack;
    protected float maxDistance;
    public EntityAIEat(EntityHuman entity, float maxDistance) {
        this.entity = entity;
        this.maxDistance = maxDistance;
        this.setMutexBits(4);
    }
    @Override
    public boolean shouldExecute() {
    	if (!this.entity.isSleeping() && this.entity.getFoodLevels() < 12 && this.entity.ticksExisted % 240 == 0) {
    		if (this.entity.getAttackTarget() == null) {
    			ItemStack foodStack = this.hasFood(this.entity);
    			if (!foodStack.isEmpty()) {
    				this.stack = foodStack;
    				return true;
    			}
    			else {
    				List<EntityHuman> list = this.entity.world.<EntityHuman>getEntitiesWithinAABB(EntityHuman.class, this.entity.getEntityBoundingBox().grow(this.maxDistance / (this.entity.world.isDaytime() ? 1 : 2), 3.0D, this.maxDistance / (this.entity.world.isDaytime() ? 1 : 2)));
		            for (int i = 0; i < list.size(); ++i) {
		            	EntityHuman e = list.get(i);
		            	if (e != this.entity) { 
			            	if (e.canEntityBeSeen(this.entity) && e.getTribeID().equals(this.entity.getTribeID())) {
			            		if (!e.isSleeping() && e.getFoodLevels() > 12) {
			            			ItemStack stack = this.hasFood(e);
			            			if (!stack.isEmpty()) {
			            				this.entity.getNavigator().tryMoveToEntityLiving(e, 1.0D);
			            				this.stack = stack;
			            				return true;
			            			}
			            		}
			            	}
		            	}
		            }
    			}
	        }
    	}
    	return false;
    }
    @Override
    public void startExecuting() {
    	ItemFood item = (ItemFood)(this.stack.getItem());
        this.entity.setFoodLevels(this.entity.getFoodLevels() + item.getHealAmount(this.stack));
        this.entity.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
        this.stack.shrink(1);
    }
    @Override
    public boolean shouldContinueExecuting() {
    	return false;
    }
    @Override
    public void resetTask() {
        this.stack = null;
    }
    public ItemStack hasFood(EntityHuman human) {
    	InventoryBasic inventory = human.getInventory();
    	for (int i = 0; i < inventory.getSizeInventory(); ++i) {
    		if (inventory.getStackInSlot(i).getItem() instanceof ItemFood) {
    			return inventory.getStackInSlot(i);
    		}
    	}
    	return ItemStack.EMPTY;
    }
}