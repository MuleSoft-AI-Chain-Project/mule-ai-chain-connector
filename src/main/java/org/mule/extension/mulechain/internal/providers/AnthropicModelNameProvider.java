package org.mule.extension.mulechain.internal.providers;

import dev.langchain4j.model.anthropic.AnthropicChatModelName;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.Arrays;
import java.util.Set;

public class AnthropicModelNameProvider implements ValueProvider {

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    return ValueBuilder.getValuesFor(Arrays.stream(AnthropicChatModelName.values()).map(String::valueOf));
  }

}
