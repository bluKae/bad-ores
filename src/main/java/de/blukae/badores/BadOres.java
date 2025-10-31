/*
 * Copyright (C) 2025 bluKae
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.blukae.badores;

import com.mojang.logging.LogUtils;
import de.blukae.badores.advancement.MineBadOreTrigger;
import de.blukae.badores.block.BadOreBlockEntity;
import de.blukae.badores.client.BadOresClient;
import de.blukae.badores.item.BadOreBookItem;
import de.blukae.badores.item.BadOreItem;
import de.blukae.badores.ore.BadOre;
import de.blukae.badores.ore.Fleesonsite;
import de.blukae.badores.ore.Iwontfite;
import de.blukae.badores.ore.Nosleeptonite;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Supplier;

@Mod(BadOres.MOD_ID)
@EventBusSubscriber
public class BadOres {
    public static final String MOD_ID = "badores";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(
            BuiltInRegistries.ENTITY_TYPE,
            MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(
            BuiltInRegistries.FEATURE,
            MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(
            BuiltInRegistries.SOUND_EVENT,
            MOD_ID);
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES = DeferredRegister.create(
            BuiltInRegistries.TRIGGER_TYPES,
            MOD_ID);
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(
            BuiltInRegistries.ARMOR_MATERIAL,
            MOD_ID);

    public static final DeferredItem<BadOreBookItem> BAD_ORE_BOOK_ITEM = ITEMS.registerItem(
            "bad_ore_book",
            BadOreBookItem::new);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(
            "tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.badores"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(BadOre.AMADEUM.oreBlock::toStack)
                    .displayItems((parameters, output) -> ITEMS.getEntries().forEach(item -> output.accept(item.get())))
                    .build());


    public static final Supplier<BlockEntityType<BadOreBlockEntity>> BAD_ORE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "bad_ore", () -> {
                Set<Block> validBlocks = new HashSet<>();
                for (BadOre ore : BadOre.values()) {
                    if (ore.oreBlock.get().tickRate != null) {
                        validBlocks.add(ore.oreBlock.get());
                    }
                    if (ore.deepslateOreBlock != null && ore.deepslateOreBlock.get().tickRate != null) {
                        validBlocks.add(ore.deepslateOreBlock.get());
                    }
                }
                return new BlockEntityType<>(BadOreBlockEntity::new, validBlocks, null);
            });

    public static final Supplier<MineBadOreTrigger> MINE_BAD_ORE_TRIGGER = TRIGGER_TYPES.register(
            "mine_bad_ore",
            MineBadOreTrigger::new);
    public static final TagKey<Block> BAD_ORES_TAG = TagKey.create(Registries.BLOCK, BadOres.rl("bad_ores"));
    public static final TagKey<Item> ORE_BOOK_COMPONENTS = TagKey.create(
            Registries.ITEM,
            BadOres.rl("ore_book_components"));

    public BadOres(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.debug("Loading {} ores", BadOre.values().length);

        BLOCKS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        TRIGGER_TYPES.register(modEventBus);
        FEATURES.register(modEventBus);
        ARMOR_MATERIALS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, BadOresConfig.SPEC);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @SubscribeEvent
    public static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(
                Fleesonsite.FLEESONSITE_ENTITY_TYPE.get(),
                Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.4).build());

        event.put(
                Nosleeptonite.NOSLEEPTONITE_ENTITY_TYPE.get(),
                Monster.createMonsterAttributes()
                        .add(Attributes.FOLLOW_RANGE, 40.0)
                        .add(Attributes.ATTACK_DAMAGE, 5.0)
                        .add(Attributes.MAX_HEALTH, 15.0)
                        .add(Attributes.MOVEMENT_SPEED, 0.4)
                        .build());
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.commonToClient(
                RandomTranslation.TYPE,
                RandomTranslation.STREAM_CODEC,
                BadOresClient::handleRandomTranslation);
    }

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (event.getState().is(BadOre.GHOSTIUM.ores)) {
            event.setCanceled(true);
            for (ItemEntity drop : event.getDrops()) {
                drop.setNeverPickUp();
                event.getLevel().addFreshEntity(drop);
            }
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingIncomingDamageEvent event) {
        Entity entity = event.getSource().getEntity();
        boolean hasIwontfite = switch (entity) {
            case Player player -> !player.isCreative() && player.getInventory().contains(BadOre.IWONTFITE.oreItems);
            case LivingEntity livingEntity -> Arrays.stream(EquipmentSlot.values())
                    .anyMatch(slot -> livingEntity.getItemBySlot(slot).is(BadOre.IWONTFITE.oreItems));
            case null, default -> false;
        };

        if (hasIwontfite) {
            if (entity.getRandom().nextInt(1000) == 0) {
                event.setAmount(1.0F);

                if (entity instanceof ServerPlayer serverPlayer) {
                    Iwontfite.HURT_IWONTFITE_TRIGGER.get().trigger(serverPlayer);
                }
            } else {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onArmorHurt(ArmorHurtEvent event) {
        event.getArmorMap().forEach((slot, armorEntry) -> {
            if (armorEntry.armorItemStack.getItem() instanceof BadOreItem item) {
                item.template.onArmorHurt(event.getEntity(), armorEntry.armorItemStack, slot);
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            Inventory inventory = player.getInventory();

            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (stack.is(Fleesonsite.FLEESONSITE_JUMP) && player.getRandom().nextInt(150) == 0) {
                    List<Integer> freeSlots = new ArrayList<>();
                    for (int j = 0; j < inventory.getContainerSize(); j++) {
                        if (inventory.getItem(j).isEmpty()) {
                            freeSlots.add(j);
                        }
                    }
                    if (freeSlots.isEmpty()) {
                        return;
                    }
                    int targetSlot = freeSlots.get(player.getRandom().nextInt(freeSlots.size()));
                    inventory.setItem(i, ItemStack.EMPTY);
                    inventory.setItem(targetSlot, stack);
                }
            }
        }
    }


}
