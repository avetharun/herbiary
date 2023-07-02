package com.avetharun.herbiary.entity.block;

import com.avetharun.herbiary.recipes.ShapelessPotRecipe;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractCampfirePotBlockEntity extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider {
    protected static final int INPUT_SLOT_INDEX_START = 0;
    protected static final int INPUT_SLOT_INDEX_END = 4;
    protected static final int OUTPUT_SLOT_INDEX = 5;
    private static final int[] BOTTOM_SLOTS = new int[]{0,1,2,3};
    public SimpleInventory inventory;
    int cookTime;
    int cookTimeTotal;
    protected final PropertyDelegate propertyDelegate;
    private final Object2IntOpenHashMap<Identifier> recipesUsed;
    private final RecipeManager.MatchGetter<Inventory, ? extends ShapelessPotRecipe> matchGetter;

    protected AbstractCampfirePotBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<ShapelessPotRecipe> recipeType) {
        super(blockEntityType, pos, state);
        this.inventory = new SimpleInventory(5);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> AbstractCampfirePotBlockEntity.this.cookTime;
                    case 1 -> AbstractCampfirePotBlockEntity.this.cookTimeTotal;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AbstractCampfirePotBlockEntity.this.cookTime = value;
                    case 1 -> AbstractCampfirePotBlockEntity.this.cookTimeTotal = value;
                    default -> {
                    }
                }

            }

            public int size() {
                return 4;
            }
        };
        this.recipesUsed = new Object2IntOpenHashMap<>();
        this.matchGetter = RecipeManager.createCachedMatchGetter(recipeType);
    }


    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory.stacks);;
        this.cookTime = nbt.getShort("CookTime");
        this.cookTimeTotal = nbt.getShort("CookTimeTotal");
        NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");
        for (String string : nbtCompound.getKeys()) {
            this.recipesUsed.put(new Identifier(string), nbtCompound.getInt(string));
        }

    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("CookTime", (short)this.cookTime);
        nbt.putShort("CookTimeTotal", (short)this.cookTimeTotal);
        Inventories.writeNbt(nbt, this.inventory.stacks);
        NbtCompound nbtCompound = new NbtCompound();
        this.recipesUsed.forEach((identifier, count) -> {
            nbtCompound.putInt(identifier.toString(), count);
        });
        nbt.put("RecipesUsed", nbtCompound);
    }
    public boolean isCraftingAreaEmpty() {
        boolean bl = true;
        for (int i = 0; i < 4; i++) {
            bl &= this.inventory.getStack(i).isEmpty();
        }
        return bl;
    }
    public static boolean isCraftingAreaEmpty(List<ItemStack> inventory) {
        boolean bl = true;
        for (int i = 0; i < 4; i++) {
            bl &= inventory.get(i).isEmpty();
        }
        return bl;
    }
    boolean isBurning() {
        assert this.world != null;
        BlockState cmpf = this.world.getBlockState(pos.down());
        boolean bl = cmpf.isIn(BlockTags.CAMPFIRES);
        if (bl) {
            bl = cmpf.get(Properties.LIT);
        }
        System.out.println(bl);
        return bl;
    }
    public static void tick(World world, BlockPos pos, BlockState state, AbstractCampfirePotBlockEntity blockEntity) {
        boolean bl = blockEntity.isBurning();
        boolean bl2 = false;
        boolean bl3 = !blockEntity.isCraftingAreaEmpty();
        if (blockEntity.isBurning() || bl3) {
            ShapelessPotRecipe recipe;
            var _r = blockEntity.matchGetter.getFirstMatch(blockEntity, world);
            if (bl3 && _r.isPresent()) {
                recipe = _r.get();
            } else {
                recipe = null;
            }

            int i = blockEntity.getMaxCountPerStack();

            if (blockEntity.isBurning() && canAcceptRecipeOutput(world.getRegistryManager(), recipe, blockEntity.inventory.stacks, i)) {
                ++blockEntity.cookTime;
                if (blockEntity.cookTime == blockEntity.cookTimeTotal) {
                    blockEntity.cookTime = 0;
                    blockEntity.cookTimeTotal = getCookTime(world, blockEntity);
                    if (craftRecipe(world.getRegistryManager(), recipe, blockEntity.inventory.stacks, i)) {
                        blockEntity.setLastRecipe(recipe);
                    }

                    bl2 = true;
                }
            } else {
                blockEntity.cookTime = 0;
            }
        } else if (!blockEntity.isBurning() && blockEntity.cookTime > 0) {
            blockEntity.cookTime = MathHelper.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
        }

        if (bl != blockEntity.isBurning()) {
            bl2 = true;
            state = (BlockState)state.with(AbstractFurnaceBlock.LIT, blockEntity.isBurning());
            world.setBlockState(pos, state, 3);
        }

        if (bl2) {
            markDirty(world, pos, state);
        }

    }

    private static boolean canAcceptRecipeOutput(DynamicRegistryManager registryManager, @Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (!((ItemStack)slots.get(0)).isEmpty() && recipe != null) {
            ItemStack itemStack = recipe.getOutput(registryManager);
            if (itemStack.isEmpty()) {
                return false;
            } else {
                ItemStack itemStack2 = (ItemStack)slots.get(2);
                if (itemStack2.isEmpty()) {
                    return true;
                } else if (!ItemStack.areEqual(itemStack, itemStack2)) {
                    return false;
                } else if (itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxCount()) {
                    return true;
                } else {
                    return itemStack2.getCount() < itemStack.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }

    private static boolean craftRecipe(DynamicRegistryManager registryManager, @Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (recipe != null && canAcceptRecipeOutput(registryManager, recipe, slots, count)) {
            ItemStack itemStack = (ItemStack)slots.get(0);
            ItemStack itemStack2 = recipe.getOutput(registryManager);
            ItemStack itemStack3 = (ItemStack)slots.get(2);
            if (itemStack3.isEmpty()) {
                slots.set(2, itemStack2.copy());
            } else if (itemStack3.isOf(itemStack2.getItem())) {
                itemStack3.increment(1);
            }

            if (itemStack.isOf(Blocks.WET_SPONGE.asItem()) && !((ItemStack)slots.get(1)).isEmpty() && ((ItemStack)slots.get(1)).isOf(Items.BUCKET)) {
                slots.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemStack.decrement(1);
            return true;
        } else {
            return false;
        }
    }

    private static int getCookTime(World world, AbstractCampfirePotBlockEntity furnace) {
        return 20 * 25;
    }
    public int[] getAvailableSlots(Direction side) {
        return BOTTOM_SLOTS;
    }

    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN && slot == 1) {
            return stack.isOf(Items.WATER_BUCKET) || stack.isOf(Items.BUCKET);
        } else {
            return true;
        }
    }

    public int size() {
        return this.inventory.size();
    }

    public boolean isEmpty() {
        Iterator<ItemStack> var1 = this.inventory.stacks.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    public ItemStack getStack(int slot) {
        return (ItemStack)this.inventory.getStack(slot);
    }

    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory.stacks, slot, amount);
    }

    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory.stacks, slot);
    }

    public void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = (ItemStack)this.inventory.stacks.get(slot);
        boolean bl = !stack.isEmpty() && ItemStack.areItemsEqual(stack, itemStack);
        this.inventory.stacks.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        if (slot == 0 && !bl) {
            this.cookTimeTotal = getCookTime(this.world, this);
            this.cookTime = 0;
            this.markDirty();
        }

    }

    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    public boolean isValid(int slot, ItemStack stack) {
        return slot == 4;
    }

    public void clear() {
        this.inventory.clear();
    }

    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            Identifier identifier = recipe.getId();
            this.recipesUsed.addTo(identifier, 1);
        }

    }

    @Nullable
    public Recipe<?> getLastRecipe() {
        return null;
    }

    public void unlockLastRecipe(PlayerEntity player) {
    }

    public void dropExperienceForRecipesUsed(ServerPlayerEntity player) {
        List<Recipe<?>> list = this.getRecipesUsedAndDropExperience((ServerWorld) player.getWorld(), player.getPos());
        player.unlockRecipes(list);
        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesUsedAndDropExperience(ServerWorld world, Vec3d pos) {
        List<Recipe<?>> list = Lists.newArrayList();
        ObjectIterator var4 = this.recipesUsed.object2IntEntrySet().iterator();

        while(var4.hasNext()) {
            Object2IntMap.Entry<Identifier> entry = (Object2IntMap.Entry)var4.next();
            world.getRecipeManager().get((Identifier)entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                dropExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe)recipe).getExperience());
            });
        }

        return list;
    }

    private static void dropExperience(ServerWorld world, Vec3d pos, int multiplier, float experience) {
        int i = MathHelper.floor((float)multiplier * experience);
        float f = MathHelper.fractionalPart((float)multiplier * experience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        ExperienceOrbEntity.spawn(world, pos, i);
    }

    public void provideRecipeInputs(RecipeMatcher finder) {
        Iterator var2 = this.inventory.stacks.iterator();

        while(var2.hasNext()) {
            ItemStack itemStack = (ItemStack)var2.next();
            finder.addInput(itemStack);
        }

    }
}
