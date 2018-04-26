package com.example.vinicius.marvelcharacters.model.api;

import com.example.vinicius.marvelcharacters.utils.TimeUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor
{
  private static final String TIMESTAMP_KEY = "ts";
  private static final String HASH_KEY = "hash";
  private static final String APIKEY_KEY = "apikey";

  private final String publicKey;
  private final String privateKey;
  private final TimeUtils timeUtils;
  private final AuthHashGenerator authHashGenerator = new AuthHashGenerator();

  public AuthInterceptor(String publicKey, String privateKey, TimeUtils timeUtils)
  {
    this.publicKey = publicKey;
    this.privateKey = privateKey;
    this.timeUtils = timeUtils;
  }

  @Override
  public Response intercept(Chain chain) throws IOException
  {
    String timestamp = String.valueOf(timeUtils.currentTimeMillis());
    String hash = null;
    try
    {
      hash = authHashGenerator.generateHash(timestamp, publicKey, privateKey);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    Request request = chain.request();
    HttpUrl url = request.url()
      .newBuilder()
      .addQueryParameter(TIMESTAMP_KEY, timestamp)
      .addQueryParameter(APIKEY_KEY, publicKey)
      .addQueryParameter(HASH_KEY, hash)
      .build();
    request = request.newBuilder().url(url).build();
    return chain.proceed(request);
  }
}
