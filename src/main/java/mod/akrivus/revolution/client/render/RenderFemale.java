package mod.akrivus.revolution.client.render;

import mod.akrivus.revolution.client.model.ModelFemale;
import mod.akrivus.revolution.entity.EntityFemale;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFemale extends RenderBiped<EntityFemale> {
	private static final ResourceLocation FEMALE_TEXTURE = new ResourceLocation("revolution:textures/entities/female/blank.png");
	public RenderFemale(RenderManager manager) {
		super(manager, new ModelFemale(), 0.25F);
	}
	@Override
	public ResourceLocation getEntityTexture(EntityFemale female) {
		return FEMALE_TEXTURE;
	}
}
