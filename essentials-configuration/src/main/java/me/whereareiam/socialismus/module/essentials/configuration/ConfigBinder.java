package me.whereareiam.socialismus.module.essentials.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsCommands;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsMessages;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsSettings;
import me.whereareiam.socialismus.module.essentials.api.model.config.FeaturesConfig;
import me.whereareiam.socialismus.module.essentials.configuration.provider.EssentialsCommandsProvider;
import me.whereareiam.socialismus.module.essentials.configuration.provider.EssentialsMessagesProvider;
import me.whereareiam.socialismus.module.essentials.configuration.provider.EssentialsSettingsProvider;
import me.whereareiam.socialismus.module.essentials.configuration.provider.FeaturesProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class ConfigBinder extends AbstractModule {
	private final Path workingPath;

	@Override
	protected void configure() {
		bind(Path.class).annotatedWith(Names.named("workingPath")).toInstance(workingPath);
		bind(Path.class).annotatedWith(Names.named("featuresPath")).toInstance(workingPath.resolve("features"));
		createDirectories();

		bind(EssentialsSettingsProvider.class).asEagerSingleton();
		bind(EssentialsSettings.class).toProvider(EssentialsSettingsProvider.class);

		bind(EssentialsMessagesProvider.class).asEagerSingleton();
		bind(EssentialsMessages.class).toProvider(EssentialsMessagesProvider.class);

		bind(EssentialsCommandsProvider.class).asEagerSingleton();
		bind(EssentialsCommands.class).toProvider(EssentialsCommandsProvider.class);

		bind(FeaturesProvider.class).asEagerSingleton();
		bind(FeaturesConfig.class).toProvider(FeaturesProvider.class);
	}

	private void createDirectories() {
		try {
			Files.createDirectories(workingPath);
			Files.createDirectories(workingPath.resolve("features"));
		} catch (IOException e) {
			throw new RuntimeException("Failed to create directories", e);
		}
	}
}
