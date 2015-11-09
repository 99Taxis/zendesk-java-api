package com.taxis99.zendesk.model;

import java.util.Date;

import javax.annotation.Nullable;

public class User {

  Long id;

  /**
   * Automatically assigned when creating users
   */
  @Nullable Long getId() {
    return id;
  }

  String url;

  /**
   * The API url of this user
   */
  @Nullable public String getUrl() {
    return url;
  }

  String name;

  /**
   * The name of the user
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the user
   */
  public void setName(String name) {
    this.name = name;
  }

  Date createdAt;

  /**
   * The time the user was created
   */
  public Date getCreatedAt() {
    return createdAt;
  }

  Date updatedAt;

  /**
   * @return The time of the last update of the user
   */
  public Date getUpdatedAt() {
    return updatedAt;
  }

  String timeZone;

  /**
   * @return The time-zone of this user
   */
  @Nullable public String getTimeZone() {
    return timeZone;
  }

  /**
   * sets the time-zone of this user
   * 
   * @param timeZone
   *          the time-zone
   */
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  String locale;

  /**
   * @return The locale for this user
   */
  @Nullable public String getLocale() {
    return locale;
  }

  Integer localeId;

  /**
   * @return The language identifier for this user
   */
  @Nullable public Integer getLocaleId() {
    return localeId;
  }

  /**
   * Sets the language identifier for this user
   * 
   * @param localeId
   *          the language identifier
   */
  public void setLocaleId(Integer localeId) {
    this.localeId = localeId;
  }

  Integer organizationId;

  /**
   * The id of the organization this user is associated with
   */
  @Nullable public Integer getOrganizationId() {
    return organizationId;
  }

  /**
   * Sets the id of the organization this user is associated with
   * 
   * @param organizationId
   *          the id of the organization
   */
  public void setOrganizationId(Integer organizationId) {
    this.organizationId = organizationId;
  }

  UserRole role;

  /**
   * The role of the user
   */
  public UserRole getRole() {
    return role;
  }

  /**
   * Sets the role of the user
   * 
   * @param role
   */
  public void setRole(UserRole role) {
    this.role = role;
  }

  Boolean verified;

  /**
   * Zendesk has verified that this user is who he says he is
   * 
   * @return if the user is verified
   */
  @Nullable public Boolean getVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  String email;

  /**
   * The primary email address of this user
   * 
   * @return the email address
   */
  public String getEmail() {
    return email;
  }

  String phone;

  /**
   * The primary phone number of this user
   * 
   * @return the phone number
   */
  @Nullable public String getPhone() {
    return phone;
  }

  /**
   * Sets the primary phone number of this user
   * 
   * @return the new phone number
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  Attachment photo;

  /**
   * The user's profile picture represented as an Attachment object
   * 
   * @return the profile picture
   */
  @Nullable Attachment getPhoto() {
    return photo;
  }

  /**
   * Sets the user's profile picture
   * 
   * @param photo
   *          the new profile picture
   */
  public void setPhoto(Attachment photo) {
    this.photo = photo;
  }
}
