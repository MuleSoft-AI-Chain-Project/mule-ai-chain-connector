package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.mule.extension.mulechain.internal.connection.paramter.GeminiAIConnectionParameter;
import org.mule.runtime.api.connection.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.Duration.ofSeconds;

public class GeminiAIChatConnection implements ChatConnection {

  private static final Logger LOGGER = LoggerFactory.getLogger(GeminiAIChatConnection.class);
  private ChatLanguageModel model;
  private final GeminiAIConnectionParameter geminiAIConnectionParameter;

  public GeminiAIChatConnection(GeminiAIConnectionParameter geminiAIConnectionParameter) {
    this.geminiAIConnectionParameter = geminiAIConnectionParameter;
  }

  @Override
  public ChatLanguageModel getModel() {
    return model;
  }

  @Override
  public void connect() throws ConnectionException {
    try {
      long durationInSec = geminiAIConnectionParameter.getLlmTimeoutUnit().toSeconds(geminiAIConnectionParameter.getLlmTimeout());
      model = GoogleAiGeminiChatModel.builder()
          .apiKey(geminiAIConnectionParameter.getApiKey())
          .modelName(geminiAIConnectionParameter.getModelName())
          .temperature(geminiAIConnectionParameter.getTemperature())
          .topP(geminiAIConnectionParameter.getTopP())
          .timeout(ofSeconds(durationInSec))
          .maxOutputTokens(geminiAIConnectionParameter.getMaxTokens())
          .logRequestsAndResponses(false)
          .build();
      testConnection(model);
    } catch (Exception e) {
      throw new ConnectionException("Failed to connect to Gemini AI.", e);
    }
  }

  @Override
  public void disconnect() {
    LOGGER.debug("GeminiAI Connection disconnect called");
  }

  @Override
  public boolean isValid() {
    try {
      return testConnection(model);
    } catch (Exception e) {
      LOGGER.error("Failed to validate connection to Gemini AI.", e);
      return false;
    }
  }
}
