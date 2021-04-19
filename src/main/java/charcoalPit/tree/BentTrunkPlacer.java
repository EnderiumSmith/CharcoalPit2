package charcoalPit.tree;

import charcoalPit.core.ModFeatures;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class BentTrunkPlacer extends AbstractTrunkPlacer {
	
	public static final Codec<BentTrunkPlacer> CODEC= RecordCodecBuilder.create((arg1)->{
		return func_236915_a_(arg1).apply(arg1,BentTrunkPlacer::new);
	});
	
	public BentTrunkPlacer(int p_i232060_1_, int p_i232060_2_, int p_i232060_3_) {
		super(p_i232060_1_, p_i232060_2_, p_i232060_3_);
	}
	
	@Override
	protected TrunkPlacerType<?> func_230381_a_() {
		return ModFeatures.BENT_PLACER;
	}
	
	@Override
	public List<FoliagePlacer.Foliage> func_230382_a_(IWorldGenerationReader p_230382_1_, Random p_230382_2_, int p_230382_3_, BlockPos p_230382_4_, Set<BlockPos> p_230382_5_, MutableBoundingBox p_230382_6_, BaseTreeFeatureConfig p_230382_7_) {
		func_236909_a_(p_230382_1_, p_230382_4_.down());
		List<FoliagePlacer.Foliage> list = Lists.newArrayList();
		Direction direction = Direction.Plane.HORIZONTAL.random(p_230382_2_);
		int i = p_230382_3_ - p_230382_2_.nextInt(4) - 1;
		int j = 3 - p_230382_2_.nextInt(3);
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int k = p_230382_4_.getX();
		int l = p_230382_4_.getZ();
		int i1 = 0;
		
		for(int j1 = 0; j1 < p_230382_3_; ++j1) {
			int k1 = p_230382_4_.getY() + j1;
			if (j1 >= i && j > 0) {
				k += direction.getXOffset();
				l += direction.getZOffset();
				--j;
			}
			
			if (func_236911_a_(p_230382_1_, p_230382_2_, blockpos$mutable.setPos(k, k1, l), p_230382_5_, p_230382_6_, p_230382_7_)) {
				i1 = k1 + 1;
			}
		}
		
		list.add(new FoliagePlacer.Foliage(new BlockPos(k, i1, l), 1, false));
		k = p_230382_4_.getX();
		l = p_230382_4_.getZ();
		
		return list;
	}
}
