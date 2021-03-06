package charcoalPit.tree;

import charcoalPit.core.ModFeatures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;

import java.util.Random;
import java.util.Set;

public class PalmFoliagePlacer extends FoliagePlacer {
	
	public static final Codec<PalmFoliagePlacer> CODEC= RecordCodecBuilder.create((arg1)->{
		return func_242830_b(arg1).apply(arg1,PalmFoliagePlacer::new);
	});
	
	public PalmFoliagePlacer(FeatureSpread p_i241999_1_, FeatureSpread p_i241999_2_) {
		super(p_i241999_1_, p_i241999_2_);
	}
	
	@Override
	protected FoliagePlacerType<?> func_230371_a_() {
		return ModFeatures.PALM_PLACER;
	}
	
	@Override
	protected void func_230372_a_(IWorldGenerationReader p_230372_1_, Random p_230372_2_, BaseTreeFeatureConfig p_230372_3_, int p_230372_4_, Foliage p_230372_5_, int p_230372_6_, int p_230372_7_, Set<BlockPos> p_230372_8_, int p_230372_9_, MutableBoundingBox p_230372_10_) {
		int i=p_230372_9_;
		if(i>=p_230372_9_-p_230372_6_) {
			this.func_236753_a_(p_230372_1_, p_230372_2_, p_230372_3_, p_230372_5_.func_236763_a_(), 1, p_230372_8_, i, p_230372_5_.func_236765_c_(), p_230372_10_);
			i--;
			if(i>=p_230372_9_-p_230372_6_) {
				this.func_236753_a_(p_230372_1_, p_230372_2_, p_230372_3_, p_230372_5_.func_236763_a_(), 3, p_230372_8_, i, p_230372_5_.func_236765_c_(), p_230372_10_);
				i--;
				if(i>=p_230372_9_-p_230372_6_) {
					this.func_236753_a_(p_230372_1_, p_230372_2_, p_230372_3_, p_230372_5_.func_236763_a_(), 4, p_230372_8_, i, p_230372_5_.func_236765_c_(), p_230372_10_);
					i--;
				}
			}
		}
	}
	
	@Override
	public int func_230374_a_(Random p_230374_1_, int p_230374_2_, BaseTreeFeatureConfig p_230374_3_) {
		return 2;
	}
	
	@Override
	protected boolean func_230373_a_(Random p_230373_1_, int p_230373_2_, int p_230373_3_, int p_230373_4_, int p_230373_5_, boolean p_230373_6_) {
		if(p_230373_3_==0){
			return p_230373_2_==p_230373_4_&&p_230373_2_>0;
		}
		if(p_230373_3_==-1){
			return p_230373_2_!=0&&p_230373_4_!=0&&!(p_230373_2_==1&&p_230373_4_==1);
		}
		if(p_230373_3_==-2){
			return (p_230373_2_!=0&&p_230373_4_!=0)||(p_230373_2_<3&&p_230373_4_<3);
		}
		return false;
	}
}
