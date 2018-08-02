package mod.akrivus.revolution.entity.ai;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EntityAIHaveIdeas extends EntityAIBase {
    protected EntityHuman human;
    public EntityAIHaveIdeas(EntityHuman human) {
        this.human = human;
        this.setMutexBits(4);
    }
    @Override
    public boolean shouldExecute() {
    	return !this.human.isSleeping() && this.human.isOldEnoughToBreed() && this.human.ticksExisted % 100 == 0;
    }
    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
    @Override
    public void startExecuting() {
    	ItemStack attempt = this.human.canCraft(Item.getItemFromBlock(Blocks.PLANKS));
    	if (attempt.isEmpty()) {
    		attempt = this.human.canCraft(Items.STICK);
    		if (attempt.isEmpty()) {
    			attempt = this.human.canCraft(Items.WOODEN_AXE);
    			if (attempt.isEmpty()) {
    				attempt = this.human.canCraft(Items.WOODEN_HOE);
    				if (attempt.isEmpty()) {
    					attempt = this.human.canCraft(Items.WOODEN_PICKAXE);
    					if (attempt.isEmpty()) {
    						attempt = this.human.canCraft(Items.STONE_SWORD);
    						if (attempt.getItem() == Items.CAKE) {
    							this.human.reverseEngineer(new ItemStack(Items.STONE_SWORD));
    						}
    					}
    					else if (attempt.getItem() == Items.CAKE) {
    						this.human.reverseEngineer(new ItemStack(Items.WOODEN_PICKAXE));
    					}
    					attempt = this.human.canCraft(Items.BREAD);
    					if (attempt.getItem() == Items.CAKE) {
    						this.human.reverseEngineer(new ItemStack(Items.BREAD));
    					}
    				}
    				else if (attempt.getItem() == Items.CAKE) {
    					this.human.reverseEngineer(new ItemStack(Items.WOODEN_HOE));
    				}
    			}
    			else if (attempt.getItem() == Items.CAKE) {
    				this.human.reverseEngineer(new ItemStack(Items.WOODEN_AXE));
    			}
    		}
    		else if (attempt.getItem() == Items.CAKE) {
    			this.human.reverseEngineer(new ItemStack(Items.STICK));
    		}
    	}
    	else if (attempt.getItem() == Items.CAKE) {
    		this.human.reverseEngineer(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS)));
    	}
    }
}