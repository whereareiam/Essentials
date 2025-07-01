package me.whereareiam.socialismus.module.essentials.configuration.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsCommands;
import me.whereareiam.socialismus.module.essentials.configuration.template.EssentialsCommandsTemplate;

import java.nio.file.Path;

@Singleton
public class EssentialsCommandsProvider implements Provider<EssentialsCommands>, Reloadable {
	private final Path workingPath;
	private final ConfigurationLoader configLoader;

	private EssentialsCommands commands;

	@Inject
	public EssentialsCommandsProvider(@Named("workingPath") Path workingPath, ConfigurationLoader configLoader, ConfigurationManager configManager,
	                                  EssentialsCommandsTemplate template, Registry<Reloadable> reloadableRegistry) {
		this.workingPath = workingPath;
		this.configLoader = configLoader;

		configManager.addTemplate(EssentialsCommands.class, template);

		reloadableRegistry.register(this);
		get();
	}

	@Override
	public EssentialsCommands get() {
		if (commands != null) return commands;

		load();

		return commands;
	}

	@Override
	public void reload() {
		load();
	}

	private void load() {
		commands = configLoader.load(workingPath.resolve("commands"), EssentialsCommands.class);
	}
}
