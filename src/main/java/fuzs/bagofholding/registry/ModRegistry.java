package fuzs.bagofholding.registry;

import fuzs.bagofholding.BagOfHolding;
import fuzs.bagofholding.capability.BagPerseveranceCapability;
import fuzs.bagofholding.capability.BagPerseveranceCapabilityImpl;
import fuzs.bagofholding.world.inventory.BagMenu;
import fuzs.bagofholding.world.item.BagItem;
import fuzs.bagofholding.world.item.crafting.BagUpgradeRecipe;
import fuzs.bagofholding.world.item.enchantment.PerseveranceEnchantment;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.capability.data.PlayerRespawnStrategy;
import fuzs.puzzleslib.registry.RegistryManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
    private static final RegistryManager REGISTRY = RegistryManager.of(BagOfHolding.MOD_ID);
    public static final RegistryObject<Item> LEATHER_BAG_OF_HOLDING_ITEM = REGISTRY.registerItem("leather_bag_of_holding", () -> new BagItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), 1, DyeColor.BROWN, BagMenu::oneRow));
    public static final RegistryObject<Item> IRON_BAG_OF_HOLDING_ITEM = REGISTRY.registerItem("iron_bag_of_holding", () -> new BagItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), 3, DyeColor.WHITE, BagMenu::threeRows));
    public static final RegistryObject<Item> GOLDEN_BAG_OF_HOLDING_ITEM = REGISTRY.registerItem("golden_bag_of_holding", () -> new BagItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), 6, DyeColor.YELLOW, BagMenu::sixRows));
    public static final RegistryObject<MenuType<BagMenu>> LEATHER_BAG_OF_HOLDING_MENU_TYPE = REGISTRY.registerRawMenuType("leather_bag_of_holding", () -> BagMenu::oneRow);
    public static final RegistryObject<MenuType<BagMenu>> IRON_BAG_OF_HOLDING_MENU_TYPE = REGISTRY.registerRawMenuType("iron_bag_of_holding", () -> BagMenu::threeRows);
    public static final RegistryObject<MenuType<BagMenu>> GOLDEN_BAG_OF_HOLDING_MENU_TYPE = REGISTRY.registerRawMenuType("golden_bag_of_holding", () -> BagMenu::sixRows);
    public static final RegistryObject<Enchantment> PERSEVERANCE_ENCHANTMENT = REGISTRY.registerEnchantment("perseverance", () -> new PerseveranceEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.values()));
    public static final RegistryObject<RecipeSerializer<ShapedRecipe>> BAG_OF_HOLDING_UPGRADE_RECIPE_SERIALIZER = REGISTRY.register((Class<RecipeSerializer<?>>) (Class<?>) RecipeSerializer.class, "crafting_special_bag_of_holding_upgrade", () -> new BagUpgradeRecipe.Serializer());

    private static final CapabilityController CAPABILITIES = CapabilityController.of(BagOfHolding.MOD_ID);
    public static final Capability<BagPerseveranceCapability> BAG_PERSEVERANCE_CAPABILITY = CAPABILITIES.registerPlayerCapability("bag_perseverance", BagPerseveranceCapability.class, player -> new BagPerseveranceCapabilityImpl(), PlayerRespawnStrategy.NEVER, new CapabilityToken<BagPerseveranceCapability>() {});

    public static final EnchantmentCategory BAG_OF_HOLDING_ENCHANTMENT_CATEGORY = EnchantmentCategory.create("BAG_OF_HOLDING", item -> item instanceof BagItem);

    public static void touch() {

    }
}