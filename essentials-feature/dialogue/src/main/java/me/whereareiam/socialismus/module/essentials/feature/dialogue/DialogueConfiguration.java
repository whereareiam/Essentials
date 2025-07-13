package me.whereareiam.socialismus.module.essentials.feature.dialogue;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import me.whereareiam.socialismus.module.essentials.api.model.feature.Feature;
import me.whereareiam.socialismus.module.essentials.api.output.FeatureInitializer;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueCommands;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueMessages;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueSettings;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.provider.DialogueCommandsProvider;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.provider.DialogueMessagesProvider;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.provider.DialogueSettingsProvider;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.LocalDialogueService;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.service.SyncDialogueService;

public class DialogueConfiguration extends AbstractModule {
	@Override
	protected void configure() {
		bind(DialogueSettings.class).toProvider(DialogueSettingsProvider.class);
		bind(DialogueMessages.class).toProvider(DialogueMessagesProvider.class);
		bind(DialogueCommands.class).toProvider(DialogueCommandsProvider.class);
		bind(LocalDialogueService.class).asEagerSingleton();
		bind(SyncDialogueService.class).asEagerSingleton();

		bind(new TypeLiteral<FeatureInitializer<? extends Feature>>() {}).to(Dialogue.class);
	}
}
