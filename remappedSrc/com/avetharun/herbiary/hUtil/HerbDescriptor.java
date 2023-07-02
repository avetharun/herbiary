package com.avetharun.herbiary.hUtil;

import com.avetharun.herbiary.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class HerbDescriptor {
    public Identifier name;
    public String data;
    public ItemStack stack = ModItems.UNKNOWN_HERB.getDefaultStack();
    // used by GUI only.
    public Slot slot;
    public boolean poison;
    public boolean isEdible;
    public ItemStack getBuiltStack() {
        ItemStack  s = stack;
        NbtCompound sc = s.getOrCreateNbt();
        NbtList lore = new NbtList();
        lore.add(NbtString.of(data));
        NbtCompound dc = new NbtCompound();
        dc.put("Lore", lore);
        System.out.println(dc);
        System.out.println(sc);
        sc.put("display", dc);
        return s;
    }
    public static class Builder{
        HerbDescriptor impl_descriptor = new HerbDescriptor();
        public static Builder create() {
            return new Builder();
        }
        public Builder name(Identifier name) {impl_descriptor.name = name; return this;}
        public Builder name(String name) {impl_descriptor.name = new Identifier(name); return this;}
        public Builder data(String data) {impl_descriptor.data = Text.Serializer.toJson(Text.of(data)); return this;}
        public Builder data(Text data) {impl_descriptor.data = Text.Serializer.toJson(data); return this;}
        public Builder stack(ItemStack stack) {impl_descriptor.stack = stack; return this;}
        public HerbDescriptor build() {return impl_descriptor;}
    }
}
