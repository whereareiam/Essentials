package me.whereareiam.socialismus.module.essentials.feature.dialogue.sync;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.api.Constants;
import me.whereareiam.socialismus.api.Logger;
import me.whereareiam.socialismus.api.output.PlatformInteractor;
import me.whereareiam.socialismus.api.output.SerializationService;
import me.whereareiam.socialismus.api.output.resource.sync.SyncService;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.Dialogue;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.LocalDialogueService;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class DialogueNetworkBridge {
	private static final String CHANNEL = Constants.Channels.NAME + ":dialogue";

	private final SyncService sync;
	private final Provider<DialogueSettings> settings;
	private final SerializationService serializer;
	private final PlatformInteractor platform;
	private final LocalDialogueService dialogueService;

	public void initialize() {
		subscribe();
	}

	public void publish(Dialogue pm) {
		if (!settings.get().getSynchronization().isEnabled()) return;

		try {
			pm.setOrigin(Constants.IDENTIFIER);
			sync.publish(CHANNEL, serializer.serialize(pm));
			Logger.debug("Synced PM from " + pm.getSender().getUsername());
		} catch (Exception ex) {
			Logger.warn("Failed to sync PM: " + ex);
		}
	}

	public void subscribe() {
		if (!settings.get().getSynchronization().isEnabled()) return;

		sync.subscribe(CHANNEL, (c, data) -> {
			try {
				Dialogue pm = serializer.deserialize(data, Dialogue.class);
				if (Constants.IDENTIFIER.equals(pm.getOrigin())) return;
				pm.getSender().setInteractor(platform);

				dialogueService.deliverLocally(pm);
			} catch (Exception ex) {
				Logger.warn("Bad PM-sync packet: " + ex);
			}
		});
	}
}
