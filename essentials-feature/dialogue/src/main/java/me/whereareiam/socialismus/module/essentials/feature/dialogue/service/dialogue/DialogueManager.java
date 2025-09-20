package me.whereareiam.socialismus.module.essentials.feature.dialogue.service.dialogue;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.Dialogue;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.Message;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.conversation.ConversationThread;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.ReplyValidator;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.conversation.ConversationService;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.message.MessageDeliveryCoordinator;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.message.MessageFactory;

import java.util.List;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class DialogueManager {
	private final ConversationService conversationService;
	private final MessageFactory messageFactory;
	private final ReplyValidator validator;
	private final DialogueErrorHandler errorHandler;
	private final MessageDeliveryCoordinator deliveryCoordinator;

	public void send(DummyPlayer sender, String rawRecipient, String rawMessage) {
		if (validator.isSelfMessage(sender, rawRecipient)) {
			errorHandler.sendSelfMessageError(sender);
			return;
		}

		ConversationThread thread = conversationService.getOrCreateConversation(
				sender.getUsername(), rawRecipient);

		Message message = messageFactory.createMessage(sender, rawRecipient, rawMessage, thread.getId());
		Dialogue dialogue = messageFactory.createDialogue(sender, rawRecipient, rawMessage);

		deliveryCoordinator.processMessageDelivery(dialogue, message);
		conversationService.addMessage(thread, message);
	}

	public void sendReply(DummyPlayer sender, String rawMessage) {
		if (!validator.isReplyEnabled()) {
			errorHandler.sendReplyError(sender, DialogueErrorHandler.ERROR_NO_PREVIOUS_SENDER);
			return;
		}

		Optional<String> lastPartner = conversationService.getLastConversationPartner(sender.getUsername());
		if (lastPartner.isEmpty()) {
			errorHandler.handleNoLastPartner(sender);
			return;
		}

		send(sender, lastPartner.get(), rawMessage);
	}

	public void sendReplyTo(DummyPlayer sender, String recipient, String rawMessage) {
		if (!validator.isReplyEnabled()) {
			errorHandler.sendReplyError(sender, DialogueErrorHandler.ERROR_NO_PREVIOUS_SENDER);
			return;
		}

		if (!validator.conversationExists(sender.getUsername(), recipient)) {
			errorHandler.sendReplyError(sender, DialogueErrorHandler.ERROR_NO_PREVIOUS_SENDER);
			return;
		}

		send(sender, recipient, rawMessage);
	}

	public void showRecentConversations(DummyPlayer sender) {
		List<String> conversations = conversationService.getPlayerConversations(sender.getUsername());
		if (conversations.isEmpty()) {
			errorHandler.sendReplyError(sender, DialogueErrorHandler.ERROR_NO_CONVERSATIONS);
		} else {
			errorHandler.sendConversationList(sender, conversations);
		}
	}

	public void clearConversationHistory(DummyPlayer sender) {
		List<String> conversations = conversationService.getPlayerConversations(sender.getUsername());
		deactivateAllConversations(conversations);
		errorHandler.sendHistoryClearedMessage(sender);
	}

	// Conversation management helper
	private void deactivateAllConversations(List<String> conversations) {
		for (String conversationKey : conversations) {
			String[] participants = conversationKey.split(":");
			if (participants.length == 2) {
				conversationService.deactivateConversation(participants[0], participants[1]);
			}
		}
	}
}
