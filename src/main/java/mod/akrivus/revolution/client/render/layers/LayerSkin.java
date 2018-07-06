package mod.akrivus.revolution.client.render.layers;

import mod.akrivus.revolution.entity.EntityFemale;
import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerSkin implements LayerRenderer<EntityHuman> {
	public static final ResourceLocation FEMALE_SKIN = new ResourceLocation("revolution:textures/entities/female/skin.png");
	public static final ResourceLocation MALE_SKIN = new ResourceLocation("revolution:textures/entities/male/skin.png");
	private final RenderLiving<? extends EntityHuman> renderer;
	private final ModelBase model;
	public LayerSkin(RenderLiving<? extends EntityHuman> renderer) {
		this.renderer = renderer;
		this.model = renderer.getMainModel();
	}
	@Override
	public void doRenderLayer(EntityHuman human, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.renderer.bindTexture(human instanceof EntityFemale ? FEMALE_SKIN : MALE_SKIN);
		int color = human.getSkinColor();
        float r = (float) ((color & 16711680) >> 16) / 255f;
        float g = (float) ((color & 65280) >> 8) / 255f;
        float b = (float) ((color & 255) >> 0) / 255f;
		GlStateManager.color(r, g, b);
		this.model.setModelAttributes(this.renderer.getMainModel());
        this.model.render(human, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.disableBlend();
	}
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
