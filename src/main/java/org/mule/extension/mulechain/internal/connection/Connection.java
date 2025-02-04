package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.mule.runtime.api.connection.ConnectionException;

public interface Connection<T> {

  T getModel();

  void connect() throws ConnectionException;

  void disconnect();

  boolean isValid();

  boolean testConnection(T model);

}
