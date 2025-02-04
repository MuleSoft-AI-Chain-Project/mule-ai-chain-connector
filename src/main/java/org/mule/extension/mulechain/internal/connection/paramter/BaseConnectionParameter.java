package org.mule.extension.mulechain.internal.connection.paramter;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

import java.util.concurrent.TimeUnit;

public abstract class BaseConnectionParameter {

  @Parameter
  @Placement(order = 4)
  @Optional(defaultValue = "0.7")
  private final double temperature = 0.7;

  @Parameter
  @Placement(order = 5)
  @Optional(defaultValue = "0.95")
  private final double topP = 0.95;


  @Parameter
  @Placement(order = 6)
  @Optional(defaultValue = "60")
  @DisplayName("LLM timeout")
  private final int llmTimeout = 60;

  @Parameter
  @Optional(defaultValue = "SECONDS")
  @Placement(order = 7)
  @DisplayName("LLM timeout unit")
  @Summary("Time unit to be used in the LLM Timeout")
  private final TimeUnit llmTimeoutUnit = TimeUnit.SECONDS;

  @Parameter
  @Placement(order = 8)
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional(defaultValue = "500")
  private final int maxTokens = 500;

  public double getTemperature() {
    return temperature;
  }

  public double getTopP() {
    return topP;
  }

  public int getLlmTimeout() {
    return llmTimeout;
  }

  public TimeUnit getLlmTimeoutUnit() {
    return llmTimeoutUnit;
  }

  public int getMaxTokens() {
    return maxTokens;
  }
}
