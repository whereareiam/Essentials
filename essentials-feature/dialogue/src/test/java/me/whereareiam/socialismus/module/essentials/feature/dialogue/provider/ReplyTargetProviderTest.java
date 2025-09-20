package me.whereareiam.socialismus.module.essentials.feature.dialogue.provider;

import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.conversation.ConversationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReplyTargetProviderTest {

	@Mock
	private ConversationService conversationService;

	private ReplyTargetProvider replyTargetProvider;

	@BeforeEach
	void setUp() {
		replyTargetProvider = new ReplyTargetProvider(conversationService);
	}

	@Test
	void shouldReturnRecentConversationPartners() {
		// Given
		String playerName = "alice";
		List<String> conversations = List.of("alice:bob", "alice:charlie", "alice:david");
		when(conversationService.getPlayerConversations(playerName))
				.thenReturn(conversations);

		// When
		Collection<String> result = replyTargetProvider.suggestReplyTargets(playerName);

		// Then
		assertThat(result).containsExactlyElementsOf(conversations);
	}

	@Test
	void shouldLimitSuggestionsToTen() {
		// Given
		String playerName = "alice";
		List<String> manyConversations = IntStream.range(0, 15)
				.mapToObj(i -> "alice:player" + i)
				.toList();

		when(conversationService.getPlayerConversations(playerName))
				.thenReturn(manyConversations);

		// When
		Collection<String> result = replyTargetProvider.suggestReplyTargets(playerName);

		// Then
		assertThat(result).hasSize(10);
		assertThat(result).containsExactly(
				"alice:player0", "alice:player1", "alice:player2", "alice:player3", "alice:player4",
				"alice:player5", "alice:player6", "alice:player7", "alice:player8", "alice:player9"
		);
	}

	@Test
	void shouldReturnEmptyWhenNoConversations() {
		// Given
		String playerName = "alice";
		when(conversationService.getPlayerConversations(playerName))
				.thenReturn(List.of());

		// When
		Collection<String> result = replyTargetProvider.suggestReplyTargets(playerName);

		// Then
		assertThat(result).isEmpty();
	}

	@Test
	void shouldReturnSameForBothSuggestionMethods() {
		// Given
		String playerName = "alice";
		List<String> conversations = List.of("alice:bob", "alice:charlie");
		when(conversationService.getPlayerConversations(playerName))
				.thenReturn(conversations);

		// When
		Collection<String> replyTargets = replyTargetProvider.suggestReplyTargets(playerName);
		Collection<String> conversationPartners = replyTargetProvider.suggestConversationPartners(playerName);

		// Then
		assertThat(replyTargets).isEqualTo(conversationPartners);
	}
}
