package me.whereareiam.socialismus.module.essentials;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.container.PlayerContainerService;
import me.whereareiam.socialismus.api.input.registry.ObjectMapperRegistry;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.PlatformInteractor;
import me.whereareiam.socialismus.api.output.SerializationService;
import me.whereareiam.socialismus.api.output.command.CommandService;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.api.output.resource.sync.SyncService;

import java.util.Map;

@RequiredArgsConstructor
public class EssentialsInjectorConfiguration extends AbstractModule {
	private final ObjectMapperRegistry objectMapperRegistry;
	private final PlatformInteractor platformInteractor;
	private final PlayerContainerService playerContainerService;
	private final SerializationService serializationService;
	private final SyncService syncService;

	private final Registry<Reloadable> reloadableRegistry;
	private final Registry<Map<String, CommandEntity>> commandRegistry;

	private final ConfigurationManager configurationManager;
	private final ConfigurationLoader configurationLoader;

	private final CommandService commandService;

	@Override
	protected void configure() {
		bind(ObjectMapperRegistry.class).toInstance(objectMapperRegistry);
		bind(PlatformInteractor.class).toInstance(platformInteractor);
		bind(PlayerContainerService.class).toInstance(playerContainerService);
		bind(SerializationService.class).toInstance(serializationService);
		bind(SyncService.class).toInstance(syncService);

		bind(new TypeLiteral<Registry<Reloadable>>() {}).toInstance(reloadableRegistry);
		bind(new TypeLiteral<Registry<Map<String, CommandEntity>>>() {}).toInstance(commandRegistry);

		bind(ConfigurationManager.class).toInstance(configurationManager);
		bind(ConfigurationLoader.class).toInstance(configurationLoader);

		bind(CommandService.class).toInstance(commandService);
	}
}
