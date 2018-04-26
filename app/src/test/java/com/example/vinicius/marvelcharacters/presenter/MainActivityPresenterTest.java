package com.example.vinicius.marvelcharacters.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

import com.example.vinicius.marvelcharacters.DTO.CharacterDTO;
import com.example.vinicius.marvelcharacters.DTO.CharacterDataDTO;
import com.example.vinicius.marvelcharacters.model.api.AppApiHelper;
import com.example.vinicius.marvelcharacters.model.api.AuthInterceptor;
import com.example.vinicius.marvelcharacters.model.api.GetCharactersResponse;
import com.example.vinicius.marvelcharacters.model.data_manager.AppDataManager;
import com.example.vinicius.marvelcharacters.presenter.main_activity.MainActivityMvpPresenter;
import com.example.vinicius.marvelcharacters.presenter.main_activity.MainActivityPresenter;
import com.example.vinicius.marvelcharacters.utils.TimeUtils;
import com.example.vinicius.marvelcharacters.view.main_activity.MainActivityMvpView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityPresenterTest
{
  @Mock
  @Named("ActivityContext") Context activityContext;

  @Mock
  NetworkInfo networkInfo;

  @Mock
  AppCompatActivity activity;

  @Mock
  MainActivityMvpView mainActivityMvpView;

  @Mock
  ConnectivityManager connectivityManager;

  @Mock
  Resources resources;

  private AppApiHelper appApiHelper;
  private AppDataManager dataManager;
  private MainActivityMvpPresenter mainActivityPresenter;

  /**
   * Executa antes de cada método de teste
   */
  @Before
  public void setUp() throws Exception
  {
    OkHttpClient.Builder builder = new OkHttpClient.Builder()
      .readTimeout(20, TimeUnit.SECONDS)
      .connectTimeout(20, TimeUnit.SECONDS)
      .addInterceptor(new AuthInterceptor("f3b647ff997bc5a3da3e68e5a08417d7",
        "9caeea1cbcdd2fc9181fe452dd6b89e58aba4d92", new TimeUtils()));

    OkHttpClient okHttpClient = builder.build();
    /**
     * Precisamos usar spy pois o método when requer um objeto mockado, como a variável appApiHelper
     * se refere a um objeto real, o que o método spy faz é criar um wrapper no objeto real
     * para que o mesmo possa ser usado no método when.
     */
    appApiHelper = Mockito.spy(new AppApiHelper(new Retrofit.Builder()
      .baseUrl("https://gateway.marvel.com/v1/public/")
      .client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()));

    dataManager = new AppDataManager(appApiHelper);
    mainActivityPresenter = new MainActivityPresenter(activityContext, dataManager, activity);
    mainActivityPresenter.registerView(mainActivityMvpView);

    when(activityContext.getResources()).thenReturn(resources);
    when((ConnectivityManager) activityContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
    when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
  }

  @Test
  public void showProgressBarWhenStartLoadCharacters()
  {
    when(networkInfo.isConnected()).thenReturn(true);
    when(networkInfo.isAvailable()).thenReturn(true);

    CharacterDataDTO data = new CharacterDataDTO();
    List<CharacterDTO> results = new ArrayList();
    CharacterDTO characterDTO = new CharacterDTO();
    results.add(characterDTO);
    data.setResults(results);
    GetCharactersResponse getCharactersResponse = new GetCharactersResponse();
    getCharactersResponse.setData(data);
    when(appApiHelper.getCharacters("30", "0")).thenReturn(Observable.just(getCharactersResponse));

    mainActivityPresenter.loadCharacters(null, Schedulers.trampoline(), Schedulers.trampoline());
    /**
     * Verifica se o método showProgressBar é chamado durante a execução
     * do método loadCharacters no presenter
     */
    verify(mainActivityMvpView).showProgressBar();
  }

  @Test
  public void getCharactersResponseAfterLoadCharacters()
  {
    when(networkInfo.isConnected()).thenReturn(true);
    when(networkInfo.isAvailable()).thenReturn(true);

    CharacterDataDTO data = new CharacterDataDTO();
    List<CharacterDTO> results = new ArrayList();
    CharacterDTO characterDTO = new CharacterDTO();
    results.add(characterDTO);
    data.setResults(results);
    GetCharactersResponse getCharactersResponse = new GetCharactersResponse();
    getCharactersResponse.setData(data);
    when(appApiHelper.getCharacters("30", "0")).thenReturn(Observable.just(getCharactersResponse));

    mainActivityPresenter.loadCharacters(null, Schedulers.trampoline(), Schedulers.trampoline());
    /**
     * Verifica se o método getCharactersResponse é chamado durante a execução
     * do método loadCharacters no presenter
     */
    verify(mainActivityMvpView).getCharactersResponse(getCharactersResponse);
  }

  @Test
  public void allItemLoadedReturnFalse()
  {
    Assert.assertEquals(((MainActivityPresenter)mainActivityPresenter).allItemsLoaded(), false);
  }

  @Test
  public void hideProgressBarAfterLoadCharacters()
  {
    when(networkInfo.isConnected()).thenReturn(true);
    when(networkInfo.isAvailable()).thenReturn(true);

    CharacterDataDTO data = new CharacterDataDTO();
    List<CharacterDTO> results = new ArrayList();
    CharacterDTO characterDTO = new CharacterDTO();
    results.add(characterDTO);
    data.setResults(results);
    GetCharactersResponse getCharactersResponse = new GetCharactersResponse();
    getCharactersResponse.setData(data);
    when(appApiHelper.getCharacters("30", "0")).thenReturn(Observable.just(getCharactersResponse));

    mainActivityPresenter.loadCharacters(null, Schedulers.trampoline(), Schedulers.trampoline());
    /**
     * Verifica se o método hideProgressBar é chamado durante a execução
     * do método loadCharacters no presenter
     */
    verify(mainActivityMvpView).hideProgressBar();
  }

  @Test
  public void hideProgressBarWhenLoadCharactersAndNoInternetConnection()
  {
    when(activityContext.getResources()).thenReturn(resources);
    when(networkInfo.isConnected()).thenReturn(false);

    CharacterDataDTO data = new CharacterDataDTO();
    List<CharacterDTO> results = new ArrayList();
    CharacterDTO characterDTO = new CharacterDTO();
    results.add(characterDTO);
    data.setResults(results);
    GetCharactersResponse getCharactersResponse = new GetCharactersResponse();
    getCharactersResponse.setData(data);

    mainActivityPresenter.loadCharacters(null, Schedulers.trampoline(), Schedulers.trampoline());
    /**
     * Verifica se o método hideProgressBar é chamado durante a execução
     * do método loadCharacters no presenter
     */
    verify(mainActivityMvpView).hideProgressBar();
  }

  @Test
  public void testEmmitedValuesByObservable()
  {
    TestObserver<GetCharactersResponse> testObserver = new TestObserver<>();

    when(networkInfo.isConnected()).thenReturn(true);
    when(networkInfo.isAvailable()).thenReturn(true);

    CharacterDataDTO data = new CharacterDataDTO();
    List<CharacterDTO> results = new ArrayList();
    data.setResults(results);
    GetCharactersResponse getCharactersResponse = new GetCharactersResponse();
    getCharactersResponse.setData(data);
    when(appApiHelper.getCharacters("30", "0")).thenReturn(Observable.just(getCharactersResponse));

    mainActivityPresenter.loadCharacters(testObserver, Schedulers.trampoline(), Schedulers.trampoline());

    /**
     * Testa se o TestObserver recebeu apenas um evento onComplete
     */
    testObserver.assertComplete();
    /**
     * Testa se o TestObserver não recebeu nenhum evento onError
     */
    testObserver.assertNoErrors();
    /**
     * Faz um assert no número de vezes em que o evento onNext do TestObserver foi chamado
     */
    testObserver.assertValueCount(1);

    GetCharactersResponse receivedValues = testObserver.values().get(0);

    Assert.assertTrue(receivedValues.getData().getResults().isEmpty());
  }
}
