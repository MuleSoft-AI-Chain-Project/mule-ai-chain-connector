package org.mule.extension.mulechain.internal.connection.provider;

import org.mule.extension.mulechain.internal.connection.MistralAIChatConnection;
import org.mule.extension.mulechain.internal.connection.paramter.MistralAIConnectionParameter;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alias("mistralAI")
@DisplayName("MistralAI")
public class MistralAIConnectionProvider implements CachedConnectionProvider<MistralAIChatConnection> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MistralAIConnectionProvider.class);

  @ParameterGroup(name = ParameterGroup.CONNECTION)
  private MistralAIConnectionParameter mistralAIConnectionParameter;

  @Override
  public MistralAIChatConnection connect() throws ConnectionException {
    try {
      MistralAIChatConnection mistralAIConnection = new MistralAIChatConnection(mistralAIConnectionParameter);
      mistralAIConnection.connect();
      return mistralAIConnection;
    } catch (ConnectionException e) {
      throw e;
    } catch (Exception e) {
      throw new ConnectionException("Mistral AI connection failed", e);
    }
  }

  @Override
  public void disconnect(MistralAIChatConnection mistralAIConnection) {
    try {
      mistralAIConnection.disconnect();
    } catch (Exception e) {
      LOGGER.error("Failed to close connection", e);
    }
  }

  @Override
  public ConnectionValidationResult validate(MistralAIChatConnection mistralAIConnection) {
    try {
      if (mistralAIConnection.isValid()) {
        return ConnectionValidationResult.success();
      } else {
        return ConnectionValidationResult.failure("Mistral AI Connection Invalid", null);
      }
    } catch (Exception e) {
      return ConnectionValidationResult.failure("Failed to validate connection to Mistral AI", e);
    }
  }
}
