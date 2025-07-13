package me.whereareiam.socialismus.module.essentials.feature.dialogue.template;

import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;

@Singleton
public class DialogueSettingsTemplate implements DefaultConfig<DialogueSettings> {
	@Override
	public DialogueSettings getDefault() {
		DialogueSettings config = new DialogueSettings();

		// Default values
		DialogueSettings.Synchronization synchronization = new DialogueSettings.Synchronization();
		synchronization.setEnabled(false);

		config.setSynchronization(synchronization);

		return config;
	}
}
