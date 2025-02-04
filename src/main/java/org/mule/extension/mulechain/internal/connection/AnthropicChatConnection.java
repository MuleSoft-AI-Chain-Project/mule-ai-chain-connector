package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.mule.extension.mulechain.internal.connection.paramter.AnthropicConnectionParameter;
import org.mule.runtime.api.connection.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.Duration.ofSeconds;

public class AnthropicChatConnection implements ChatConnection {

  private static final Logger LOGGER = LoggerFactory.getLogger(AnthropicChatConnection.class);
  private ChatLanguageModel model;
  private final AnthropicConnectionParameter anthropicConnectionParameter;

  public AnthropicChatConnection(AnthropicConnectionParameter anthropicConnectionParameter) {
    this.anthropicConnectionParameter = anthropicConnectionParameter;
  }

  @Override
  public ChatLanguageModel getModel() {
    return model;
  }

  @Override
  public void connect() throws ConnectionException {
    try {
      long durationInSec =
          anthropicConnectionParameter.getLlmTimeoutUnit().toSeconds(anthropicConnectionParameter.getLlmTimeout());
      model = AnthropicChatModel.builder()
          .apiKey(anthropicConnectionParameter.getApiKey())
          .modelName(anthropicConnectionParameter.getModelName())
          .maxTokens(anthropicConnectionParameter.getMaxTokens())
          .temperature(anthropicConnectionParameter.getTemperature())
          .topP(anthropicConnectionParameter.getTopP())
          .timeout(ofSeconds(durationInSec))
          .logRequests(true)
          .logResponses(true)
          .build();
      testConnection(model);
    } catch (Exception e) {
      throw new ConnectionException("Failed to connect to Anthropic.", e);
    }
  }

  @Override
  public void disconnect() {
    LOGGER.debug("Anthropic Connection disconnect called");
  }

  @Override
  public boolean isValid() {
    try {
      return testConnection(model);
    } catch (Exception e) {
      LOGGER.error("Failed to validate connection to Anthropic.", e);
      return false;
    }
  }
}
