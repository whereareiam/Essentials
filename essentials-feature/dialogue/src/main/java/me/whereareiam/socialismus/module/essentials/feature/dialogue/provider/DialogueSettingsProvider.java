package me.whereareiam.socialismus.module.essentials.feature.dialogue.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.config.ConfigProvider;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.template.DialogueSettingsTemplate;
import me.whereareiam.socialismus.registry.base.Registry;

import java.nio.file.Path;

@Singleton
public class DialogueSettingsProvider extends ConfigProvider<DialogueSettings> implements Provider<DialogueSettings> {
	private final Path featurePath;

	@Inject
	public DialogueSettingsProvider(
			@Named("featurePath") Path featurePath,
			Registry<Reloadable> reloadableRegistry
	) {
		this.featurePath = featurePath;
		reloadableRegistry.register(this);
	}

	@Override
	protected DialogueSettings load() {
		return Config.update(featurePath.resolve("settings"), DialogueSettings.class);
	}

	@Override
	protected void registerTemplate() {
		Config.registerTemplate(DialogueSettingsTemplate.class);
	}
}
