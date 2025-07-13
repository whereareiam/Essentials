package me.whereareiam.socialismus.module.essentials.feature.dialogue.sync;

import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.Dialogue;

public interface PrivateMessageSyncBus {
	void publish(Dialogue pm);

	void subscribe();
}
