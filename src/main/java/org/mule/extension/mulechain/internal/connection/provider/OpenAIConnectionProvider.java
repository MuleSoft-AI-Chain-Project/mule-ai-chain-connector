package org.mule.extension.mulechain.internal.connection.provider;

import org.mule.extension.mulechain.internal.connection.OpenAIChatConnection;
import org.mule.extension.mulechain.internal.connection.paramter.OpenAIConnectionParameter;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alias("openAI")
@DisplayName("OpenAI")
public class OpenAIConnectionProvider implements CachedConnectionProvider<OpenAIChatConnection> {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpenAIConnectionProvider.class);

  @ParameterGroup(name = ParameterGroup.CONNECTION)
  private OpenAIConnectionParameter openAIConnectionParameter;

  @Override
  public OpenAIChatConnection connect() throws ConnectionException {
    try {
      OpenAIChatConnection openAIConnection = new OpenAIChatConnection(openAIConnectionParameter);
      openAIConnection.connect();
      return openAIConnection;
    } catch (ConnectionException e) {
      throw e;
    } catch (Exception e) {
      throw new ConnectionException("Open AI connection failed", e);
    }
  }

  @Override
  public void disconnect(OpenAIChatConnection openAIConnection) {
    try {
      openAIConnection.disconnect();
    } catch (Exception e) {
      LOGGER.error("Failed to close connection", e);
    }
  }

  @Override
  public ConnectionValidationResult validate(OpenAIChatConnection openAIConnection) {
    try {
      if (openAIConnection.isValid()) {
        return ConnectionValidationResult.success();
      } else {
        return ConnectionValidationResult.failure("Open AI Connection Invalid", null);
      }
    } catch (Exception e) {
      return ConnectionValidationResult.failure("Failed to validate connection to Open AI", e);
    }
  }
}
