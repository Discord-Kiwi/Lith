package mod.akrivus.revolution.client.render;

import mod.akrivus.revolution.client.model.ModelMale;
import mod.akrivus.revolution.client.render.layers.LayerEyes;
import mod.akrivus.revolution.client.render.layers.LayerHair;
import mod.akrivus.revolution.client.render.layers.LayerSkin;
import mod.akrivus.revolution.entity.EntityMale;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;

public class RenderMale extends RenderBiped<EntityMale> {
	private static final ResourceLocation MALE_TEXTURE = new ResourceLocation("revolution:textures/entities/male/blank.png");
	public RenderMale(RenderManager manager) {
		super(manager, new ModelMale(), 0.25F);
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerSkin(this));
		this.addLayer(new LayerEyes(this));
		this.addLayer(new LayerHair(this));
	}
	@Override
	public void preRenderCallback(EntityMale human, float partialTickTime) {
		if (human.getAge() < 24192000) {
			float size = Math.min(1.0F, human.getSize() * (human.getAge() / 24192000.0F * (human.getSize() / 3.0F)) + (human.getSize() / 4.0F));
			GlStateManager.scale(size, size, size);
		}
		else {
			GlStateManager.scale(human.getSize(), human.getSize(), human.getSize());
		}
		if (human.isSleeping()) {
			GlStateManager.rotate(180.0F, 0, 0, 1);
			GlStateManager.rotate(90.0F, 1, 0, 0);
			GlStateManager.translate(0, 0, -0.2);
		}
	}
	@Override
	public ResourceLocation getEntityTexture(EntityMale male) {
		return MALE_TEXTURE;
	}
	@Override
	protected void renderEntityName(EntityMale human, double x, double y, double z, String name, double distanceSq) {
		this.renderLivingLabel(human, human.getTribeName(), x, y + 0.25, z, 64);
		this.renderLivingLabel(human, human.getFirstName(), x, y, z, 64);
	}
}
