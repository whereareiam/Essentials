package me.whereareiam.socialismus.module.essentials.feature.dialogue.config;

import lombok.Getter;
import lombok.Setter;
import me.whereareiam.commandant.model.CommandDefinition;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class DialogueCommands {
	private Map<String, CommandDefinition> commands = new HashMap<>();
}
