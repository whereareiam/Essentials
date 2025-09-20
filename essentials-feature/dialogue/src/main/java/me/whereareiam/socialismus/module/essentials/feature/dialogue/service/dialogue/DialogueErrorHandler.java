package me.whereareiam.socialismus.module.essentials.feature.dialogue.service.dialogue;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.conversation.ConversationRenderer;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.conversation.ConversationService;

import java.util.List;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class DialogueErrorHandler {
	private final ConversationService conversationService;
	private final ConversationRenderer renderer;

	// Error type constants
	public static final String ERROR_NO_PREVIOUS_SENDER = "no_previous_sender";
	public static final String ERROR_NO_CONVERSATIONS = "no_conversations";
	public static final String SUCCESS_HISTORY_CLEARED = "Conversation history cleared.";

	public void sendSelfMessageError(DummyPlayer sender) {
		sender.sendMessage(renderer.renderSelfMessageError(sender));
	}

	public void sendReplyError(DummyPlayer sender, String errorType) {
		sender.sendMessage(renderer.renderReplyError(sender, errorType));
	}

	public void handleNoLastPartner(DummyPlayer sender) {
		List<String> conversations = conversationService.getPlayerConversations(sender.getUsername());
		if (conversations.isEmpty()) {
			sendReplyError(sender, ERROR_NO_CONVERSATIONS);
			return;
		}

		sendConversationList(sender, conversations);
	}

	public void sendConversationList(DummyPlayer sender, List<String> conversations) {
		sender.sendMessage(renderer.renderConversationList(sender, conversations));
	}

	public void sendHistoryClearedMessage(DummyPlayer sender) {
		sender.sendMessage(renderer.renderHistoryClearedMessage(sender));
	}
}
