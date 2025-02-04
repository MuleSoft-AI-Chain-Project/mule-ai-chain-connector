package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import org.mule.extension.mulechain.internal.connection.paramter.OpenAIImageConnectionParameter;
import org.mule.runtime.api.connection.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.Duration.ofSeconds;

public class OpenAIImageConnection implements ImageConnection {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpenAIImageConnection.class);
  private ImageModel model;
  private final OpenAIImageConnectionParameter openAIImageConnectionParameter;

  public OpenAIImageConnection(OpenAIImageConnectionParameter openAIImageConnectionParameter) {
    this.openAIImageConnectionParameter = openAIImageConnectionParameter;
  }

  @Override
  public ImageModel getModel() {
    return model;
  }

  @Override
  public void connect() throws ConnectionException {
    try {
      model = OpenAiImageModel.builder()
          .modelName(openAIImageConnectionParameter.getModelName())
          .apiKey(openAIImageConnectionParameter.getApiKey())
          .build();
      testConnection(model);
    } catch (Exception e) {
      throw new ConnectionException("Failed to connect to Open AI.", e);
    }
  }

  @Override
  public void disconnect() {
    LOGGER.debug("OpenAI Connection disconnect called");
  }

  @Override
  public boolean isValid() {
    try {
      return testConnection(model);
    } catch (Exception e) {
      LOGGER.error("Failed to validate connection to Open AI.", e);
      return false;
    }
  }
}
