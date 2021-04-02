package charcoalPit.item;

import java.util.Optional;
import java.util.function.Predicate;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public class ItemFireStarter extends Item{

	public ItemFireStarter() {
		super(new Properties().group(ModItemRegistry.CHARCOAL_PIT));
	}
	
	@Override
	public int getUseDuration(ItemStack stack) {
		return 30;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		Vector3d eyePos=new Vector3d(playerIn.getPosX(), playerIn.getPosYEye(), playerIn.getPosZ());
		Vector3d rangedLookRot=playerIn.getLookVec().scale(playerIn.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue());
		Vector3d lookVec=eyePos.add(rangedLookRot);
		BlockRayTraceResult trace=worldIn.rayTraceBlocks(new RayTraceContext(eyePos, lookVec, BlockMode.OUTLINE, FluidMode.NONE, playerIn));
		ItemStack stack= playerIn.getHeldItem(handIn);
		if(trace.getType()==Type.BLOCK) {
			EntityRayTraceResult trace2=ItemFireStarter.rayTraceEntities(worldIn, null, eyePos, trace.getHitVec(), new AxisAlignedBB(eyePos, trace.getHitVec()), null);
			if(trace2==null) {
				playerIn.setActiveHand(handIn);
				return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
			}else {
				return new ActionResult<ItemStack>(ActionResultType.FAIL, stack);
			}
		}else {
			return new ActionResult<ItemStack>(ActionResultType.FAIL, stack);
		}
	}
	
	@Override
	public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
		Vector3d eyePos=new Vector3d(player.getPosX(), player.getPosYEye(), player.getPosZ());
		Vector3d rangedLookRot=player.getLookVec().scale(player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue());
		Vector3d lookVec=eyePos.add(rangedLookRot);
		BlockRayTraceResult trace=player.world.rayTraceBlocks(new RayTraceContext(eyePos, lookVec, BlockMode.OUTLINE, FluidMode.NONE, player));
		EntityRayTraceResult trace2=ItemFireStarter.rayTraceEntities(player.world, null, eyePos, trace.getHitVec(), new AxisAlignedBB(eyePos, trace.getHitVec()), null);
		if(!player.world.isRemote) {
			if(trace.getType()==Type.BLOCK&&trace2==null) {
				if(count==1) {
					BlockPos hit=new BlockPos(trace.getPos().offset(trace.getFace()));
					if(CampfireBlock.canBeLit(player.world.getBlockState(trace.getPos()))){
						player.world.playSound(null, trace.getPos(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1F, Item.random.nextFloat()*0.4F+0.8F);
						player.world.setBlockState(trace.getPos(), player.world.getBlockState(trace.getPos()).with(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
						stack.shrink(1);
					}else if(AbstractFireBlock.canLightBlock(player.world, hit, Direction.UP)) {
						BlockState blockstate1 = AbstractFireBlock.getFireForPlacement(player.world, hit);
						player.world.setBlockState(hit, blockstate1);
						player.world.playSound(null, hit, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1F, Item.random.nextFloat()*0.4F+0.8F);
						stack.shrink(1);
					}else {
						player.stopActiveHand();
					}
				}
			}else {
				player.stopActiveHand();
			}
		}else {
			if(trace.getType()==Type.BLOCK&&trace2==null) {
				player.world.addParticle(ParticleTypes.SMOKE, trace.getHitVec().x, trace.getHitVec().y, trace.getHitVec().z, 0, 0, 0);
			}
		}
	}
	
	public static EntityRayTraceResult rayTraceEntities(World worldIn, Entity projectile, Vector3d startVec, Vector3d endVec, AxisAlignedBB boundingBox, Predicate<Entity> filter) {
	      double d0 = Double.MAX_VALUE;
	      Entity entity = null;

	      for(Entity entity1 : worldIn.getEntitiesInAABBexcluding(projectile, boundingBox, filter)) {
	         AxisAlignedBB axisalignedbb = entity1.getBoundingBox();
	         Optional<Vector3d> optional = axisalignedbb.rayTrace(startVec, endVec);
	         if (optional.isPresent()) {
	            double d1 = startVec.squareDistanceTo(optional.get());
	            if (d1 < d0) {
	               entity = entity1;
	               d0 = d1;
	            }
	         }
	      }

	      return entity == null ? null : new EntityRayTraceResult(entity);
	   }
	
}
