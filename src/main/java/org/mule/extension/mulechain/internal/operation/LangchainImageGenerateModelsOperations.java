/**
 * (c) 2003-2024 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.mulechain.internal.operation;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;
import org.json.JSONObject;
import org.mule.extension.mulechain.internal.connection.ImageConnection;
import org.mule.extension.mulechain.internal.constants.MuleChainConstants;
import org.mule.extension.mulechain.internal.error.MuleChainErrorType;
import org.mule.extension.mulechain.internal.error.provider.ImageErrorTypeProvider;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.io.IOUtils.toInputStream;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;


/**
 * This class is a container for Image related operations.Every public method in this class will be taken as an extension operation.
 */
public class LangchainImageGenerateModelsOperations {

  private static final Logger LOGGER = LoggerFactory.getLogger(LangchainImageGenerateModelsOperations.class);

  /**
   * Generates an image based on the prompt in data
   * @param connection           Refers to the configuration object
   * @param data                    Refers to the user prompt
   * @return                        Returns the image URL link in the response
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("IMAGE-generate")
  @Throws(ImageErrorTypeProvider.class)
  @OutputJsonType(schema = "api/response/Response.json")
  public Result<InputStream, Void> drawImage(@Connection ImageConnection connection,
                                             @Content String data) {
    try {
      LOGGER.debug("Image Generate Operation called with the prompt: {}", data);
      ImageModel model = connection.getModel();

      Response<Image> response = model.generate(data);
      LOGGER.info("Generated Image: {}", response.content().url());

      JSONObject jsonObject = new JSONObject();
      jsonObject.put(MuleChainConstants.RESPONSE, response.content().url());

      LOGGER.debug("Image Generate Operation completed successfully with the image: {}", response.content().url());
      return Result.<InputStream, Void>builder()
          .attributesMediaType(org.mule.runtime.api.metadata.MediaType.APPLICATION_JAVA)
          .output(toInputStream(jsonObject.toString(), StandardCharsets.UTF_8))
          .mediaType(org.mule.runtime.api.metadata.MediaType.APPLICATION_JSON)
          .build();
    } catch (Exception e) {
      throw new ModuleException("Error while generating the required image: " + data, MuleChainErrorType.IMAGE_GENERATION_FAILURE,
                                e);
    }
  }
}
