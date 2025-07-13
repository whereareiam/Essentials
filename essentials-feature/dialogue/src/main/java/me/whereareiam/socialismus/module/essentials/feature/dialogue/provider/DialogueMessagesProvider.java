package me.whereareiam.socialismus.module.essentials.feature.dialogue.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueMessages;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.template.DialogueMessagesTemplate;

import java.nio.file.Path;

@Singleton
public class DialogueMessagesProvider implements Provider<DialogueMessages>, Reloadable {
	private final Path featurePath;
	private final ConfigurationLoader configLoader;

	private DialogueMessages messages;

	@Inject
	public DialogueMessagesProvider(
			@Named("featurePath") Path featurePath,
			ConfigurationLoader configLoader,
			ConfigurationManager configManager,
			DialogueMessagesTemplate template,
			Registry<Reloadable> reloadableRegistry
	) {
		this.featurePath = featurePath;
		this.configLoader = configLoader;

		configManager.addTemplate(DialogueMessages.class, template);

		reloadableRegistry.register(this);
		get();
	}

	@Override
	public DialogueMessages get() {
		if (messages != null) return messages;

		load();

		return messages;
	}

	@Override
	public void reload() {
		load();
	}

	private void load() {
		messages = configLoader.load(featurePath.resolve("messages"), DialogueMessages.class);
	}
}
