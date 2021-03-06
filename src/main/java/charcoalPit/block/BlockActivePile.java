package charcoalPit.block;

import java.util.Random;

import charcoalPit.tile.TileActivePile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockActivePile extends Block{

	public final boolean isCoal;
	public BlockActivePile(boolean coal,Properties properties) {
		super(properties);
		isCoal=coal;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		if(!isCoal) {
			builder.add(BlockStateProperties.AXIS);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileActivePile(isCoal);
	}
	
	@Override
	public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
		return true;
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		((TileActivePile)worldIn.getTileEntity(pos)).isValid=false;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		double centerX = pos.getX() + 0.5F;
		double centerY = pos.getY() + 2F;
		double centerZ = pos.getZ() + 0.5F;
		//double d3 = 0.2199999988079071D;
		//double d4 = 0.27000001072883606D;
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.1D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.15D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY-1, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.1D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY-1, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.15D, 0.0D);
	}

}
