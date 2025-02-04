package org.mule.extension.mulechain.internal.connection.paramter;

import org.mule.extension.mulechain.internal.providers.OllamaModelNameProvider;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;

public class OllamaConnectionParameter extends BaseConnectionParameter {

  @Parameter
  @Password
  @Placement(order = 1)
  @Expression(ExpressionSupport.SUPPORTED)
  private String baseUrl;

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @OfValues(OllamaModelNameProvider.class)
  @Placement(order = 2)
  private String modelName;

  public String getBaseUrl() {
    return baseUrl;
  }

  public String getModelName() {
    return modelName;
  }
}
