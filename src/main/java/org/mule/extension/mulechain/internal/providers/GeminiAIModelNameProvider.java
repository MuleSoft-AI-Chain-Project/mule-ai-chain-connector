package org.mule.extension.mulechain.internal.providers;

import org.mule.extension.mulechain.internal.llm.type.LangchainLLMType;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.Arrays;
import java.util.Set;

public class GeminiAIModelNameProvider implements ValueProvider {

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    return ValueBuilder.getValuesFor(Arrays.stream(LangchainLLMType.GeminiModelName.values()).map(String::valueOf));
  }

}
