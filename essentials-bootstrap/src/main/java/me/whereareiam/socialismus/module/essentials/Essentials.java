package me.whereareiam.socialismus.module.essentials;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.container.PlayerContainerService;
import me.whereareiam.socialismus.api.input.registry.ObjectMapperRegistry;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.PlatformInteractor;
import me.whereareiam.socialismus.api.output.Scheduler;
import me.whereareiam.socialismus.api.output.SerializationService;
import me.whereareiam.socialismus.api.output.command.CommandService;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.api.output.module.SocialisticModule;
import me.whereareiam.socialismus.api.output.resource.sync.SyncService;
import me.whereareiam.socialismus.module.essentials.api.input.FeatureManager;
import me.whereareiam.socialismus.module.essentials.command.CommandRegistrar;
import me.whereareiam.socialismus.module.essentials.common.CommonConfiguration;
import me.whereareiam.socialismus.module.essentials.configuration.ConfigBinder;
import me.whereareiam.socialismus.module.essentials.feature.FeatureConfiguration;

import java.util.Map;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Essentials extends SocialisticModule {
	private final Injector parentInjector;
	private final Registry<Reloadable> reloadableRegistry;
	private final Registry<Map<String, CommandEntity>> commandRegistry;
	private Injector injector;

	@Override
	public void onLoad() {
		injector =
				Guice.createInjector(
						new EssentialsInjectorConfiguration(
								parentInjector.getInstance(ObjectMapperRegistry.class),
								parentInjector.getInstance(PlatformInteractor.class),
								parentInjector.getInstance(PlayerContainerService.class),
								parentInjector.getInstance(SerializationService.class),
								parentInjector.getInstance(SyncService.class),
								reloadableRegistry,
								commandRegistry,
								parentInjector.getInstance(ConfigurationManager.class),
								parentInjector.getInstance(ConfigurationLoader.class),
								parentInjector.getInstance(CommandService.class)),
						new CommonConfiguration(),
						new ConfigBinder(workingPath),
						new FeatureConfiguration());
	}

	@Override
	public void onEnable() {
		injector.getInstance(CommandRegistrar.class).registerCommands();
		injector.getInstance(FeatureManager.class).initializeFeatures();
	}

	@Override
	public void onDisable() {
		injector.getInstance(Scheduler.class).cancelByModule("essentials");
	}

	@Override
	public void onUnload() {
	}
}
