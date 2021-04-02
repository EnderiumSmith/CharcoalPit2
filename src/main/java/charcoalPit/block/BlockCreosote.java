package charcoalPit.block;

import charcoalPit.fluid.ModFluidRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCreosote extends FlowingFluidBlock{

	public BlockCreosote() {
		super(()->ModFluidRegistry.CreosoteStill, Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops());
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
	
	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 100;
	}
	
	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 5;
	}
	
	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if(entityIn instanceof LivingEntity&&
				((LivingEntity)entityIn).getCreatureAttribute()==CreatureAttribute.ARTHROPOD) {
			entityIn.attackEntityFrom(DamageSource.GENERIC, 1);
		}
	}

}
