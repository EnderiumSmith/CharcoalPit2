package charcoalPit.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import charcoalPit.CharcoalPit;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ClayPotScreen extends ContainerScreen<ClayPotContainer2>{
	
	private static final ResourceLocation DISPENSER_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/claypot.png");
	
	public ClayPotScreen(ClayPotContainer2 container, PlayerInventory inv, ITextComponent title) {
		super(container, inv, title);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	      this.minecraft.getTextureManager().bindTexture(DISPENSER_GUI_TEXTURES);
	      int i = (this.width - this.xSize) / 2;
	      int j = (this.height - this.ySize) / 2;
	      this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
		
	}
	
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
	      this.renderBackground(matrixStack);
	      super.render(matrixStack, mouseX, mouseY, partialTicks);
	      this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	   }
	
}
