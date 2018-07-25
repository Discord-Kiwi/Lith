package mod.akrivus.revolution.proxy;

import mod.akrivus.revolution.Revolution;
import mod.akrivus.revolution.client.render.RenderFemale;
import mod.akrivus.revolution.client.render.RenderMale;
import mod.akrivus.revolution.entity.EntityFemale;
import mod.akrivus.revolution.entity.EntityMale;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends ModProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		ModelLoader.setCustomModelResourceLocation(Revolution.HUMAN_FLESH_ITEM, 0, new ModelResourceLocation(Revolution.HUMAN_FLESH_ITEM.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Revolution.SPAWN_WAND_ITEM, 0, new ModelResourceLocation(Revolution.SPAWN_WAND_ITEM.getRegistryName(), "inventory"));
		RenderingRegistry.registerEntityRenderingHandler(EntityFemale.class, RenderFemale::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMale.class, RenderMale::new);
	}
}
