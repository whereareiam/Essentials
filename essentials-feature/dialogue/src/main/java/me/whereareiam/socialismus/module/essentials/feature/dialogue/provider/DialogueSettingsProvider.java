package me.whereareiam.socialismus.module.essentials.feature.dialogue.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.socialismus.api.Reloadable;
import me.whereareiam.socialismus.api.input.registry.Registry;
import me.whereareiam.socialismus.api.output.config.ConfigurationLoader;
import me.whereareiam.socialismus.api.output.config.ConfigurationManager;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.template.DialogueSettingsTemplate;

import java.nio.file.Path;

@Singleton
public class DialogueSettingsProvider implements Provider<DialogueSettings>, Reloadable {
	private final Path featurePath;
	private final ConfigurationLoader configLoader;

	private DialogueSettings settings;

	@Inject
	public DialogueSettingsProvider(
			@Named("featurePath") Path featurePath,
			ConfigurationLoader configLoader,
			ConfigurationManager configManager,
			DialogueSettingsTemplate template,
			Registry<Reloadable> reloadableRegistry
	) {
		this.featurePath = featurePath;
		this.configLoader = configLoader;

		configManager.addTemplate(DialogueSettings.class, template);

		reloadableRegistry.register(this);
		get();
	}

	@Override
	public DialogueSettings get() {
		if (settings != null) return settings;

		load();

		return settings;
	}

	@Override
	public void reload() {
		load();
	}

	private void load() {
		settings = configLoader.load(featurePath.resolve("settings"), DialogueSettings.class);
	}
}
