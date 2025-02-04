package org.mule.extension.mulechain.internal.providers;

import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiImageModelName;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class OpenAIModelNameProvider implements ValueProvider {

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    return ValueBuilder
        .getValuesFor(Stream.concat(Arrays.stream(OpenAiChatModelName.values()), Arrays.stream(OpenAiImageModelName.values()))
            .map(String::valueOf));
  }

}
