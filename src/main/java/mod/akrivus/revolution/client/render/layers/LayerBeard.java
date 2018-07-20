package mod.akrivus.revolution.client.render.layers;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerBeard implements LayerRenderer<EntityHuman> {
	public static final ResourceLocation[] FACE_HAIR = new ResourceLocation[] {
		new ResourceLocation("revolution:textures/entities/male/face_0.png"),
		new ResourceLocation("revolution:textures/entities/male/face_1.png"),
		new ResourceLocation("revolution:textures/entities/male/face_2.png"),
		new ResourceLocation("revolution:textures/entities/male/face_3.png"),
		new ResourceLocation("revolution:textures/entities/male/face_4.png")
	};
	private final RenderLiving<? extends EntityHuman> renderer;
	private final ModelBase model;
	public LayerBeard(RenderLiving<? extends EntityHuman> renderer) {
		this.renderer = renderer;
		this.model = renderer.getMainModel();
	}
	@Override
	public void doRenderLayer(EntityHuman human, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (human.canLoseHair() && human.isOldEnoughToBreed()) {
			this.renderer.bindTexture(FACE_HAIR[human.getBeardType()]);
			int color = human.getHairColor();
			if (human.canHairGray() && human.getAge() > 70560000) {
				color = 0xCCCCCC;
			}
	        float r = (float)((color & 16711680) >> 16) / 255f;
	        float g = (float)((color & 65280) >> 8) / 255f;
	        float b = (float)((color & 255) >> 0) / 255f;
			GlStateManager.color(r, g, b);
			this.model.setModelAttributes(this.renderer.getMainModel());
	        this.model.render(human, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.disableBlend();
		}
	}
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
