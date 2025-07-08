package me.whereareiam.socialismus.module.essentials.feature.dialogue.template;

import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.DefaultConfig;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueCommands;

import java.util.List;
import java.util.Map;

@Singleton
public class DialogueCommandsTemplate implements DefaultConfig<DialogueCommands> {
	@Override
	public DialogueCommands getDefault() {
		DialogueCommands config = new DialogueCommands();

		// Default values
		CommandEntity msg = CommandEntity.builder()
				.enabled(true)
				.aliases(List.of("message", "w", "whisper", "tell", "msg"))
				.permission("")
				.description("Send a private message to another player.")
				.usage("{alias} <recipient> <message>")
				.cooldown(CommandEntity.Cooldown.builder()
						.enabled(true)
						.duration(2)
						.group("global")
						.build()
				).build();

		CommandEntity reply = CommandEntity.builder()
				.enabled(true)
				.aliases(List.of("reply", "r"))
				.permission("")
				.description("Reply to the last private message you received.")
				.usage("{alias} <message>")
				.cooldown(CommandEntity.Cooldown.builder()
						.enabled(true)
						.duration(2)
						.group("global")
						.build()
				).build();

		config.setCommands(Map.of(
				"message", msg,
				"reply", reply
		));

		return config;
	}
}
