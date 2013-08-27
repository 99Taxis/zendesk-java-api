package com.taxis99.zendesk.serialize;

import java.io.IOException;

import com.google.common.base.Enums;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class EnumUpperCaseTypeAdapter <T extends Enum<T>> extends TypeAdapter<T> {
  
  private final Class<T> type;

  public static <T extends Enum<T>> EnumUpperCaseTypeAdapter<T> of(Class<T> type) {
    return new EnumUpperCaseTypeAdapter<>(type);
  }
  
  private EnumUpperCaseTypeAdapter(Class<T> type) {
    this.type = type;
  }

  @Override public void write(JsonWriter out, T value) throws IOException {
    String result = null;
    if (value != null) {
      result = value.toString().toLowerCase().replace('_', '-');
    }
    out.value(result);
  }

  @Override public T read(JsonReader in) throws IOException {
    T result = null;
    if (in.hasNext()) {
      final JsonToken peeked = in.peek();
      if (peeked == JsonToken.STRING) {
        final String upperCase = in.nextString().replace('-', '_').toUpperCase();
        result = Enums.getIfPresent(type, upperCase).orNull();
      } else if (peeked == JsonToken.NULL) {
        in.nextNull();
      }
    }
    return result;
  }
}
