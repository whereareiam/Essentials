package me.whereareiam.socialismus.module.essentials.feature.dialogue.service.conversation;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.whereareiam.socialismus.api.output.resource.CacheService;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.Message;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.conversation.ConversationKey;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.conversation.ConversationThread;

import java.time.Duration;
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
		thread.addMessage(message);
		message.setConversationId(thread.getId());

		// Update cache
		ConversationKey key = ConversationKey.of(thread.getParticipants());
		String cacheKey = CONVERSATION_PREFIX + key.getKey();
		Duration ttl = Duration.parse(settings.get().getMessageHistory().getTtl());
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

		return cache.get(cacheKey).stream().toList();
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
		cache.add(cacheKey, conversationKey);
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
