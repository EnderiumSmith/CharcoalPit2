package charcoalPit.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import charcoalPit.CharcoalPit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class BarrelScreen extends ContainerScreen<BarrelContainer>{

	private static final ResourceLocation BARREL_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/barrel.png");
	private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};
	
	public BarrelScreen(BarrelContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	      this.minecraft.getTextureManager().bindTexture(BARREL_GUI_TEXTURES);
	      int i = (this.width - this.xSize) / 2;
	      int j = (this.height - this.ySize) / 2;
	      this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
	      renderFluid(matrixStack,i,j);
		
	}
	
	public void renderFluid(MatrixStack matrixStack, int i, int j) {
		FluidStack fluid=FluidStack.loadFluidStackFromNBT(this.container.inventorySlots.get(this.container.inventorySlots.size()-1).getStack().getTag().getCompound("fluid"));
		if(fluid.isEmpty())
			return;
		int height=(int)(58*fluid.getAmount()/16000D);
		Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite sprite=this.minecraft.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluid.getFluid().getAttributes().getStillTexture());
		int c=fluid.getFluid().getAttributes().getColor(fluid);
		RenderSystem.color4f((c>>16&255)/255.0F, (c>>8&255)/255.0F, (c&255)/255.0F, 1F/*(c>>24&255)/255f*/);
		//blit(matrixStack, i+62, j+71-height, this.getBlitOffset(), 16, height+1, sprite);
		while(height>=16) {
			innerBlit(matrixStack.getLast().getMatrix(), i+62, i+62+16, j+72-height, j+72+16-height, this.getBlitOffset(), sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
			height-=16;
		}
		if(height>0)
			innerBlit(matrixStack.getLast().getMatrix(), i+62, i+62+16, j+72-height, j+72, this.getBlitOffset(), sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), 
				(sprite.getMaxV()-sprite.getMinV())*(height/16F)+sprite.getMinV());
		//innerBlit(matrixStack.getLast().getMatrix(), i+62, i+62+16, j+72-height, j+72, this.getBlitOffset(), sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//this.blit(matrixStack, i+62, j+71-height, (int)(sprite.getWidth()*sprite.getMinU()), (int)(sprite.getHeight()*sprite.getMinV()), 16, height);
		/*minecraft.getTextureManager().bindTexture(fluid.getFluid().getAttributes().getStillTexture());
		this.blit(matrixStack, i+62, j+71-height, 0, 0, 16, height);*/
	}
	
	private static void innerBlit(Matrix4f matrix, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
	      BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
	      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
	      bufferbuilder.pos(matrix, (float)x1, (float)y2, (float)blitOffset).tex(minU, maxV).endVertex();
	      bufferbuilder.pos(matrix, (float)x2, (float)y2, (float)blitOffset).tex(maxU, maxV).endVertex();
	      bufferbuilder.pos(matrix, (float)x2, (float)y1, (float)blitOffset).tex(maxU, minV).endVertex();
	      bufferbuilder.pos(matrix, (float)x1, (float)y1, (float)blitOffset).tex(minU, minV).endVertex();
	      bufferbuilder.finishDrawing();
	      RenderSystem.enableAlphaTest();
	      WorldVertexBufferUploader.draw(bufferbuilder);
	   }
	
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		super.drawGuiContainerForegroundLayer(matrixStack, x, y);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	      this.minecraft.getTextureManager().bindTexture(BARREL_GUI_TEXTURES);
	      //int i = (this.width - this.xSize) / 2;
	      //int j = (this.height - this.ySize) / 2;
	      this.blit(matrixStack, 62, 14, 176, 47, 16, 71-14);
	    int time=container.array.get(0);
	    int total=container.array.get(1);
	    if(total>0&&time>=0) {
	    	int height=(int)(time*14F/total);
	    	this.blit(matrixStack, 97, 36, 176, 2, 18, 14-height);
	    	height=BUBBLELENGTHS[(time)/2%7];
	    	this.blit(matrixStack, 82, 14+29-height, 176, 18+29-height, 12, height);
	    }
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
	      super.render(matrixStack, mouseX, mouseY, partialTicks);
	      this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	   
	}

}
