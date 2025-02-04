package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.mule.extension.mulechain.internal.connection.paramter.GroqAIConnectionParameter;
import org.mule.runtime.api.connection.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.Duration.ofSeconds;

public class GroqAIChatConnection implements ChatConnection {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroqAIChatConnection.class);

  private ChatLanguageModel model;
  private final GroqAIConnectionParameter groqAIConnectionParameter;

  public GroqAIChatConnection(GroqAIConnectionParameter groqAIConnectionParameter) {
    this.groqAIConnectionParameter = groqAIConnectionParameter;
  }

  @Override
  public ChatLanguageModel getModel() {
    return model;
  }

  @Override
  public void connect() throws ConnectionException {
    try {
      long durationInSec = groqAIConnectionParameter.getLlmTimeoutUnit().toSeconds(groqAIConnectionParameter.getLlmTimeout());
      model = OpenAiChatModel.builder()
          .baseUrl("https://api.groq.com/openai/v1")
          .apiKey(groqAIConnectionParameter.getApiKey())
          .modelName(groqAIConnectionParameter.getModelName())
          .maxTokens(groqAIConnectionParameter.getMaxTokens())
          .temperature(groqAIConnectionParameter.getTemperature())
          .topP(groqAIConnectionParameter.getTopP())
          .timeout(ofSeconds(durationInSec))
          .logRequests(true)
          .logResponses(true)
          .build();
      testConnection(model);
    } catch (Exception e) {
      throw new ConnectionException("Failed to connect to Groq Open AI.", e);
    }
  }

  @Override
  public void disconnect() {
    LOGGER.debug("GorqOpenAI Connection disconnect called");
  }

  @Override
  public boolean isValid() {
    try {
      return testConnection(model);
    } catch (Exception e) {
      LOGGER.error("Failed to validate connection to Groq Open AI.", e);
      return false;
    }
  }
}
