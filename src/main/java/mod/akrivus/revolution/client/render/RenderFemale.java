package mod.akrivus.revolution.client.render;

import mod.akrivus.revolution.client.model.ModelFemale;
import mod.akrivus.revolution.client.render.layers.LayerEyes;
import mod.akrivus.revolution.client.render.layers.LayerHair;
import mod.akrivus.revolution.client.render.layers.LayerSkin;
import mod.akrivus.revolution.entity.EntityFemale;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFemale extends RenderBiped<EntityFemale> {
	private static final ResourceLocation FEMALE_TEXTURE = new ResourceLocation("revolution:textures/entities/female/blank.png");
	public RenderFemale(RenderManager manager) {
		super(manager, new ModelFemale(), 0.25F);
		this.addLayer(new LayerSkin(this));
		this.addLayer(new LayerEyes(this));
		this.addLayer(new LayerHair(this));
	}
	@Override
	public void preRenderCallback(EntityFemale human, float partialTickTime) {
		GlStateManager.scale(human.getSize() * 0.9F, human.getSize() * 0.9F, human.getSize() * 0.9F);
	}
	@Override
	public ResourceLocation getEntityTexture(EntityFemale female) {
		return FEMALE_TEXTURE;
	}
	@Override
	protected void renderEntityName(EntityFemale human, double x, double y, double z, String name, double distanceSq) {
		this.renderLivingLabel(human, human.getTribeName(), x, y+0.25, z, 64);
		this.renderLivingLabel(human, human.getFirstName() , x, y, z, 64);
	}
}
