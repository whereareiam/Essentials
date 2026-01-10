package me.whereareiam.socialismus.module.essentials.feature.dialogue.template;

import com.google.inject.Singleton;
import me.whereareiam.configura.TemplateProvider;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;

@Singleton
public class DialogueSettingsTemplate implements TemplateProvider<DialogueSettings> {
	@Override
	public DialogueSettings supply(DialogueSettings dialogueSettings) {
		DialogueSettings.Synchronization synchronization = new DialogueSettings.Synchronization();
		synchronization.setEnabled(false);
		dialogueSettings.setSynchronization(synchronization);

		DialogueSettings.MessageHistory messageHistory = new DialogueSettings.MessageHistory();
		messageHistory.setEnabled(true);
		messageHistory.setTtl("PT24H");
		messageHistory.setMaxConversations(10);
		messageHistory.setMaxMessagesPerConversation(50);
		dialogueSettings.setMessageHistory(messageHistory);

		DialogueSettings.Reply reply = new DialogueSettings.Reply();
		reply.setEnabled(true);
		reply.setTimeout("PT5M");
		reply.setShowRecentOnError(true);
		reply.setAllowReplyToOffline(false);
		dialogueSettings.setReply(reply);

		return dialogueSettings;
	}
}
