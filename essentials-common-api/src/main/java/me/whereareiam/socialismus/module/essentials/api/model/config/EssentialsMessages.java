package me.whereareiam.socialismus.module.essentials.api.model.config;

import com.google.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Singleton
public class EssentialsMessages {
	private Features features;

	@Getter
	@Setter
	@ToString
	public static class Features {
		private PrivateMessage privateMessage;

		@Getter
		@Setter
		@ToString
		public static class PrivateMessage {
			private String noRecipient;
		}
	}
}
