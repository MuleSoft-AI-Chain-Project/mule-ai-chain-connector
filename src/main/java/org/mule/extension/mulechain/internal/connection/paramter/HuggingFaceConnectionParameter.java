package org.mule.extension.mulechain.internal.connection.paramter;

import org.mule.extension.mulechain.internal.providers.HuggingFaceModelNameProvider;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;

public class HuggingFaceConnectionParameter extends BaseConnectionParameter {

  @Parameter
  @Password
  @Placement(order = 1)
  @Expression(ExpressionSupport.SUPPORTED)
  private String apiKey;

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @OfValues(HuggingFaceModelNameProvider.class)
  @Placement(order = 2)
  private String modelName;

  public String getApiKey() {
    return apiKey;
  }

  public String getModelName() {
    return modelName;
  }
}
