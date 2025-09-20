package me.whereareiam.socialismus.module.essentials.feature.dialogue.command;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.api.output.command.CommandBase;
import me.whereareiam.socialismus.api.output.command.CommandCooldown;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueCommands;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.dialogue.DialogueManager;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;

import java.util.Map;

@Singleton
public class ReplyCommand extends CommandBase {
	private static final String COMMAND_NAME = "reply";
	private final Provider<DialogueCommands> commands;
	private final DialogueManager dialogueManager;

	@Inject
	public ReplyCommand(
			Provider<DialogueCommands> commands,
			DialogueManager dialogueManager
	) {
		super(COMMAND_NAME);

		this.commands = commands;
		this.dialogueManager = dialogueManager;
	}

	@Command("%command." + COMMAND_NAME)
	@CommandDescription("%description." + COMMAND_NAME)
	@CommandCooldown("%cooldown." + COMMAND_NAME)
	@Permission("%permission." + COMMAND_NAME)
	public void onCommand(
			DummyPlayer dummyPlayer,
			@Argument(value = "message") String message
	) {
		dialogueManager.sendReply(dummyPlayer, message);
	}

	@Command("%command." + COMMAND_NAME + " list")
	@CommandDescription("Show recent conversations")
	@CommandCooldown("%cooldown." + COMMAND_NAME)
	@Permission("%permission." + COMMAND_NAME)
	public void onListCommand(DummyPlayer dummyPlayer) {
		dialogueManager.showRecentConversations(dummyPlayer);
	}

	@Command("%command." + COMMAND_NAME + " clear")
	@CommandDescription("Clear conversation history")
	@CommandCooldown("%cooldown." + COMMAND_NAME)
	@Permission("%permission." + COMMAND_NAME)
	public void onClearCommand(DummyPlayer dummyPlayer) {
		dialogueManager.clearConversationHistory(dummyPlayer);
	}

	@Command("%command." + COMMAND_NAME + " <player> <message>")
	@CommandDescription("Reply to specific player")
	@CommandCooldown("%cooldown." + COMMAND_NAME)
	@Permission("%permission." + COMMAND_NAME)
	public void onReplyToCommand(
			DummyPlayer dummyPlayer,
			@Argument(value = "player", suggestions = "replyTargets") String player,
			@Argument(value = "message") String message
	) {
		dialogueManager.sendReplyTo(dummyPlayer, player, message);
	}

	@Override
	public CommandEntity getCommandEntity() {
		return commands.get().getCommands().get(COMMAND_NAME);
	}

	@Override
	public Map<String, String> getTranslations() {
		CommandEntity command = commands.get().getCommands().get("reply");

		return Map.of(
				"command." + command.getAliases().get(0) + ".name", command.getUsage().replace("{alias}", String.join("|", command.getAliases())),
				"command." + command.getAliases().get(0) + ".permission", command.getPermission(),
				"command." + command.getAliases().get(0) + ".description", command.getDescription(),
				"command." + command.getAliases().get(0) + ".usage", command.getUsage()
		);
	}
}
