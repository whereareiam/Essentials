package me.whereareiam.socialismus.module.essentials.configuration.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsSettings;
import me.whereareiam.socialismus.module.essentials.configuration.template.EssentialsSettingsTemplate;

import java.nio.file.Path;

@Singleton
public class EssentialsSettingsProvider implements Provider<EssentialsSettings>, Reloadable {
	private final Path workingPath;
	private final ConfigurationLoader configLoader;

	private EssentialsSettings settings;

	@Inject
	public EssentialsSettingsProvider(@Named("workingPath") Path workingPath, ConfigurationLoader configLoader, ConfigurationManager configManager,
	                                  EssentialsSettingsTemplate template, Registry<Reloadable> reloadableRegistry) {
		this.workingPath = workingPath;
		this.configLoader = configLoader;

		configManager.addTemplate(EssentialsSettings.class, template);

		reloadableRegistry.register(this);
		get();
	}

	@Override
	public EssentialsSettings get() {
		if (settings != null) return settings;

		load();

		return settings;
	}

	@Override
	public void reload() {
		load();
	}

	private void load() {
		settings = configLoader.load(workingPath.resolve("settings"), EssentialsSettings.class);
	}
}
