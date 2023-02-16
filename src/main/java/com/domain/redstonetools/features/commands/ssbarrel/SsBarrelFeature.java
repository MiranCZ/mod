package com.domain.redstonetools.features.commands.ssbarrel;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.domain.redstonetools.utils.RedstoneUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;

@Feature(name = "ss")
public class SsBarrelFeature extends CommandFeature<SsBarrelFeatureOptions> {
    private static final int BARREL_CONTAINER_SLOTS = 27;

    @Override
    protected int execute(ServerCommandSource source, SsBarrelFeatureOptions options) throws CommandSyntaxException {
        var stack = new ItemStack(Items.BARREL);

        // {BlockEntityTag:{Items:[{Slot:0,id:redstone,Count:3},{Slot:1,id:redstone,Count:61}]}}
        var items = new NbtList();

        for (int i = 0; i < RedstoneUtils.getRequiredShovelCount(options.signalStrength.getValue(), BARREL_CONTAINER_SLOTS); i++) {
            var item = new NbtCompound();
            item.putByte("Slot", (byte) i);
            item.putString("id", Registry.ITEM.getId(Items.WOODEN_SHOVEL).toString());
            item.putByte("Count", (byte) 1);
            items.add(item);
        }

        stack.getOrCreateSubNbt("BlockEntityTag").put("Items", items);
        stack.setCustomName(Text.of(options.signalStrength.getValue().toString()));

        source.getPlayer().giveItemStack(stack);

        return Command.SINGLE_SUCCESS;
    }
}
