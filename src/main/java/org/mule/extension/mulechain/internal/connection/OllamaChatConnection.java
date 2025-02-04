package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.mule.extension.mulechain.internal.connection.paramter.OllamaConnectionParameter;
import org.mule.runtime.api.connection.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.Duration.ofSeconds;

public class OllamaChatConnection implements ChatConnection {

  private static final Logger LOGGER = LoggerFactory.getLogger(OllamaChatConnection.class);
  private ChatLanguageModel model;
  private final OllamaConnectionParameter ollamaConnectionParameter;

  public OllamaChatConnection(OllamaConnectionParameter ollamaConnectionParameter) {
    this.ollamaConnectionParameter = ollamaConnectionParameter;
  }

  @Override
  public ChatLanguageModel getModel() {
    return model;
  }

  @Override
  public void connect() throws ConnectionException {
    try {
      long durationInSec = ollamaConnectionParameter.getLlmTimeoutUnit().toSeconds(ollamaConnectionParameter.getLlmTimeout());
      model = OllamaChatModel.builder()
          .baseUrl(ollamaConnectionParameter.getBaseUrl())
          .modelName(ollamaConnectionParameter.getModelName())
          .temperature(ollamaConnectionParameter.getTemperature())
          .topP(ollamaConnectionParameter.getTopP())
          .timeout(ofSeconds(durationInSec))
          .build();
      testConnection(model);
    } catch (Exception e) {
      throw new ConnectionException("Failed to connect to Ollama", e);
    }
  }

  @Override
  public void disconnect() {
    LOGGER.debug("Ollama Connection disconnect called");
  }

  @Override
  public boolean isValid() {
    try {
      return testConnection(model);
    } catch (Exception e) {
      LOGGER.error("Failed to validate connection to Ollama AI.", e);
      return false;
    }
  }
}
