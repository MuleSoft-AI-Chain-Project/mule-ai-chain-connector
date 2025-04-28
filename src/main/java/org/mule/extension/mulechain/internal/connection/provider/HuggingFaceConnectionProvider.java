package org.mule.extension.mulechain.internal.connection.provider;

import org.mule.extension.mulechain.internal.connection.HuggingFaceChatConnection;
import org.mule.extension.mulechain.internal.connection.parameter.HuggingFaceConnectionParameter;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.ExternalLib;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mule.runtime.api.meta.ExternalLibraryType.JAR;

@ExternalLib(
    name = "HuggingFace Library",
    description = "HuggingFace Library",
    type = JAR,
    nameRegexpMatcher = "langchain4j-hugging-face.*.jar")
@Alias("huggingFace")
@DisplayName("HuggingFace")
public class HuggingFaceConnectionProvider implements CachedConnectionProvider<HuggingFaceChatConnection> {

  private static final Logger LOGGER = LoggerFactory.getLogger(HuggingFaceConnectionProvider.class);

  @ParameterGroup(name = ParameterGroup.CONNECTION)
  private HuggingFaceConnectionParameter huggingFaceConnectionParameter;

  @Override
  public HuggingFaceChatConnection connect() throws ConnectionException {
    try {
      HuggingFaceChatConnection huggingFaceConnection = new HuggingFaceChatConnection(huggingFaceConnectionParameter);
      huggingFaceConnection.connect();
      return huggingFaceConnection;
    } catch (ConnectionException e) {
      throw e;
    } catch (Exception e) {
      throw new ConnectionException("Hugging Face AI connection failed", e);
    }
  }

  @Override
  public void disconnect(HuggingFaceChatConnection huggingFaceConnection) {
    try {
      huggingFaceConnection.disconnect();
    } catch (Exception e) {
      LOGGER.error("Failed to close connection", e);
    }
  }

  @Override
  public ConnectionValidationResult validate(HuggingFaceChatConnection huggingFaceConnection) {
    try {
      if (huggingFaceConnection.isValid()) {
        return ConnectionValidationResult.success();
      } else {
        return ConnectionValidationResult.failure("Hugging Face AI Connection Invalid", null);
      }
    } catch (Exception e) {
      return ConnectionValidationResult.failure("Failed to validate connection to Hugging Face AI", e);
    }
  }
}
