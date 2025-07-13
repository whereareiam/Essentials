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
		DialogueMessages.Commands commands = new DialogueMessages.Commands();
		DialogueMessages.Commands.Message message = new DialogueMessages.Commands.Message();
		message.setNoRecipient("{prefix}<white>Recipient not found");
		message.setSenderFormat("<gold><bold>PM</bold> <dark_gray>| <gray>{senderName} <dark_gray>-> <gray>{recipientName}: <white>{message}");
		message.setRecipientFormat("<gold><bold>PM</bold> <dark_gray>| <gray>{senderName} <dark_gray>-> <gray>{recipientName}: <white>{message}");
		commands.setMessage(message);

		config.setCommands(commands);

		return config;
	}
}
