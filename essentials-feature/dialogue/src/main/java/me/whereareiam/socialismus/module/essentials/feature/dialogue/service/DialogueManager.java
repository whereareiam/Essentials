package me.whereareiam.socialismus.module.essentials.feature.dialogue.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.api.Constants;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.Dialogue;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class DialogueManager {
	private final LocalDialogueService local;
	private final SyncDialogueService sync;
	private final Provider<DialogueSettings> settings;

	public void send(DummyPlayer sender, String rawRecipient, String rawMessage) {
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
