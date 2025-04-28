package org.mule.extension.mulechain.internal.connection.parameter;

import org.mule.extension.mulechain.internal.providers.OpenAIModelNameProvider;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;

public class OpenAIImageConnectionParameter {

  @Parameter
  @Password
  @Placement(order = 1)
  @Expression(ExpressionSupport.SUPPORTED)
  private String apiKey;

  @Parameter
  @OfValues(OpenAIModelNameProvider.class)
  @Placement(order = 2)
  private String openAIImageModelName;

  public String getApiKey() {
    return apiKey;
  }

  public String getOpenAIImageModelName() {
    return openAIImageModelName;
  }
}
