package charcoalPit.block;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import charcoalPit.tile.TileCreosoteCollector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class BlockCreosoteCollector extends Block{

	public BlockCreosoteCollector(Properties properties) {
		super(properties);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		if(InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			tooltip.add(new StringTextComponent("\u00A77"+"Collects creosote oil produced by log/coal piles above"));
			tooltip.add(new StringTextComponent("\u00A77"+"Collection area is a 9x9 '+' shape"));
			tooltip.add(new StringTextComponent("\u00A77"+"Piles need to be connected to funnel"));
			tooltip.add(new StringTextComponent("\u00A77"+"Creosote oil only flows down between piles"));
			tooltip.add(new StringTextComponent("\u00A77"+"If redstone signal is applied:"));
			tooltip.add(new StringTextComponent("\u00A77"+"-Funnel will also collect from neighboring funnels"));
			tooltip.add(new StringTextComponent("\u00A77"+"-Funnel will auto output creosote oil"));
			tooltip.add(new StringTextComponent("\u00A77"+"Creosote oil can only be extracted from the bottom"));
			tooltip.add(new StringTextComponent("\u00A77"+"A line of funnels works best"));
		}else {
			tooltip.add(new StringTextComponent("\u00A77"+"<Hold Shift>"+"\u00A77"));
		}
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileCreosoteCollector();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		TileCreosoteCollector tile=(TileCreosoteCollector)worldIn.getTileEntity(pos);
		if(worldIn.isRemote)
			return player.getHeldItem(handIn).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).isPresent()?ActionResultType.SUCCESS:ActionResultType.FAIL;
		else {
			return FluidUtil.interactWithFluidHandler(player, handIn, tile.external)?ActionResultType.SUCCESS:ActionResultType.FAIL;
		}
	}

}
