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
        //this.setMutexBits(5);
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
    	if (this.human.canCraft(Item.getItemFromBlock(Blocks.PLANKS)).isEmpty()) {
    		if (this.human.canCraft(Items.STICK).isEmpty()) {
    			if (this.human.canCraft(Items.WOODEN_AXE).isEmpty()) {
    				if (this.human.canCraft(Items.WOODEN_HOE).isEmpty()) {
    					if (this.human.canCraft(Items.WOODEN_PICKAXE).isEmpty()) {
    						this.human.reverseEngineer(new ItemStack(Items.STONE_SWORD));
    						this.human.reverseEngineer(new ItemStack(Items.STONE_PICKAXE));
    						this.human.reverseEngineer(new ItemStack(Items.STONE_AXE));
    						this.human.reverseEngineer(new ItemStack(Items.STONE_HOE));
    					}
    					else {
    						this.human.reverseEngineer(new ItemStack(Items.WOODEN_PICKAXE));
    					}
    				}
    				else {
    					this.human.reverseEngineer(new ItemStack(Items.WOODEN_HOE));
    				}
    				if (this.human.canCraft(Items.BOW).isEmpty()) {
    					this.human.reverseEngineer(new ItemStack(Items.ARROW));
    				}
    				else {
    					this.human.reverseEngineer(new ItemStack(Items.BOW));
    				}
    			}
    			else {
    				this.human.reverseEngineer(new ItemStack(Items.WOODEN_AXE));
    			}
    		}
    		else {
    			this.human.reverseEngineer(new ItemStack(Items.STICK));
    		}
    	}
    	else {
    		this.human.reverseEngineer(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS)));
    	}
    }
}