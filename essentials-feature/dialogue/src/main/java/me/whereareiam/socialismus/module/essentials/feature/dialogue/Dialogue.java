package me.whereareiam.socialismus.module.essentials.feature.dialogue;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.api.model.CommandEntity;
import me.whereareiam.socialismus.api.output.command.CommandBase;
import me.whereareiam.socialismus.module.essentials.api.model.feature.CommandFeature;
import me.whereareiam.socialismus.module.essentials.api.output.CommandFeatureInitializer;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.command.MessageCommand;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.command.ReplyCommand;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueCommands;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueMessages;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.sync.DialogueNetworkBridge;

import java.util.Map;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Dialogue implements CommandFeatureInitializer {
	private final DialogueNetworkBridge networkBridge;

	private final Provider<DialogueSettings> settings;
	private final Provider<DialogueMessages> messages;
	private final Provider<DialogueCommands> commands;

	@Override
	public String getId() {
		return "dialogue";
	}

	@Override
	public void initialize(CommandFeature feature) {
		// initialize configurations before using them
		settings.get();
		messages.get();
		commands.get();

		networkBridge.initialize();
	}

	@Override
	public Map<String, CommandEntity> getCommands() {
		return commands.get().getCommands();
	}

	@Override
	public Map<String, Class<? extends CommandBase>> getExecutors() {
		return Map.of(
				"message", MessageCommand.class,
				"reply", ReplyCommand.class
		);
	}
}
