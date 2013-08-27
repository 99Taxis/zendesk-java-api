package com.taxis99.zendesk.model;

import java.util.List;

public class Attachment {

  Integer id;

  /**
   * Automatically assigned when created
   * 
   * @return The ID
   */
  public Integer getId() {
    return id;
  }

  String fileName;

  /**
   * @return The name of the file
   */
  public String getFileName() {
    return fileName;
  }

  String contentUrl;

  /**
   * A full URL where the attachment file can be downloaded
   * 
   * @return the full content URL
   */
  public String getContentUrl() {
    return contentUrl;
  }

  String contentType;

  /**
   * The content type of the attachment. Example value: image/png
   * 
   * @return The content type of the attachment
   */
  public String getContentType() {
    return contentType;
  }

  Integer size;

  /**
   * The size of the file in bytes
   * 
   * @return the size in bytes
   */
  public Integer getSize() {
    return size;
  }

  List<Attachment> thumbnails;

  /**
   * An array of Photo objects. Note that thumbnails do not have thumbnails.
   * 
   * @return An array of Photo objects.
   */
  public List<Attachment> getThumbnails() {
    return thumbnails;
  }
}
