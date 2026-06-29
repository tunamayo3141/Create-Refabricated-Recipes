package tnmy.create_refab_recipes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateRefabricatedRecipes implements ModInitializer {
	public static final String MOD_ID = "create_refab_recipes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ModConfig CONFIG = new ModConfig();

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("create_refab_recipes_config.json");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		loadConfig();

		// Define the codec for your condition's JSON data
		MapCodec<ConfigCondition> codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.STRING.fieldOf("setting").forGetter(ConfigCondition::getSetting)
		).apply(instance, ConfigCondition::new));

		// Create and register the condition type
		ResourceConditionType<ConfigCondition> type = ResourceConditionType.create(
				Identifier.parse("create_refab_recipes:config_check"),
				codec
		);

		ConfigCondition.TYPE = type;
		ResourceConditions.register(type);
	}

	// Condition class
	private static class ConfigCondition implements ResourceCondition {
		private static ResourceConditionType<ConfigCondition> TYPE;

		private final String setting;

		public ConfigCondition(String setting) {
			this.setting = setting;
		}

		public String getSetting() {
			return setting;
		}

		@Override
		public ResourceConditionType<?> getType() {
			return TYPE;
		}

		@Override
		public boolean test(RegistryOps.RegistryInfoLookup registryInfo) {
			switch (setting) {
				// Game Economy Changing Recipes
				case "enable_netherite_scrap":
					return CONFIG.enableNetheriteScrapRecipe;
				case "enable_wither_rose":
					return CONFIG.enableWitherRoseRecipe;
				case "enable_wither_skull":
					return CONFIG.enableWitherSkeletonSkullRecipe;
				case "enable_basalt_generator":
					return CONFIG.enableBasaltGenerator;
				case "enable_cobblestone_generator":
					return CONFIG.enableCobblestoneGenerator;
				case "enable_limestone_generator":
					return CONFIG.enableLimestoneGenerator;

				// Compacting Recipes
				case "enable_cobbled_deepslate":
					return CONFIG.enableCobbledDeepslateRecipe;
				case "enable_diamond":
					return CONFIG.enableDiamondRecipe;
				case "enable_rose_quartz":
					return CONFIG.enableRoseQuartzRecipe;
				case "enable_tuff":
					return CONFIG.enableTuffRecipe;

				// Haunting Recipes
				case "enable_coal":
					return CONFIG.enableCoalRecipe;
				case "enable_netherrack":
					return CONFIG.enableNetherrackRecipe;

				// Mixing Recipes
				case "enable_asurine":
					return CONFIG.enableAsurineRecipe;
				case "enable_calcite":
					return CONFIG.enableCalciteRecipe;
				case "enable_crimsite":
					return CONFIG.enableCrimsiteRecipe;
				case "enable_glowstone":
					return CONFIG.enableGlowstoneRecipe;
				case "enable_gunpowder":
					return CONFIG.enableGunpowderRecipe;
				case "enable_lapis_lazuli":
					return CONFIG.enableLapisLazuliRecipe;
				case "enable_leather":
					return CONFIG.enableLeatherRecipe;
				case "enable_ochrum":
					return CONFIG.enableOchrumRecipe;
				case "enable_polished_rose_quartz":
					return CONFIG.enablePolishedRoseQuartzRecipe;
				case "enable_redstone":
					return CONFIG.enableRedstoneRecipe;
				case "enable_veridium":
					return CONFIG.enableVeridiumRecipe;

				default:
					LOGGER.warn("Unknown config setting: {}", setting);
					return true;
			}
		}
	}

	// Helper method for creating resource locations
	public static Identifier id(String path) {
		return Identifier.parse(MOD_ID + ":" + path);
	}

	// Config loading method
	private void loadConfig() {
		if (Files.exists(CONFIG_PATH)) {
			try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
				ModConfig loaded = GSON.fromJson(reader, ModConfig.class);

				// Copy all values (skip the _info field since it's just a comment)
				CONFIG.enableNetheriteScrapRecipe = loaded.enableNetheriteScrapRecipe;
				CONFIG.enableWitherRoseRecipe = loaded.enableWitherRoseRecipe;
				CONFIG.enableWitherSkeletonSkullRecipe = loaded.enableWitherSkeletonSkullRecipe;
				CONFIG.enableBasaltGenerator = loaded.enableBasaltGenerator;
				CONFIG.enableCobblestoneGenerator = loaded.enableCobblestoneGenerator;
				CONFIG.enableLimestoneGenerator = loaded.enableLimestoneGenerator;
				CONFIG.enableCobbledDeepslateRecipe = loaded.enableCobbledDeepslateRecipe;
				CONFIG.enableDiamondRecipe = loaded.enableDiamondRecipe;
				CONFIG.enableRoseQuartzRecipe = loaded.enableRoseQuartzRecipe;
				CONFIG.enableTuffRecipe = loaded.enableTuffRecipe;
				CONFIG.enableCoalRecipe = loaded.enableCoalRecipe;
				CONFIG.enableNetherrackRecipe = loaded.enableNetherrackRecipe;
				CONFIG.enableAsurineRecipe = loaded.enableAsurineRecipe;
				CONFIG.enableCalciteRecipe = loaded.enableCalciteRecipe;
				CONFIG.enableCrimsiteRecipe = loaded.enableCrimsiteRecipe;
				CONFIG.enableGlowstoneRecipe = loaded.enableGlowstoneRecipe;
				CONFIG.enableGunpowderRecipe = loaded.enableGunpowderRecipe;
				CONFIG.enableLapisLazuliRecipe = loaded.enableLapisLazuliRecipe;
				CONFIG.enableLeatherRecipe = loaded.enableLeatherRecipe;
				CONFIG.enableOchrumRecipe = loaded.enableOchrumRecipe;
				CONFIG.enablePolishedRoseQuartzRecipe = loaded.enablePolishedRoseQuartzRecipe;
				CONFIG.enableRedstoneRecipe = loaded.enableRedstoneRecipe;
				CONFIG.enableVeridiumRecipe = loaded.enableVeridiumRecipe;

				LOGGER.info("Loaded config from {}", CONFIG_PATH);
			} catch (IOException e) {
				LOGGER.error("Failed to load config, using defaults", e);
			}
		} else {
			// Create default config
			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
				GSON.toJson(CONFIG, writer);
				LOGGER.info("Created default config at {}", CONFIG_PATH);
			} catch (IOException e) {
				LOGGER.error("Failed to create config file", e);
			}
		}
	}
}