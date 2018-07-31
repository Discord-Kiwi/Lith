package mod.akrivus.revolution.entity.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mod.akrivus.revolution.data.LearnedData;
import mod.akrivus.revolution.data.Memory;
import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityAICraftItems extends EntityAIBase {
    protected EntityHuman human;
    public EntityAICraftItems(EntityHuman human) {
        this.human = human;
        //this.setMutexBits(5);
    }
    @Override
    public boolean shouldExecute() {
    	return !this.human.isSleeping() && this.human.isOldEnoughToBreed() && this.human.ticksExisted % 20 == 0;
    }
    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
    @Override
    public void startExecuting() {
    	List<Item> knowledge = new ArrayList<Item>();
		Map<UUID, Memory> collective = LearnedData.get(this.human.world).memories;
		for (UUID id : this.human.getMemories()) {
			if (collective.get(id).getCraft() != null) {
				knowledge.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(collective.get(id).getCraft().split(";")[0].split("/")[0])));
			}
		}
		List<Item> items = new ArrayList<Item>();
		for (ItemStack stack : this.human.getStackList()) {
			items.add(stack.getItem());
		}
		for (Item item : knowledge) {
			if (!items.contains(item)) {
				if (this.human.canCraft(item).isEmpty()) {
					this.human.craft(item);
				}
				else {
					this.human.groceryList.add(this.human.canCraft(item).getItem());
				}
			}
		}
    }
}