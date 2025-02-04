package org.mule.extension.mulechain.internal.connection.provider;

import org.mule.extension.mulechain.internal.connection.GroqAIChatConnection;
import org.mule.extension.mulechain.internal.connection.paramter.GroqAIConnectionParameter;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alias("groqAI")
@DisplayName("GroqOpenAI")
public class GroqOpenAIConnectionProvider implements CachedConnectionProvider<GroqAIChatConnection> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroqOpenAIConnectionProvider.class);

  @ParameterGroup(name = ParameterGroup.CONNECTION)
  private GroqAIConnectionParameter groqAIConnectionParameter;

  @Override
  public GroqAIChatConnection connect() throws ConnectionException {
    try {
      GroqAIChatConnection groqAIConnection = new GroqAIChatConnection(groqAIConnectionParameter);
      groqAIConnection.connect();
      return groqAIConnection;
    } catch (ConnectionException e) {
      throw e;
    } catch (Exception e) {
      throw new ConnectionException("Groq Open AI connection failed", e);
    }
  }

  @Override
  public void disconnect(GroqAIChatConnection groqAIConnection) {
    try {
      groqAIConnection.disconnect();
    } catch (Exception e) {
      LOGGER.error("Failed to close connection", e);
    }
  }

  @Override
  public ConnectionValidationResult validate(GroqAIChatConnection groqAIConnection) {
    try {
      if (groqAIConnection.isValid()) {
        return ConnectionValidationResult.success();
      } else {
        return ConnectionValidationResult.failure("Groq Open AI Connection Invalid", null);
      }
    } catch (Exception e) {
      return ConnectionValidationResult.failure("Failed to validate connection to Groq Open AI", e);
    }
  }
}
