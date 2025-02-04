package org.mule.extension.mulechain.internal.connection.util;

import dev.langchain4j.service.Result;

public interface Assistant {

  Result<String> chat(String userMessage);
}
