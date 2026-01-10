package me.whereareiam.socialismus.module.essentials.feature.dialogue.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.config.ConfigProvider;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueCommands;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.template.DialogueCommandsTemplate;
import me.whereareiam.socialismus.registry.base.Registry;

import java.nio.file.Path;

@Singleton
public class DialogueCommandsProvider extends ConfigProvider<DialogueCommands> implements Provider<DialogueCommands> {
	private final Path featurePath;

	@Inject
	public DialogueCommandsProvider(
			@Named("featurePath") Path featurePath,
			Registry<Reloadable> reloadableRegistry
	) {
		this.featurePath = featurePath;
		reloadableRegistry.register(this);
	}

	@Override
	protected DialogueCommands load() {
		return Config.update(featurePath.resolve("commands"), DialogueCommands.class);
	}

	@Override
	protected void registerTemplate() {
		Config.registerTemplate(DialogueCommandsTemplate.class);
	}
}
