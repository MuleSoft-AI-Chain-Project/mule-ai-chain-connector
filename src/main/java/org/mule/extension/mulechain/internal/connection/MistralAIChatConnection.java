package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;
import org.mule.extension.mulechain.internal.connection.paramter.MistralAIConnectionParameter;
import org.mule.runtime.api.connection.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.Duration.ofSeconds;

public class MistralAIChatConnection implements ChatConnection {

  private static final Logger LOGGER = LoggerFactory.getLogger(MistralAIChatConnection.class);
  private ChatLanguageModel model;
  private final MistralAIConnectionParameter mistralAIConnectionParameter;

  public MistralAIChatConnection(MistralAIConnectionParameter mistralAIConnectionParameter) {
    this.mistralAIConnectionParameter = mistralAIConnectionParameter;
  }

  @Override
  public ChatLanguageModel getModel() {
    return model;
  }

  @Override
  public void connect() throws ConnectionException {
    try {
      long durationInSec =
          mistralAIConnectionParameter.getLlmTimeoutUnit().toSeconds(mistralAIConnectionParameter.getLlmTimeout());
      model = MistralAiChatModel.builder()
          .apiKey(mistralAIConnectionParameter.getApiKey())
          .modelName(mistralAIConnectionParameter.getModelName())
          .maxTokens(mistralAIConnectionParameter.getMaxTokens())
          .temperature(mistralAIConnectionParameter.getTemperature())
          .topP(mistralAIConnectionParameter.getTopP())
          .timeout(ofSeconds(durationInSec))
          .logRequests(true)
          .logResponses(true)
          .build();
      testConnection(model);
    } catch (Exception e) {
      throw new ConnectionException("Failed to connect to Mistral AI.", e);
    }
  }

  @Override
  public void disconnect() {
    LOGGER.debug("MistralAI Connection disconnect called");
  }

  @Override
  public boolean isValid() {
    try {
      return testConnection(model);
    } catch (Exception e) {
      LOGGER.error("Failed to validate connection to Mistral AI.", e);
      return false;
    }
  }
}
