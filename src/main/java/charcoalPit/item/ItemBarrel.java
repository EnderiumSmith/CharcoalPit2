package charcoalPit.item;

import charcoalPit.core.ModBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBarrel extends BlockItem {
    public ItemBarrel(Block blockIn, Item.Properties builder){
        super(blockIn,builder);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FluidHandlerItemStack(stack,16000){
            @Override
            public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
                return stack.getFluid().getAttributes().getTemperature()<450&&!stack.getFluid().getAttributes().isGaseous();
            }

            @Override
            public boolean canFillFluidType(FluidStack fluid) {
                return isFluidValid(0,fluid);
            }
        };
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
        if(stack.hasTag()&&stack.getTag().contains("Fluid"))
            return FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid")).isEmpty()?super.getItemStackLimit(stack):1;
        return super.getItemStackLimit(stack);
    }
    
    public static boolean isFluidEmpty(ItemStack stack){
        if(stack.hasTag()&&stack.getTag().contains("Fluid")){
            return FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid")).isEmpty();
        }
        return true;
    }
    
    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        if(ItemBarrel.isFluidEmpty(stack)&&!context.getPlayer().isSneaking()){
            RayTraceResult trace=rayTrace(context.getWorld(),context.getPlayer(), RayTraceContext.FluidMode.SOURCE_ONLY);
            if(trace.getType()==RayTraceResult.Type.BLOCK){
                BlockRayTraceResult blocktrace=(BlockRayTraceResult)trace;
                BlockPos pos=blocktrace.getPos();
                Direction dir=blocktrace.getFace();
                BlockPos pos2=pos.offset(dir);
                if (context.getWorld().isBlockModifiable(context.getPlayer(), pos) && context.getPlayer().canPlayerEdit(pos2, dir, stack)) {
                    FluidState state=context.getWorld().getFluidState(pos);
                    if(state.getFluid()==Fluids.WATER&&state.isSource()){
                        int s=0;
                        for(Direction dir2:Direction.Plane.HORIZONTAL){
                            if(context.getWorld().getFluidState(pos.offset(dir2)).getFluid()==Fluids.WATER&&
                            context.getWorld().getFluidState(pos.offset(dir2)).isSource())
                                s++;
                        }
                        if(s>=2){
                            context.getPlayer().addStat(Stats.ITEM_USED.get(this));
                            context.getPlayer().playSound(SoundEvents.ITEM_BUCKET_FILL,1F,1F);
                            ItemStack stack2=new ItemStack(ModBlockRegistry.Barrel);
                            stack2.setTagInfo("Fluid",new FluidStack(Fluids.WATER,16000).writeToNBT(new CompoundNBT()));
                            ItemHandlerHelper.giveItemToPlayer(context.getPlayer(),stack2);
                            context.getPlayer().getHeldItem(context.getHand()).shrink(1);
                            if(context.getWorld().getBlockState(pos).getBlock() instanceof IBucketPickupHandler){
                                ((IBucketPickupHandler)context.getWorld().getBlockState(pos).getBlock()).pickupFluid(context.getWorld(),pos,context.getWorld().getBlockState(pos));
                            }
                            return ActionResultType.SUCCESS;
                        }
                    }
                }
            }
        }
        return ActionResultType.PASS;
    }
}
