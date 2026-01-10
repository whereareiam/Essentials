package me.whereareiam.socialismus.module.essentials.feature.dialogue.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.whereareiam.configura.Config;
import me.whereareiam.socialismus.Reloadable;
import me.whereareiam.socialismus.config.ConfigProvider;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueMessages;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.template.DialogueMessagesTemplate;
import me.whereareiam.socialismus.registry.base.Registry;

import java.nio.file.Path;

@Singleton
public class DialogueMessagesProvider extends ConfigProvider<DialogueMessages> implements Provider<DialogueMessages> {
	private final Path featurePath;

	@Inject
	public DialogueMessagesProvider(
			@Named("featurePath") Path featurePath,
			Registry<Reloadable> reloadableRegistry
	) {
		this.featurePath = featurePath;
		reloadableRegistry.register(this);
	}

	@Override
	protected DialogueMessages load() {
		return Config.update(featurePath.resolve("messages"), DialogueMessages.class);
	}

	@Override
	protected void registerTemplate() {
		Config.registerTemplate(DialogueMessagesTemplate.class);
	}
}
