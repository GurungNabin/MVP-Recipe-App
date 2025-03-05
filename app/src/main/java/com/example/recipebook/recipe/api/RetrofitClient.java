package com.example.recipebook.recipe.api;//package com.example.recipebook.recipe.api;
//
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class RetrofitClient {
//    private static final String BASE_URL = "https://dummyjson.com/";
//    private static Retrofit retrofit;
//
//    public static RecipeApiService getApiService(){
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create()).build();
//        }
//        return retrofit.create(RecipeApiService.class);
//    }
//}


import com.example.recipebook.recipe.api.RecipeApiService;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;

public class RetrofitClient {
    private static final String BASE_URL = "https://dummyjson.com/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() throws NoSuchAlgorithmException, KeyManagementException {
        if (retrofit == null) {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            // Return an empty array instead of null
                            return new X509Certificate[]{};
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

            // Create an OkHttpClient that uses the custom SSLContext
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCertificates[0]);
            clientBuilder.hostnameVerifier((hostname, session) -> true); // Disable hostname verification

            OkHttpClient okHttpClient = clientBuilder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient) // Set custom client
                    .build();
        }
        return retrofit;
    }

    public static RecipeApiService getApiService() throws NoSuchAlgorithmException, KeyManagementException {
        return getRetrofitInstance().create(RecipeApiService.class);
    }
}

