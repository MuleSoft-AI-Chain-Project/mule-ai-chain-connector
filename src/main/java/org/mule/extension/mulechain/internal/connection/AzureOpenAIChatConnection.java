package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.mule.extension.mulechain.internal.connection.paramter.AzureOpenAIConnectionParameter;
import org.mule.runtime.api.connection.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.Duration.ofSeconds;

public class AzureOpenAIChatConnection implements ChatConnection {

  private static final Logger LOGGER = LoggerFactory.getLogger(AzureOpenAIChatConnection.class);
  private ChatLanguageModel model;
  private final AzureOpenAIConnectionParameter azureOpenAIConnectionParameter;

  public AzureOpenAIChatConnection(AzureOpenAIConnectionParameter azureOpenAIConnectionParameter) {
    this.azureOpenAIConnectionParameter = azureOpenAIConnectionParameter;
  }

  @Override
  public ChatLanguageModel getModel() {
    return model;
  }

  @Override
  public void connect() throws ConnectionException {
    try {
      long durationInSec =
          azureOpenAIConnectionParameter.getLlmTimeoutUnit().toSeconds(azureOpenAIConnectionParameter.getLlmTimeout());
      model = AzureOpenAiChatModel.builder()
          .apiKey(azureOpenAIConnectionParameter.getApiKey())
          .endpoint(azureOpenAIConnectionParameter.getEndpoint())
          .deploymentName(azureOpenAIConnectionParameter.getDeploymentName())
          .maxTokens(azureOpenAIConnectionParameter.getMaxTokens())
          .temperature(azureOpenAIConnectionParameter.getTemperature())
          .topP(azureOpenAIConnectionParameter.getTopP())
          .timeout(ofSeconds(durationInSec))
          .logRequestsAndResponses(true)
          .build();
      testConnection(model);
    } catch (Exception e) {
      throw new ConnectionException("Failed to connect to Azure Open AI.", e);
    }
  }

  @Override
  public void disconnect() {
    LOGGER.debug("Azure OpenAI Connection disconnect called");
  }

  @Override
  public boolean isValid() {
    try {
      return testConnection(model);
    } catch (Exception e) {
      LOGGER.error("Failed to validate connection to Azure Open AI.", e);
      return false;
    }
  }
}
