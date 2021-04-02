package charcoalPit.tile;

import com.mojang.blaze3d.matrix.MatrixStack;

import charcoalPit.block.BlockPotteryKiln;
import charcoalPit.block.BlockPotteryKiln.EnumKilnTypes;
import charcoalPit.core.ModBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class TESRPotteryKiln extends TileEntityRenderer<TilePotteryKiln>{

	public TESRPotteryKiln(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}
	@Override
	public void render(TilePotteryKiln tile, float partialTicks, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		BlockState state=tile.getWorld().getBlockState(tile.getPos());
		if(state.getBlock()==ModBlockRegistry.Kiln&&(state.get(BlockPotteryKiln.TYPE)==EnumKilnTypes.EMPTY||state.get(BlockPotteryKiln.TYPE)==EnumKilnTypes.COMPLETE)) {
			matrixStack.push();
	
	        matrixStack.translate(0.5, 0.1, 0.5);
	        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
	        ItemStack stack=tile.pottery.getStackInSlot(0);
	        IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(stack, tile.getWorld(), null);
	        itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, combinedLight, combinedOverlay, ibakedmodel);
			
	        matrixStack.pop();
		}
	}
	
}
