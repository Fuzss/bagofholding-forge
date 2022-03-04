package fuzs.bagofholding.world.item;

import fuzs.bagofholding.world.inventory.BagContainer;
import fuzs.bagofholding.world.inventory.tooltip.ContainerItemTooltip;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ContainerItemHelper {
    public static SimpleContainer loadItemContainer(@Nullable CompoundTag compoundtag, Supplier<CompoundTag> tagSupplier, int containerRows) {
        SimpleContainer bagOfHoldingContainer = new BagContainer(containerRows);
        if (compoundtag != null && compoundtag.contains("Items")) {
            bagOfHoldingContainer.fromTag(compoundtag.getList("Items", 10));
        }
        bagOfHoldingContainer.addListener(container -> {
            saveItemContainer(tagSupplier.get(), (SimpleContainer) container);
        });
        return bagOfHoldingContainer;
    }

    private static void saveItemContainer(CompoundTag compoundtag, SimpleContainer container) {
        ListTag tag = container.createTag();
        compoundtag.put("Items", tag);
    }

    public static boolean overrideStackedOnOther(@Nullable CompoundTag compoundtag, Supplier<CompoundTag> tagSupplier, int containerRows, Slot slot, ClickAction clickAction, Player player, SoundEvent insertSound, SoundEvent removeSound) {
        if (clickAction != ClickAction.SECONDARY) return false;
        ItemStack hoveredStack = slot.getItem();
        if (hoveredStack.isEmpty()) {
            removeLastStack(compoundtag, tagSupplier, containerRows).ifPresent(stack -> {
                player.playSound(removeSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
                addStack(compoundtag, tagSupplier, containerRows, slot.safeInsert(stack), BagItem::mayPlaceInBag);
            });
        } else {
            hoveredStack = slot.safeTake(hoveredStack.getCount(), hoveredStack.getCount(), player);
            int transferredCount = addStack(compoundtag, tagSupplier, containerRows, hoveredStack, BagItem::mayPlaceInBag);
            hoveredStack.shrink(transferredCount);
            if (!hoveredStack.isEmpty()) {
                slot.safeInsert(hoveredStack);
            }
            if (transferredCount > 0) {
                player.playSound(insertSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
            }
        }
        return true;
    }

    public static boolean overrideOtherStackedOnMe(@Nullable CompoundTag compoundtag, Supplier<CompoundTag> tagSupplier, int containerRows, ItemStack stackOnMe, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, SoundEvent insertSound, SoundEvent removeSound) {
        if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)) return false;
        if (stackOnMe.isEmpty()) {
            removeLastStack(compoundtag, tagSupplier, containerRows).ifPresent((p_186347_) -> {
                player.playSound(removeSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
                slotAccess.set(p_186347_);
            });
        } else {
            int transferredCount = addStack(compoundtag, tagSupplier, containerRows, stackOnMe, BagItem::mayPlaceInBag);
            if (transferredCount > 0) {
                player.playSound(insertSound, 0.8F, 0.8F + player.getLevel().getRandom().nextFloat() * 0.4F);
                stackOnMe.shrink(transferredCount);
            }
        }
        return true;
    }

    private static int addStack(@Nullable CompoundTag compoundtag, Supplier<CompoundTag> tagSupplier, int containerRows, ItemStack newStack, Predicate<ItemStack> filter) {
        if (newStack.isEmpty() || !filter.test(newStack)) return 0;
        SimpleContainer container = loadItemContainer(compoundtag, tagSupplier, containerRows);
        ItemStack remainingStack = container.addItem(newStack);
        return newStack.getCount() - remainingStack.getCount();
    }

    private static Optional<ItemStack> removeLastStack(@Nullable CompoundTag compoundtag, Supplier<CompoundTag> tagSupplier, int containerRows) {
        SimpleContainer container = loadItemContainer(compoundtag, tagSupplier, containerRows);
        for (int i = container.getContainerSize() - 1; i >= 0; i--) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                return Optional.of(container.removeItem(i, stack.getCount()));
            }
        }
        return Optional.empty();
    }

    public static Optional<TooltipComponent> getTooltipImage(@Nullable CompoundTag compoundtag, Supplier<CompoundTag> tagSupplier, int containerRows, @Nullable DyeColor backgroundColor) {
        NonNullList<ItemStack> items;
        if (compoundtag == null || !compoundtag.contains("Items")) {
            items = NonNullList.withSize(containerRows * 9, ItemStack.EMPTY);
        } else {
            items = NonNullList.create();
            SimpleContainer container = loadItemContainer(compoundtag, tagSupplier, containerRows);
            for (int i = 0; i < container.getContainerSize(); i++) {
                items.add(container.getItem(i));
            }
        }
        return Optional.of(new ContainerItemTooltip(items, containerRows, backgroundColor));
    }
}