/**
 * (c) 2003-2024 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.mulechain.internal.config;

import org.mule.extension.mulechain.internal.connection.provider.AnthropicConnectionProvider;
import org.mule.extension.mulechain.internal.connection.provider.AzureOpenAIConnectionProvider;
import org.mule.extension.mulechain.internal.connection.provider.GeminiAIConnectionProvider;
import org.mule.extension.mulechain.internal.connection.provider.GroqOpenAIConnectionProvider;
import org.mule.extension.mulechain.internal.connection.provider.HuggingFaceConnectionProvider;
import org.mule.extension.mulechain.internal.connection.provider.MistralAIConnectionProvider;
import org.mule.extension.mulechain.internal.connection.provider.OllamaConnectionProvider;
import org.mule.extension.mulechain.internal.connection.provider.OpenAIConnectionProvider;
import org.mule.extension.mulechain.internal.operation.LangchainEmbeddingStoresOperations;
import org.mule.extension.mulechain.internal.operation.LangchainImageChatModelsOperations;
import org.mule.extension.mulechain.internal.operation.LangchainLLMOperations;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Configuration(name = "chat-config")
@Operations({LangchainLLMOperations.class, LangchainEmbeddingStoresOperations.class, LangchainImageChatModelsOperations.class})
@ConnectionProviders({OpenAIConnectionProvider.class, GroqOpenAIConnectionProvider.class, MistralAIConnectionProvider.class,
    OllamaConnectionProvider.class, HuggingFaceConnectionProvider.class, GeminiAIConnectionProvider.class,
    AnthropicConnectionProvider.class,
    AzureOpenAIConnectionProvider.class})
public class LangchainLLMChatConfiguration {

}
