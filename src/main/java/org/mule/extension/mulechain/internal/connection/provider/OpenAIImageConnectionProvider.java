package org.mule.extension.mulechain.internal.connection.provider;

import org.mule.extension.mulechain.internal.connection.OpenAIImageConnection;
import org.mule.extension.mulechain.internal.connection.paramter.OpenAIImageConnectionParameter;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alias("openAIImage")
@DisplayName("OpenAI Image")
public class OpenAIImageConnectionProvider implements CachedConnectionProvider<OpenAIImageConnection> {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpenAIImageConnectionProvider.class);

  @ParameterGroup(name = ParameterGroup.CONNECTION)
  private OpenAIImageConnectionParameter openAIImageConnectionParameter;

  @Override
  public OpenAIImageConnection connect() throws ConnectionException {
    try {
      OpenAIImageConnection openAIImageConnection = new OpenAIImageConnection(openAIImageConnectionParameter);
      openAIImageConnection.connect();
      return openAIImageConnection;
    } catch (ConnectionException e) {
      throw e;
    } catch (Exception e) {
      throw new ConnectionException("Open AI connection failed", e);
    }
  }

  @Override
  public void disconnect(OpenAIImageConnection openAIImageConnection) {
    try {
      openAIImageConnection.disconnect();
    } catch (Exception e) {
      LOGGER.error("Failed to close connection", e);
    }
  }

  @Override
  public ConnectionValidationResult validate(OpenAIImageConnection openAIImageConnection) {
    try {
      if (openAIImageConnection.isValid()) {
        return ConnectionValidationResult.success();
      } else {
        return ConnectionValidationResult.failure("Open AI Connection Invalid", null);
      }
    } catch (Exception e) {
      return ConnectionValidationResult.failure("Failed to validate connection to Open AI", e);
    }
  }
}
