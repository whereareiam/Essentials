package me.whereareiam.socialismus.module.essentials.feature.dialogue.service;

import com.google.inject.Provider;
import lombok.AllArgsConstructor;
import me.whereareiam.socialismus.api.Serializer;
import me.whereareiam.socialismus.api.input.container.PlayerContainerService;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.api.model.serializer.SerializerContent;
import me.whereareiam.socialismus.api.model.serializer.SerializerPlaceholder;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueMessages;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.Dialogue;
import net.kyori.adventure.text.Component;

import java.util.List;

@AllArgsConstructor
abstract class AbstractDialogueService {
	protected final PlayerContainerService players;
	protected final Provider<DialogueMessages> messages;

	protected List<SerializerPlaceholder> placeholders(Dialogue pm, DummyPlayer viewer) {
		return List.of(
				new SerializerPlaceholder("{senderName}", pm.getSender().getUsername()),
				new SerializerPlaceholder("{recipientName}", pm.getRecipientName()),
				new SerializerPlaceholder("{message}", pm.getContent())
		);
	}

	protected Component render(DummyPlayer viewer,
	                           List<SerializerPlaceholder> ph,
	                           String template) {
		return Serializer.serialize(new SerializerContent(viewer, ph, template));
	}

	protected Component noRecipient(DummyPlayer sender, String recipient) {
		String tpl = messages.get().getCommands().getMessage().getNoRecipient();
		return Serializer.serialize(new SerializerContent(
				sender,
				List.of(new SerializerPlaceholder("{recipientName}", recipient)),
				tpl));
	}
}
