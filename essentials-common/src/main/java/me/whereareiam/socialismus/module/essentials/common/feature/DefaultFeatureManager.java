package me.whereareiam.socialismus.module.essentials.common.feature;

import com.google.inject.*;
import com.google.inject.Module;
import com.google.inject.name.Names;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import me.whereareiam.socialismus.api.Logger;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.command.CommandBase;
import me.whereareiam.socialismus.api.output.command.CommandService;
import me.whereareiam.socialismus.module.essentials.api.input.FeatureManager;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsSettings;
import me.whereareiam.socialismus.module.essentials.api.model.config.FeaturesConfig;
import me.whereareiam.socialismus.module.essentials.api.model.feature.CommandFeature;
import me.whereareiam.socialismus.module.essentials.api.model.feature.Feature;
import me.whereareiam.socialismus.module.essentials.api.output.CommandFeatureInitializer;
import me.whereareiam.socialismus.module.essentials.api.output.FeatureInitializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class DefaultFeatureManager implements FeatureManager {
	private final Injector injector;
	private final FeaturesConfig features;
	private final EssentialsSettings settings;
	private final CommandService commandService;

	private final Path featuresPath;

	private final Map<String, Class<? extends Module>> modules;
	private final Registry<Map<String, CommandEntity>> commandRegistry;

	private final Map<String, Feature> activeFeatures = new HashMap<>();

	@Inject
	public DefaultFeatureManager(
			Injector injector,
			FeaturesConfig features,
			EssentialsSettings settings,
			CommandService commandService,
			@Named("featuresPath") Path featuresPath,
			Map<String, Class<? extends Module>> modules,
			Registry<Map<String, CommandEntity>> commandRegistry
	) {
		this.injector = injector;
		this.features = features;
		this.settings = settings;
		this.commandService = commandService;
		this.featuresPath = featuresPath;
		this.modules = modules;
		this.commandRegistry = commandRegistry;
	}

	@Override
	public void initializeFeatures() {
		var features = this.features.getFeatures();

		boolean announce = settings.getAnnounce().isFeatureInitialization();

		for (Map.Entry<String, ? extends Feature> entry : features.entrySet()) {
			String id = entry.getKey();
			Feature feat = entry.getValue();

			if (!feat.isEnabled()) continue;

			if (modules.containsKey(id)) {
				Class<? extends Module> moduleClass = modules.get(id);

				Path featurePath = featuresPath.resolve(id);
				try {
					Files.createDirectories(featurePath);
				} catch (IOException ex) {
					Logger.severe("Cannot create directory for feature " + id, ex);
					continue;
				}

				try {
					System.out.println("Initializing feature: " + id);
					Injector featureInjector = injector.createChildInjector(
							moduleClass.getDeclaredConstructor().newInstance(),
							new AbstractModule() {
								@Override
								protected void configure() {
									bind(Path.class).annotatedWith(Names.named("featurePath")).toInstance(featurePath);
								}
							}
					);
					FeatureInitializer<? extends Feature> init = featureInjector.getInstance(new Key<>() {});

					invokeInit(init, feat);
					if (announce) Logger.info("Feature enabled: %s", id);

					if (feat instanceof CommandFeature cf && cf.isRegisterCommands())
						registerCommandsFor(init, announce, featureInjector);

					activeFeatures.put(id, feat);
				} catch (Exception e) {
					Logger.severe("Failed to initialize feature: " + id, e);
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Map<String, Feature> getActiveFeatures() {
		return activeFeatures;
	}

	@SuppressWarnings("unchecked")
	private <F extends Feature> void invokeInit(FeatureInitializer<F> init, Feature f) {
		init.initialize((F) f);
	}

	private <F extends Feature> void registerCommandsFor(
			FeatureInitializer<F> init,
			boolean announce,
			Injector featureInjector
	) {
		if (!(init instanceof CommandFeatureInitializer cfi))
			return;

		Map<String, CommandEntity> commands = cfi.getCommands();
		if (commands == null || commands.isEmpty())
			return;

		commandRegistry.register(commands);

		Map<String, Class<? extends CommandBase>> executors = cfi.getExecutors();
		if (executors == null || executors.isEmpty())
			return;

		for (Map.Entry<String, CommandEntity> entry : commands.entrySet()) {
			String commandKey = entry.getKey();

			if (executors.containsKey(commandKey)) {
				Class<? extends CommandBase> commandClass = executors.get(commandKey);
				CommandBase commandInstance = featureInjector.getInstance(commandClass);
				commandService.registerCommand(commandInstance);
			}
		}

		if (announce) Logger.info("Registered commands for feature: %s", init.getId());
	}
}
