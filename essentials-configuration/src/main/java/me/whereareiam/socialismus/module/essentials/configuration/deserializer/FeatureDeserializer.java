package me.whereareiam.socialismus.module.essentials.configuration.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.input.registry.ObjectMapperRegistry;
import me.whereareiam.socialismus.module.essentials.api.model.feature.CommandFeature;
import me.whereareiam.socialismus.module.essentials.api.model.feature.Feature;

import java.io.IOException;

@Singleton
@SuppressWarnings("unused")
public class FeatureDeserializer extends JsonDeserializer<Feature> {
	@Inject
	public FeatureDeserializer(ObjectMapperRegistry registry) {
		registry.addDeserializer(Feature.class, this);
	}

	@Override
	public Feature deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
		ObjectCodec codec = parser.getCodec();
		JsonNode root = codec.readTree(parser);

		if (root.has("registerCommands"))
			return codec.treeToValue(root, CommandFeature.class);

		return codec.treeToValue(root, Feature.class);
	}
}
