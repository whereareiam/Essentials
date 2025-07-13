package me.whereareiam.socialismus.module.essentials.feature.dialogue.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.api.Constants;
import me.whereareiam.socialismus.api.Serializer;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueMessages;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.Dialogue;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class DialogueManager {
	private final LocalDialogueService local;
	private final SyncDialogueService sync;
	
	private final Provider<DialogueSettings> settings;
	private final Provider<DialogueMessages> messages;

	public void send(DummyPlayer sender, String rawRecipient, String rawMessage) {
		if (sender.getUsername().equals(rawRecipient)) {
			sender.sendMessage(Serializer.serialize(
					sender,
					messages.get().getCommands().getMessage().getSamePlayer()
			));
			return;
		}

		Dialogue pm = Dialogue.builder()
				.sender(sender)
				.recipientName(rawRecipient)
				.content(rawMessage)
				.build();

		boolean deliveredHere = local.deliverLocally(pm);

		pm.setOrigin(Constants.IDENTIFIER);
		sync.publish(pm);

		if (!deliveredHere && !settings.get().getSynchronization().isEnabled())
			local.notifyUndeliverable(pm);
	}
}
