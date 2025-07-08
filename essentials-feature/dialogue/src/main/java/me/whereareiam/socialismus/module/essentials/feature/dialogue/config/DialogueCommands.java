package me.whereareiam.socialismus.module.essentials.feature.dialogue.config;

import lombok.Getter;
import lombok.Setter;
import me.whereareiam.socialismus.api.model.CommandEntity;

import java.util.Map;

@Setter
@Getter
public class DialogueCommands {
	private Map<String, CommandEntity> commands;
}
