package org.fingtest6;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(shuili.MODID)
public class shuili {
    public static final String MODID = "shuili";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // 原始水力物品，食用回复1饱食度和1饱和度
    public static final DeferredItem<Item> SHUILI_ITEM = ITEMS.registerSimpleItem("shuili",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1)           // 回复1点饱食度
                    .saturationModifier(1.0f)  // 回复1点饱和度
                    .alwaysEdible()         // 始终可食用
                    .build()));

    // 烹饪后的水力物品，食用回复3饱食度和4饱和度
    public static final DeferredItem<Item> COOKED_SHUILI_ITEM = ITEMS.registerSimpleItem("cooked_shuili",
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(3)           // 回复3点饱食度
                    .saturationModifier(4.0f)  // 回复4点饱和度
                    .build()));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.shuili"))
            .icon(() -> SHUILI_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(SHUILI_ITEM.get());
                output.accept(COOKED_SHUILI_ITEM.get());
            })
            .build());

    public shuili(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT);
        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}