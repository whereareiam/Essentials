package me.whereareiam.socialismus.module.essentials.configuration.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.module.essentials.api.model.config.EssentialsMessages;
import me.whereareiam.socialismus.module.essentials.configuration.template.EssentialsMessagesTemplate;

import java.nio.file.Path;

@Singleton
public class EssentialsMessagesProvider implements Provider<EssentialsMessages>, Reloadable {
	private final Path workingPath;
	private final ConfigurationLoader configLoader;

	private EssentialsMessages messages;

	@Inject
	public EssentialsMessagesProvider(@Named("workingPath") Path workingPath, ConfigurationLoader configLoader, ConfigurationManager configManager,
	                                  EssentialsMessagesTemplate template, Registry<Reloadable> reloadableRegistry) {
		this.workingPath = workingPath;
		this.configLoader = configLoader;

		configManager.addTemplate(EssentialsMessages.class, template);

		reloadableRegistry.register(this);
		get();
	}

	@Override
	public EssentialsMessages get() {
		if (messages != null) return messages;

		load();

		return messages;
	}

	@Override
	public void reload() {
		load();
	}

	private void load() {
		messages = configLoader.load(workingPath.resolve("messages"), EssentialsMessages.class);
	}
}
