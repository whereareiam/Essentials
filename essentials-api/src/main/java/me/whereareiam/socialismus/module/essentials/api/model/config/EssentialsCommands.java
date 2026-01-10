package me.whereareiam.socialismus.module.essentials.api.model.config;

import lombok.Getter;
import lombok.ToString;
import me.whereareiam.commandant.model.CommandDefinition;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class EssentialsCommands {
	private Map<String, CommandDefinition> commands = new HashMap<>();
}
