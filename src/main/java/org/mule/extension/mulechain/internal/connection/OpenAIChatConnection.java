package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.mule.extension.mulechain.internal.connection.paramter.OpenAIConnectionParameter;
import org.mule.runtime.api.connection.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.Duration.ofSeconds;

public class OpenAIChatConnection implements ChatConnection {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpenAIChatConnection.class);
  private ChatLanguageModel model;
  private final OpenAIConnectionParameter openAIConnectionParameter;

  public OpenAIChatConnection(OpenAIConnectionParameter openAIConnectionParameter) {
    this.openAIConnectionParameter = openAIConnectionParameter;
  }

  @Override
  public ChatLanguageModel getModel() {
    return model;
  }

  @Override
  public void connect() throws ConnectionException {
    try {
      long durationInSec = openAIConnectionParameter.getLlmTimeoutUnit().toSeconds(openAIConnectionParameter.getLlmTimeout());
      model = OpenAiChatModel.builder()
          .apiKey(openAIConnectionParameter.getApiKey())
          .modelName(openAIConnectionParameter.getModelName())
          .maxTokens(openAIConnectionParameter.getMaxTokens())
          .temperature(openAIConnectionParameter.getTemperature())
          .topP(openAIConnectionParameter.getTopP())
          .timeout(ofSeconds(durationInSec))
          .logRequests(true)
          .logResponses(true)
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
