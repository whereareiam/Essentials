package me.whereareiam.socialismus.module.essentials.feature.dialogue.service.conversation;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.service.resource.CacheService;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.Message;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.conversation.ConversationKey;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.conversation.ConversationThread;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ConversationService {
	private static final String CONVERSATION_PREFIX = "pm:conversation:";
	private static final String PLAYER_CONVERSATIONS_PREFIX = "pm:player_conversations:";

	private final CacheService cache;
	private final Provider<DialogueSettings> settings;

	public ConversationThread getOrCreateConversation(String player1, String player2) {
		ConversationKey key = ConversationKey.of(player1, player2);
		String cacheKey = CONVERSATION_PREFIX + key.getKey();

		return cache.get(cacheKey, ConversationThread.class)
				.orElseGet(() -> createNewConversation(player1, player2, key));
	}

	private ConversationThread createNewConversation(String player1, String player2, ConversationKey key) {
		ConversationThread thread = ConversationThread.builder()
				.id(UUID.randomUUID().toString())
				.participants(Set.of(player1, player2))
				.active(true)
				.messageCount(0)
				.build();

		String cacheKey = CONVERSATION_PREFIX + key.getKey();
		Duration ttl = Duration.parse(settings.get().getMessageHistory().getTtl());
		cache.put(cacheKey, thread, ttl);

		// Track conversation for each player
		addPlayerConversation(player1, key.getKey());
		addPlayerConversation(player2, key.getKey());

		return thread;
	}

	public void addMessage(ConversationThread thread, Message message) {
		DialogueSettings.MessageHistory historySettings = settings.get().getMessageHistory();
		
		// Check if message history is enabled
		if (!historySettings.isEnabled()) {
			return;
		}

		// Enforce max messages per conversation
		int maxMessages = historySettings.getMaxMessagesPerConversation();
		if (thread.getMessages().size() >= maxMessages) {
			// Remove oldest message to make room
			thread.getMessages().remove(0);
		}

		thread.addMessage(message);
		message.setConversationId(thread.getId());

		// Update cache
		ConversationKey key = ConversationKey.of(thread.getParticipants());
		String cacheKey = CONVERSATION_PREFIX + key.getKey();
		Duration ttl = Duration.parse(historySettings.getTtl());
		cache.put(cacheKey, thread, ttl);

		// Async update for performance
		CompletableFuture.runAsync(() -> updatePlayerConversations(thread));
	}

	public Optional<ConversationThread> getConversation(String player1, String player2) {
		ConversationKey key = ConversationKey.of(player1, player2);
		String cacheKey = CONVERSATION_PREFIX + key.getKey();

		return cache.get(cacheKey, ConversationThread.class);
	}

	public List<String> getPlayerConversations(String playerName) {
		String cacheKey = PLAYER_CONVERSATIONS_PREFIX + playerName.toLowerCase();
		List<String> conversations = new ArrayList<>(cache.get(cacheKey));
		
		// Enforce max conversations limit
		int maxConversations = settings.get().getMessageHistory().getMaxConversations();
		if (conversations.size() > maxConversations) {
			// Keep only the most recent conversations
			conversations = conversations.subList(
					conversations.size() - maxConversations, 
					conversations.size()
			);
		}
		
		return conversations;
	}

	public Optional<String> getLastConversationPartner(String playerName) {
		List<String> conversations = getPlayerConversations(playerName);
		if (conversations.isEmpty())
			return Optional.empty();

		// Get the most recent conversation
		String lastConversationKey = conversations.get(conversations.size() - 1);
		String[] participants = lastConversationKey.split(":");
		if (participants.length != 2) {
			return Optional.empty();
		}

		// Return the other participant (not the current player)
		String otherParticipant = participants[0].equals(playerName.toLowerCase())
				? participants[1]
				: participants[0];

		return Optional.of(otherParticipant);
	}


	private void addPlayerConversation(String playerName, String conversationKey) {
		String cacheKey = PLAYER_CONVERSATIONS_PREFIX + playerName.toLowerCase();
		
		// Get existing conversations
		Set<String> existingConversations = cache.get(cacheKey);
		List<String> conversations = new ArrayList<>(existingConversations);
		
		// Remove if already exists (to re-add at end as most recent)
		conversations.remove(conversationKey);
		
		// Add to end as most recent
		conversations.add(conversationKey);
		
		// Enforce max conversations limit
		int maxConversations = settings.get().getMessageHistory().getMaxConversations();
		if (conversations.size() > maxConversations) {
			// Keep only the most recent conversations
			conversations = conversations.subList(
					conversations.size() - maxConversations,
					conversations.size()
			);
		}
		
		// Update cache (replace with limited list)
		cache.delete(cacheKey);
		for (String conv : conversations) {
			cache.add(cacheKey, conv);
		}
	}

	private void updatePlayerConversations(ConversationThread thread) {
		for (String participant : thread.getParticipants())
			addPlayerConversation(participant, ConversationKey.of(thread.getParticipants()).getKey());
	}

	public void deactivateConversation(String player1, String player2) {
		getConversation(player1, player2).ifPresent(thread -> {
			thread.setActive(false);
			ConversationKey key = ConversationKey.of(player1, player2);
			cache.put(
					CONVERSATION_PREFIX + key.getKey(),
					thread,
					Duration.parse(settings.get().getMessageHistory().getTtl())
			);
		});
	}
}
