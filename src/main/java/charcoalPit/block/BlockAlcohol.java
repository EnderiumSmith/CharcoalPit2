package charcoalPit.block;

import charcoalPit.fluid.ModFluidRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAlcohol extends FlowingFluidBlock{
	
	public BlockAlcohol() {
		super(()->ModFluidRegistry.AlcoholStill, Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops());
	}
	
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		worldIn.getPendingFluidTicks().scheduleTick(pos, state.getFluidState().getFluid(), this.getFluid().getTickRate(worldIn));
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		worldIn.getPendingFluidTicks().scheduleTick(pos, state.getFluidState().getFluid(), this.getFluid().getTickRate(worldIn));
	}
}
