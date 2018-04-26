package com.example.vinicius.marvelcharacters.dependency_injection.modules;

import com.example.vinicius.marvelcharacters.model.api.AuthInterceptor;
import com.example.vinicius.marvelcharacters.utils.TimeUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule
{
	private String mBaseUrl;

	public NetworkModule(String mBaseUrl)
	{
		this.mBaseUrl = mBaseUrl;
	}

	@Provides
	@Singleton
	TimeUtils provideTimeUtils()
	{
		return new TimeUtils();
	}

	@Provides
	@Singleton
	OkHttpClient provideOkHttpClient(TimeUtils timeUtils)
	{
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
			.readTimeout(20, TimeUnit.SECONDS)
			.connectTimeout(20, TimeUnit.SECONDS)
			.addInterceptor(new AuthInterceptor("f3b647ff997bc5a3da3e68e5a08417d7",
				"9caeea1cbcdd2fc9181fe452dd6b89e58aba4d92", timeUtils));

		OkHttpClient okHttpClient = builder.build();

		return okHttpClient;
	}

	@Provides
	@Singleton
	Retrofit provideRetrofit(OkHttpClient okHttpClient)
	{
		return new Retrofit.Builder()
				  .baseUrl(mBaseUrl)
					.client(okHttpClient)
				  .addConverterFactory(GsonConverterFactory.create())
				  .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				  .build();
	}
}
