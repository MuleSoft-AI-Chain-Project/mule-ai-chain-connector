package org.mule.extension.mulechain.internal.connection.parameter;

import org.mule.extension.mulechain.internal.providers.AnthropicModelNameProvider;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;

public class AnthropicConnectionParameter extends BaseConnectionParameter {

  @Parameter
  @Password
  @Placement(order = 1)
  @Expression(ExpressionSupport.SUPPORTED)
  private String apiKey;

  @Parameter
  @OfValues(AnthropicModelNameProvider.class)
  @Placement(order = 2)
  private String anthropicModelName;

  public String getApiKey() {
    return apiKey;
  }

  public String getAnthropicModelName() {
    return anthropicModelName;
  }
}
