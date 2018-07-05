package mod.akrivus.revolution.client.render;

import mod.akrivus.revolution.client.model.ModelMale;
import mod.akrivus.revolution.entity.EntityMale;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMale extends RenderBiped<EntityMale> {
	private static final ResourceLocation MALE_TEXTURE = new ResourceLocation("revolution:textures/entities/male/blank.png");
	public RenderMale(RenderManager manager) {
		super(manager, new ModelMale(), 0.25F);
	}
	@Override
	public ResourceLocation getEntityTexture(EntityMale male) {
		return MALE_TEXTURE;
	}
}
