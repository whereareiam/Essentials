package me.whereareiam.socialismus.module.essentials.feature.dialogue.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Dialogue {
	private DummyPlayer sender;
	private String recipientName;
	private String content;

	private String origin;
}
