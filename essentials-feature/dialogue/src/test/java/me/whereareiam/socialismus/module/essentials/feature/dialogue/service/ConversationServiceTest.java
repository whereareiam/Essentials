package me.whereareiam.socialismus.module.essentials.feature.dialogue.service;

import com.google.inject.Provider;
import me.whereareiam.socialismus.api.output.resource.CacheService;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.TestDataBuilder;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.Message;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.conversation.ConversationThread;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.conversation.ConversationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ConversationServiceTest {

	@Mock
	private CacheService cacheService;

	@Mock
	private Provider<DialogueSettings> settingsProvider;
	private ConversationService conversationService;

	@BeforeEach
	void setUp() {
		conversationService = new ConversationService(cacheService, settingsProvider);
	}

	@Test
	void shouldCreateNewConversationWhenNotExists() {
		// Given
		String player1 = "alice";
		String player2 = "bob";
		when(cacheService.get(anyString(), eq(ConversationThread.class)))
				.thenReturn(Optional.empty());

		// When
		ConversationThread result = conversationService.getOrCreateConversation(player1, player2);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getParticipants()).containsExactlyInAnyOrder(player1, player2);
		assertThat(result.isActive()).isTrue();
		assertThat(result.getMessageCount()).isZero();
		assertThat(result.getId()).isNotNull();

		// Verify conversation was cached
		verify(cacheService).put(
				eq("pm:conversation:alice:bob"),
				eq(result),
				eq(Duration.parse("PT24H"))
		);
	}

	@Test
	void shouldReturnExistingConversationWhenExists() {
		// Given
		String player1 = "alice";
		String player2 = "bob";
		ConversationThread existingThread = TestDataBuilder.conversationThreadBetween(player1, player2)
				.messageCount(5)
				.build();

		when(cacheService.get("pm:conversation:alice:bob", ConversationThread.class))
				.thenReturn(Optional.of(existingThread));

		// When
		ConversationThread result = conversationService.getOrCreateConversation(player1, player2);

		// Then
		assertThat(result).isSameAs(existingThread);
		assertThat(result.getMessageCount()).isEqualTo(5);

		// Verify no new conversation was created
		verify(cacheService, never()).put(anyString(), any(ConversationThread.class), any(Duration.class));
	}

	@Test
	void shouldAddMessageToConversationAndUpdateCache() {
		// Given
		ConversationThread thread = TestDataBuilder.conversationThread()
				.participants(Set.of("alice", "bob"))
				.messageCount(2)
				.build();

		Message message = TestDataBuilder.messageFrom(
				TestDataBuilder.mockPlayer("alice"), "bob"
		).build();

		// When
		conversationService.addMessage(thread, message);

		// Then
		assertThat(thread.getMessages()).contains(message);
		assertThat(thread.getLastSender()).isEqualTo("alice");
		assertThat(thread.getMessageCount()).isEqualTo(3);
		assertThat(message.getConversationId()).isEqualTo(thread.getId());

		// Verify conversation was updated in cache
		verify(cacheService).put(
				eq("pm:conversation:alice:bob"),
				eq(thread),
				any(Duration.class)
		);
	}

	@Test
	void shouldGetPlayerConversations() {
		// Given
		String playerName = "alice";
		Set<String> conversationKeys = Set.of("alice:bob", "alice:charlie");
		when(cacheService.get("pm:player_conversations:alice"))
				.thenReturn(conversationKeys);

		// When
		List<String> result = conversationService.getPlayerConversations(playerName);

		// Then
		assertThat(result).containsExactlyInAnyOrderElementsOf(conversationKeys);
	}

	@Test
	void shouldReturnLastConversationPartner() {
		// Given
		String playerName = "alice";
		// The last conversation in the list should be the most recent
		when(cacheService.get("pm:player_conversations:alice"))
				.thenReturn(Set.of("alice:bob", "alice:charlie"));

		// When
		Optional<String> result = conversationService.getLastConversationPartner(playerName);

		// Then
		// Should return one of the conversation partners (charlie or bob)
		assertThat(result).isPresent();
		assertThat(result.get()).isIn("bob", "charlie");
	}

	@Test
	void shouldReturnEmptyWhenNoConversations() {
		// Given
		String playerName = "alice";
		when(cacheService.get("pm:player_conversations:alice"))
				.thenReturn(Set.of());

		// When
		Optional<String> result = conversationService.getLastConversationPartner(playerName);

		// Then
		assertThat(result).isEmpty();
	}

	@Test
	void shouldDeactivateConversation() {
		// Given
		String player1 = "alice";
		String player2 = "bob";
		ConversationThread activeThread = TestDataBuilder.conversationThreadBetween(player1, player2)
				.active(true)
				.build();

		when(cacheService.get("pm:conversation:alice:bob", ConversationThread.class))
				.thenReturn(Optional.of(activeThread));

		// When
		conversationService.deactivateConversation(player1, player2);

		// Then
		assertThat(activeThread.isActive()).isFalse();
		verify(cacheService).put(
				eq("pm:conversation:alice:bob"),
				eq(activeThread),
				eq(Duration.parse("PT24H"))
		);
	}
}
