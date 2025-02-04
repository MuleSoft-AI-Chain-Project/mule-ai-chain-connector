package org.mule.extension.mulechain.internal.connection;

import dev.langchain4j.model.image.ImageModel;

public interface ImageConnection extends Connection<ImageModel> {

  @Override
  default boolean testConnection(ImageModel model) {
    if (model != null) {
      model.generate("Generate Happy Penguin");
      return true;
    }
    return false;
  }

}
