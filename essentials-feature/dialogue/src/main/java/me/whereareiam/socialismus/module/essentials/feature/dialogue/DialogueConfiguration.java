package me.whereareiam.socialismus.module.essentials.feature.dialogue;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import me.whereareiam.socialismus.module.essentials.api.model.feature.Feature;
import me.whereareiam.socialismus.module.essentials.api.output.FeatureInitializer;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.config.DialogueCommands;
import me.whereareiam.socialismus.module.essentials.feature.dialogue.provider.DialogueCommandsProvider;

public class DialogueConfiguration extends AbstractModule {
	@Override
	protected void configure() {
		bind(new TypeLiteral<FeatureInitializer<? extends Feature>>() {}).to(Dialogue.class);
		bind(DialogueCommands.class).toProvider(DialogueCommandsProvider.class);
	}
}
