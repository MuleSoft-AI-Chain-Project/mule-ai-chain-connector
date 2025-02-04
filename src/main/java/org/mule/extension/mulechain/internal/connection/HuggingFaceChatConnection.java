package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import org.mule.extension.mulechain.internal.connection.paramter.HuggingFaceConnectionParameter;
import org.mule.runtime.api.connection.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.Duration.ofSeconds;

public class HuggingFaceChatConnection implements ChatConnection {

  private static final Logger LOGGER = LoggerFactory.getLogger(HuggingFaceChatConnection.class);
  private ChatLanguageModel model;
  private final HuggingFaceConnectionParameter huggingFaceConnectionParameter;

  public HuggingFaceChatConnection(HuggingFaceConnectionParameter huggingFaceConnectionParameter) {
    this.huggingFaceConnectionParameter = huggingFaceConnectionParameter;
  }

  @Override
  public ChatLanguageModel getModel() {
    return model;
  }

  @Override
  public void connect() throws ConnectionException {
    try {
      long durationInSec =
          huggingFaceConnectionParameter.getLlmTimeoutUnit().toSeconds(huggingFaceConnectionParameter.getLlmTimeout());
      model = HuggingFaceChatModel.builder()
          .accessToken(huggingFaceConnectionParameter.getApiKey())
          .modelId(huggingFaceConnectionParameter.getModelName())
          .timeout(ofSeconds(durationInSec))
          .temperature(huggingFaceConnectionParameter.getTemperature())
          .maxNewTokens(huggingFaceConnectionParameter.getMaxTokens())
          .waitForModel(true)
          .build();
      testConnection(model);
    } catch (Exception e) {
      throw new ConnectionException("Failed to connect to Hugging Face AI.", e);
    }
  }

  @Override
  public void disconnect() {
    LOGGER.debug("Hugging Face AI Connection disconnect called");
  }

  @Override
  public boolean isValid() {
    try {
      return testConnection(model);
    } catch (Exception e) {
      LOGGER.error("Failed to validate connection to Hugging Face AI.", e);
      return false;
    }
  }
}
