/**
 * (c) 2003-2024 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.mulechain.internal.llm;

import java.util.Arrays;
import java.util.Set;

import org.mule.extension.mulechain.internal.llm.type.LangchainLLMType;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

public class LangchainLLMTypeProvider implements ValueProvider {

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    return ValueBuilder.getValuesFor(Arrays.stream(LangchainLLMType.values()).map(LangchainLLMType::name));
  }

}
