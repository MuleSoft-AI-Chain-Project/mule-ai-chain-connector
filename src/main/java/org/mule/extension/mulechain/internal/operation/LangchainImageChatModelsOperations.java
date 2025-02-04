/**
 * (c) 2003-2024 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.mulechain.internal.operation;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.extension.mulechain.api.metadata.LLMResponseAttributes;
import org.mule.extension.mulechain.api.metadata.ScannedDocResponseAttributes;
import org.mule.extension.mulechain.api.metadata.TokenUsage;
import org.mule.extension.mulechain.internal.connection.ChatConnection;
import org.mule.extension.mulechain.internal.constants.MuleChainConstants;
import org.mule.extension.mulechain.internal.error.MuleChainErrorType;
import org.mule.extension.mulechain.internal.error.provider.ImageErrorTypeProvider;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;

import static org.mule.extension.mulechain.internal.helpers.ResponseHelper.createLLMResponse;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import dev.langchain4j.model.output.Response;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

import javax.imageio.ImageIO;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;


/**
 * This class is a container for Image related operations.Every public method in this class will be taken as an extension operation.
 */
public class LangchainImageChatModelsOperations {

  private static final Logger LOGGER = LoggerFactory.getLogger(LangchainImageChatModelsOperations.class);

  /**
   * Reads an image from a URL and provides the responses for the user prompts.
   *
   * @param connection           Refers to the configuration object
   * @param data                    Refers to the user prompt
   * @param contextURL              Refers to the image URL to be analyzed
   * @return                        Refers to the response returned by the LLM
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("IMAGE-read")
  @Throws(ImageErrorTypeProvider.class)
  @OutputJsonType(schema = "api/response/Response.json")
  public org.mule.runtime.extension.api.runtime.operation.Result<InputStream, LLMResponseAttributes> readFromImage(@Connection ChatConnection connection,
                                                                                                                   @Content String data,
                                                                                                                   String contextURL) {
    try {
      LOGGER.debug("Image Read Operation called with the prompt: {} & the url: {}", data, contextURL);
      ChatLanguageModel model = connection.getModel();

      UserMessage userMessage;
      if (isURL(contextURL)) {
        userMessage = UserMessage.from(
                                       TextContent.from(data),
                                       ImageContent.from(contextURL));
      } else {
        String imagePath = contextURL;
        String imageBase64 = convertToBase64String(imagePath);

        userMessage = UserMessage.from(
                                       TextContent.from(data),
                                       ImageContent.from(imageBase64, "image/png"));
      }

      Response<AiMessage> response = model.generate(userMessage);

      JSONObject jsonObject = new JSONObject();
      jsonObject.put(MuleChainConstants.RESPONSE, response.content().text());

      LOGGER.debug("Image Read Operation completed with the response: {}", response.content().text());

      return createLLMResponse(jsonObject.toString(), response, new HashMap<>());
    } catch (Exception e) {
      throw new ModuleException(String.format("Unable to analyze the provided image %s with the text: %s", contextURL,
                                              data),
                                MuleChainErrorType.IMAGE_ANALYSIS_FAILURE,
                                e);
    }
  }

  /**
   * Reads scanned documents and converts to response as prompted by the user.
   * @param connection           Refers to the configuration object
   * @param data                    Refers to the user prompt
   * @param filePath                Path to the file to be analyzed
   * @return                        Returns the list of analyzed pages of the document
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("IMAGE-read-scanned-documents")
  @Throws(ImageErrorTypeProvider.class)
  @OutputJsonType(schema = "api/response/ScannedResponse.json")
  public org.mule.runtime.extension.api.runtime.operation.Result<InputStream, ScannedDocResponseAttributes> readScannedDocumentPDF(@Connection ChatConnection connection,
                                                                                                                                   @Content String data,
                                                                                                                                   String filePath) {

    LOGGER.debug("Image Read Scanned Documents Operation called with the prompt: {} & filePath: {}", data, filePath);
    ChatLanguageModel model = connection.getModel();

    JSONObject jsonObject = new JSONObject();
    JSONArray docPages = new JSONArray();

    int totalPages;
    List<ScannedDocResponseAttributes.DocResponseAttribute> docResponseAttributes = new ArrayList<>();

    try (InputStream inputStream = Files.newInputStream(Paths.get(filePath));
        PDDocument document = PDDocument.load(inputStream);) {

      PDFRenderer pdfRenderer = new PDFRenderer(document);
      totalPages = document.getNumberOfPages();
      LOGGER.info("Total files to be converted -> {}", totalPages);

      JSONObject docPage;

      for (int pageNumber = 0; pageNumber < totalPages; pageNumber++) {

        BufferedImage image = pdfRenderer.renderImageWithDPI(pageNumber, 300);
        LOGGER.debug("Reading page -> {}", pageNumber);

        String imageBase64 = convertToBase64String(image);
        UserMessage userMessage = UserMessage.from(
                                                   TextContent.from(data),
                                                   ImageContent.from(imageBase64, "image/png"));

        Response<AiMessage> response = model.generate(userMessage);

        docPage = new JSONObject();
        docPage.put(MuleChainConstants.PAGE, pageNumber + 1);
        docPage.put(MuleChainConstants.RESPONSE, response.content().text());
        LOGGER.debug("Image Read Scanned Documents Operation completed with the response: {}", response.content().text());
        docResponseAttributes
            .add(new ScannedDocResponseAttributes.DocResponseAttribute(pageNumber + 1,
                                                                       new TokenUsage(response.tokenUsage().inputTokenCount(),
                                                                                      response.tokenUsage()
                                                                                          .outputTokenCount(),
                                                                                      response.tokenUsage().totalTokenCount())));
        docPages.put(docPage);
      }

    } catch (IOException e) {
      throw new ModuleException("Error occurred while processing the document file: " + filePath,
                                MuleChainErrorType.FILE_HANDLING_FAILURE, e);
    } catch (ModuleException e) {
      throw e;
    } catch (Exception e) {
      throw new ModuleException(String.format("Unable to analyze the provided document %s with the text: %s", filePath,
                                              data),
                                MuleChainErrorType.IMAGE_ANALYSIS_FAILURE,
                                e);
    }

    jsonObject.put(MuleChainConstants.PAGES, docPages);

    Map<String, String> attributes = new HashMap<>();
    attributes.put(MuleChainConstants.TOTAL_PAGES, String.valueOf(totalPages));

    return createLLMResponse(jsonObject.toString(), docResponseAttributes,
                             attributes);
  }

  private String convertToBase64String(BufferedImage image) {
    String base64String;
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      ImageIO.write(image, "png", outputStream);
      byte[] imageBytes = outputStream.toByteArray();
      base64String = Base64.getEncoder().encodeToString(imageBytes);
      return base64String;
    } catch (IOException e) {
      throw new ModuleException("Error occurred while processing the image", MuleChainErrorType.IMAGE_PROCESSING_FAILURE, e);
    }
  }

  private boolean isURL(String fileNameFilter) {
    String urlPattern = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
    return fileNameFilter.matches(urlPattern);
  }

  private static String convertToBase64String(String filePath) {
    try {
      // Read file bytes
      byte[] fileContent = Files.readAllBytes(new File(filePath).toPath());
      // Encode bytes to Base64
      return Base64.getEncoder().encodeToString(fileContent);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
