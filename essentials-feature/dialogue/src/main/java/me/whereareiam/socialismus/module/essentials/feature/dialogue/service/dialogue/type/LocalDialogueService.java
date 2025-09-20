package me.whereareiam.socialismus.module.essentials.feature.dialogue.service.dialogue.type;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.input.container.PlayerContainerService;
import me.whereareiam.socialismus.api.model.serializer.SerializerPlaceholder;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueMessages;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.Dialogue;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.dialogue.AbstractDialogueService;

import java.util.List;

@Singleton
public final class LocalDialogueService extends AbstractDialogueService {
	@Inject
	public LocalDialogueService(
			PlayerContainerService players,
			Provider<DialogueMessages> messages
	) {
		super(players, messages);
	}

	public boolean deliverLocally(Dialogue pm) {
		return players.getPlayer(pm.getRecipientName()).map(recipient -> {
			DialogueMessages.Commands.Message cfg =
					messages.get().getCommands().getMessage();

			List<SerializerPlaceholder> ph = placeholders(pm);

			pm.getSender().sendMessage(render(pm.getSender(), ph, cfg.getSenderFormat()));
			recipient.sendMessage(render(recipient, ph, cfg.getRecipientFormat()));
			return true;
		}).orElse(false);
	}

	public void notifyUndeliverable(Dialogue pm) {
		pm.getSender().sendMessage(noRecipient(pm.getSender(), pm.getRecipientName()));
	}
}
