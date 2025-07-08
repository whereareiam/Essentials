package me.whereareiam.socialismus.module.essentials.feature.dialogue.config;

import lombok.Getter;
import me.whereareiam.socialismus.api.model.CommandEntity;

import java.util.Map;

@Getter
public class DialogueCommands {
	private Map<String, CommandEntity> commands;
}
