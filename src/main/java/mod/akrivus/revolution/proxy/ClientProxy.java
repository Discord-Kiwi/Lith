package mod.akrivus.revolution.proxy;

import mod.akrivus.revolution.client.render.RenderFemale;
import mod.akrivus.revolution.client.render.RenderMale;
import mod.akrivus.revolution.entity.EntityFemale;
import mod.akrivus.revolution.entity.EntityMale;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends ModProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityFemale.class, RenderFemale::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMale.class, RenderMale::new);
		super.preInit(event);
	}
}
