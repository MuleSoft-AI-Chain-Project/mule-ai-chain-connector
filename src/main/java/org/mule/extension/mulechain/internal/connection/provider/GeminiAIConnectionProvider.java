package org.mule.extension.mulechain.internal.connection.provider;

import org.mule.extension.mulechain.internal.connection.GeminiAIChatConnection;
import org.mule.extension.mulechain.internal.connection.paramter.GeminiAIConnectionParameter;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alias("geminiAI")
@DisplayName("GeminiAI")
public class GeminiAIConnectionProvider implements CachedConnectionProvider<GeminiAIChatConnection> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GeminiAIConnectionProvider.class);

  @ParameterGroup(name = ParameterGroup.CONNECTION)
  private GeminiAIConnectionParameter geminiAIConnectionParameter;

  @Override
  public GeminiAIChatConnection connect() throws ConnectionException {
    try {
      GeminiAIChatConnection geminiAIConnection = new GeminiAIChatConnection(geminiAIConnectionParameter);
      geminiAIConnection.connect();
      return geminiAIConnection;
    } catch (ConnectionException e) {
      throw e;
    } catch (Exception e) {
      throw new ConnectionException("Gemini AI connection failed", e);
    }
  }

  @Override
  public void disconnect(GeminiAIChatConnection geminiAIConnection) {
    try {
      geminiAIConnection.disconnect();
    } catch (Exception e) {
      LOGGER.error("Failed to close connection", e);
    }
  }

  @Override
  public ConnectionValidationResult validate(GeminiAIChatConnection geminiAIConnection) {
    try {
      if (geminiAIConnection.isValid()) {
        return ConnectionValidationResult.success();
      } else {
        return ConnectionValidationResult.failure("Gemini AI Connection Invalid", null);
      }
    } catch (Exception e) {
      return ConnectionValidationResult.failure("Failed to validate connection to Gemini AI", e);
    }
  }
}
