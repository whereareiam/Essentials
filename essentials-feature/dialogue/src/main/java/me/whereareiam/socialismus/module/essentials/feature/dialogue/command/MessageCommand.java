package me.whereareiam.socialismus.module.essentials.feature.dialogue.command;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.api.output.command.CommandBase;
import me.whereareiam.socialismus.api.output.command.CommandCooldown;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueCommands;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueMessages;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;

import java.util.Map;

@Singleton
public class MessageCommand extends CommandBase {
	private static final String COMMAND_NAME = "message";
	private final Provider<DialogueCommands> commands;
	private final Provider<DialogueMessages> messages;

	@Inject
	public MessageCommand(
			Provider<DialogueCommands> commands,
			Provider<DialogueMessages> messages
	) {
		super(COMMAND_NAME);

		this.commands = commands;
		this.messages = messages;
	}

	@Command("%command." + COMMAND_NAME)
	@CommandDescription("%description." + COMMAND_NAME)
	@CommandCooldown("%cooldown." + COMMAND_NAME)
	@Permission("%permission." + COMMAND_NAME)
	public void onCommand(
			DummyPlayer dummyPlayer,
			@Argument(value = "recipient", suggestions = "crossPlayers") String recipientName,
			@Argument(value = "message") String message
	) {
	}

	@Override
	public CommandEntity getCommandEntity() {
		return commands.get().getCommands().get(COMMAND_NAME);
	}

	@Override
	public Map<String, String> getTranslations() {
		CommandEntity command = commands.get().getCommands().get("message");

		return Map.of(
				"command." + command.getAliases().get(0) + ".name", command.getUsage().replace("{alias}", String.join("|", command.getAliases())),
				"command." + command.getAliases().get(0) + ".permission", command.getPermission(),
				"command." + command.getAliases().get(0) + ".description", command.getDescription(),
				"command." + command.getAliases().get(0) + ".usage", command.getUsage()
		);
	}
}
