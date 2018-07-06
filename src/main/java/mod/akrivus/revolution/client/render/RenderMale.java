package mod.akrivus.revolution.client.render;

import mod.akrivus.revolution.client.model.ModelMale;
import mod.akrivus.revolution.client.render.layers.LayerEyes;
import mod.akrivus.revolution.client.render.layers.LayerHair;
import mod.akrivus.revolution.client.render.layers.LayerSkin;
import mod.akrivus.revolution.entity.EntityMale;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMale extends RenderBiped<EntityMale> {
	private static final ResourceLocation MALE_TEXTURE = new ResourceLocation("revolution:textures/entities/male/blank.png");
	public RenderMale(RenderManager manager) {
		super(manager, new ModelMale(), 0.25F);
		this.addLayer(new LayerSkin(this));
		this.addLayer(new LayerEyes(this));
		this.addLayer(new LayerHair(this));
	}
	@Override
	public void preRenderCallback(EntityMale human, float partialTickTime) {
		GlStateManager.scale(human.getSize(), human.getSize(), human.getSize());
	}
	@Override
	public ResourceLocation getEntityTexture(EntityMale male) {
		return MALE_TEXTURE;
	}
	@Override
	protected void renderEntityName(EntityMale human, double x, double y, double z, String name, double distanceSq) {
		this.renderLivingLabel(human, human.getTribeName(), x, y+0.25, z, 64);
		this.renderLivingLabel(human, human.getFirstName() , x, y, z, 64);
	}
}
