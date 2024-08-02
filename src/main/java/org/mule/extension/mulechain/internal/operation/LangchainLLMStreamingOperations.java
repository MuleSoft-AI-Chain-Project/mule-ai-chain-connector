/**
 * (c) 2003-2024 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.mulechain.internal.operation;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.extension.mulechain.internal.config.LangchainLLMConfiguration;
import org.mule.extension.mulechain.internal.metadata.TokenStreamMetadataResolver;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.Streaming;

import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import static java.time.Duration.ofSeconds;



/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class LangchainLLMStreamingOperations {



  /*   
   * https://docs.mulesoft.com/mule-sdk/latest/define-operations
   * Define output resolver
   *  */
  interface Assistant {

    TokenStream chat(String message);
  }

  @MediaType(value = ANY, strict = false)
  @Alias("Stream-prompt-answer")
  @OutputResolver(output = TokenStreamMetadataResolver.class)
  @Streaming
  public TokenStream streamingPrompt(@Config LangchainLLMConfiguration configuration, String prompt) {

    StreamingChatLanguageModel model = OpenAiStreamingChatModel.builder()
        .apiKey(System.getenv("OPENAI_API_KEY").replace("\n", "").replace("\r", ""))
        .modelName(configuration.getModelName())
        .temperature(0.3)
        .timeout(ofSeconds(60))
        .logRequests(true)
        .logResponses(true)
        .build();


    Assistant assistant = AiServices.create(Assistant.class, model);


    TokenStream tokenStream = assistant.chat(prompt);


    tokenStream.onNext(System.out::println)
        .onComplete(System.out::println)
        .onError(Throwable::printStackTrace)
        .start();


    return tokenStream;


  }
}