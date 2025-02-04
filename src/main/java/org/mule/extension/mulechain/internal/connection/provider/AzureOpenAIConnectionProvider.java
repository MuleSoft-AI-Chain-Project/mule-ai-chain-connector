package org.mule.extension.mulechain.internal.connection.provider;

import org.mule.extension.mulechain.internal.connection.AzureOpenAIChatConnection;
import org.mule.extension.mulechain.internal.connection.paramter.AzureOpenAIConnectionParameter;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alias("azureOpenAI")
@DisplayName("AzureOpenAI")
public class AzureOpenAIConnectionProvider implements CachedConnectionProvider<AzureOpenAIChatConnection> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AzureOpenAIConnectionProvider.class);

  @ParameterGroup(name = ParameterGroup.CONNECTION)
  private AzureOpenAIConnectionParameter azureOpenAIConnectionParameter;

  @Override
  public AzureOpenAIChatConnection connect() throws ConnectionException {
    try {
      AzureOpenAIChatConnection azureOpenAIConnection = new AzureOpenAIChatConnection(azureOpenAIConnectionParameter);
      azureOpenAIConnection.connect();
      return azureOpenAIConnection;
    } catch (ConnectionException e) {
      throw e;
    } catch (Exception e) {
      throw new ConnectionException("Open AI connection failed", e);
    }
  }

  @Override
  public void disconnect(AzureOpenAIChatConnection azureOpenAIConnection) {
    try {
      azureOpenAIConnection.disconnect();
    } catch (Exception e) {
      LOGGER.error("Failed to close connection", e);
    }
  }

  @Override
  public ConnectionValidationResult validate(AzureOpenAIChatConnection azureOpenAIConnection) {
    try {
      if (azureOpenAIConnection.isValid()) {
        return ConnectionValidationResult.success();
      } else {
        return ConnectionValidationResult.failure("Open AI Connection Invalid", null);
      }
    } catch (Exception e) {
      return ConnectionValidationResult.failure("Failed to validate connection to Open AI", e);
    }
  }
}
