package me.whereareiam.socialismus.module.essentials.feature.dialogue.template;

import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueMessages;

@Singleton
public class DialogueMessagesTemplate implements DefaultConfig<DialogueMessages> {
	@Override
	public DialogueMessages getDefault() {
		DialogueMessages config = new DialogueMessages();

		// Default values

		return config;
	}
}
