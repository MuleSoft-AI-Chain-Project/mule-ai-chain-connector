package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.mule.extension.mulechain.internal.connection.util.Assistant;
import org.mule.runtime.api.connection.ConnectionException;

public interface ChatConnection extends Connection<ChatLanguageModel> {

  @Override
  default boolean testConnection(ChatLanguageModel model) {
    if (model != null) {
      Assistant assistant = AiServices.create(Assistant.class, model);
      assistant.chat("Hello!");
      return true;
    }
    return false;
  }

}
