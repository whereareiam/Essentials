package me.whereareiam.socialismus.module.essentials.feature.dialogue.model;

import me.whereareiam.socialismus.module.essentials.feature.dialogue.model.conversation.ConversationKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ConversationKeyTest {

	@Test
	void shouldCreateConsistentKeyRegardlessOfOrder() {
		// Given
		String player1 = "alice";
		String player2 = "bob";

		// When
		ConversationKey key1 = ConversationKey.of(player1, player2);
		ConversationKey key2 = ConversationKey.of(player2, player1);

		// Then
		assertThat(key1).isEqualTo(key2);
		assertThat(key1.getKey()).isEqualTo(key2.getKey());
		assertThat(key1.hashCode()).isEqualTo(key2.hashCode());
	}

	@ParameterizedTest
	@MethodSource("caseInsensitiveTestData")
	void shouldHandleCaseInsensitiveNames(String name1, String name2, String expectedKey) {
		// When
		ConversationKey key = ConversationKey.of(name1, name2);

		// Then
		assertThat(key.getKey()).isEqualTo(expectedKey);
	}

	private static Stream<Arguments> caseInsensitiveTestData() {
		return Stream.of(
				Arguments.of("Alice", "Bob", "alice:bob"),
				Arguments.of("ALICE", "bob", "alice:bob"),
				Arguments.of("alice", "BOB", "alice:bob"),
				Arguments.of("AlIcE", "BoB", "alice:bob")
		);
	}

	@Test
	void shouldGenerateValidKeyFromSet() {
		// Given
		Set<String> participants = Set.of("Charlie", "Alice", "Bob");

		// When
		ConversationKey key = ConversationKey.of(participants);

		// Then
		// Should be sorted alphabetically
		assertThat(key.getKey()).isEqualTo("alice:bob:charlie");
	}

	@Test
	void shouldGenerateConsistentKeyFromSetRegardlessOfOrder() {
		// Given
		Set<String> participants1 = Set.of("bob", "alice");
		Set<String> participants2 = Set.of("alice", "bob");

		// When
		ConversationKey key1 = ConversationKey.of(participants1);
		ConversationKey key2 = ConversationKey.of(participants2);

		// Then
		assertThat(key1).isEqualTo(key2);
		assertThat(key1.getKey()).isEqualTo("alice:bob");
	}

	@Test
	void shouldHandleEmptyAndSingleParticipant() {
		// Given & When
		ConversationKey singleKey = ConversationKey.of(Set.of("alice"));
		ConversationKey emptyKey = ConversationKey.of(Set.of());

		// Then
		assertThat(singleKey.getKey()).isEqualTo("alice");
		assertThat(emptyKey.getKey()).isEmpty();
	}

	@Test
	void shouldImplementEqualsAndHashCodeCorrectly() {
		// Given
		ConversationKey key1 = ConversationKey.of("alice", "bob");
		ConversationKey key2 = ConversationKey.of("alice", "bob");
		ConversationKey key3 = ConversationKey.of("alice", "charlie");

		// Then
		assertThat(key1)
				.isEqualTo(key2)
				.isNotEqualTo(key3)
				.isNotEqualTo(null)
				.isNotEqualTo("some string");

		assertThat(key1.hashCode()).isEqualTo(key2.hashCode());
		assertThat(key1.hashCode()).isNotEqualTo(key3.hashCode());
	}

	@Test
	void shouldImplementToStringCorrectly() {
		// Given
		ConversationKey key = ConversationKey.of("alice", "bob");

		// When
		String toString = key.toString();

		// Then
		assertThat(toString).isEqualTo("ConversationKey(key=alice:bob)");
	}
}
