package com.keenbrace.api;


import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;


/**
 * Created by  on 16/1/20.
 */
public class KeenbraceRetrofit {

    public  BaseApi createBaseApi(){
        Retrofit  retrofit = new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getUnsafeOkHttpClient())
                .build();
        return retrofit.create(BaseApi.class);
    }


    private static OkHttpClient getDefaultHttpClient() {
        try {

            SSLContext sslContext = sslContextForTrustedCertificates(trustedCertificatesInputStream());

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslContext.getSocketFactory());
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("ApiVersion", "1.3")
                            .header("AppKey", "20200")
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("ApiVersion", "1.0")
                            .header("AppKey", "60200")
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static InputStream trustedCertificatesInputStream() {
        String comodoRsaCertificationAuthority = ""
                +"-----BEGIN CERTIFICATE-----\n"
                +"MIIEpjCCA46gAwIBAgIQRih1gAnjZtu32b8ELhcjPjANBgkqhkiG9w0BAQsFADBP\n"
                +"MQswCQYDVQQGEwJDTjEaMBgGA1UEChMRV29TaWduIENBIExpbWl0ZWQxJDAiBgNV\n"
                +"BAMMG0NBIOayg+mAmuWFjei0uVNTTOivgeS5piBHMjAeFw0xNTEwMjQwMTIxMTZa\n"
                +"Fw0xNjEwMjQwMTIxMTZaMB0xGzAZBgNVBAMMEmFwaS5taGVhbHRoeXVuLmNvbTCC\n"
                +"ASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMYqmhJD5cQLnJKaNzkZOANv\n"
                +"MDJyPEUNNvINQvzbSiMORes5ux2zkzNNzxSjpJwWS6s15nttr7IjRGexZ8KzkfKA\n"
                +"Ab+DiARZRNVjvMN794Ra83OXocgNKASVTpaSuFH4etdb5b1tVM31TP9sByV19s//\n"
                +"H1BCW3jPZ7LojdkDEG0Qh5oKuO4wGubKKsNZQ1Zmm72MIBUQ8QkWEndpRNNTLkfi\n"
                +"VUrs9n9fp5aJh/AT4jGSX7ZzaNJIV4t2y4OzBGXJQtRtSqUb22JZjI9l0ze1BP7I\n"
                +"iGptZ3AqMIfX3ntXZp8XXrsXNHOVZTKg9f67EbHYa/fFhL2WqD3G4k9HSD8K/+0C\n"
                +"AwEAAaOCAa4wggGqMAsGA1UdDwQEAwIFoDAdBgNVHSUEFjAUBggrBgEFBQcDAgYI\n"
                +"KwYBBQUHAwEwCQYDVR0TBAIwADAdBgNVHQ4EFgQUVqjSc9iyw2hzGkaHERSPhL/e\n"
                +"HKowHwYDVR0jBBgwFoAUMNp0hvMokFae1zExwr1ZzZMSOR0wfwYIKwYBBQUHAQEE\n"
                +"czBxMDUGCCsGAQUFBzABhilodHRwOi8vb2NzcDIud29zaWduLmNuL2NhMmcyL3Nl\n"
                +"cnZlcjEvZnJlZTA4BggrBgEFBQcwAoYsaHR0cDovL2FpYTIud29zaWduLmNuL2Nh\n"
                +"MmcyLnNlcnZlcjEuZnJlZS5jZXIwPgYDVR0fBDcwNTAzoDGgL4YtaHR0cDovL2Ny\n"
                +"bHMyLndvc2lnbi5jbi9jYTJnMi1zZXJ2ZXIxLWZyZWUuY3JsMB0GA1UdEQQWMBSC\n"
                +"EmFwaS5taGVhbHRoeXVuLmNvbTBRBgNVHSAESjBIMAgGBmeBDAECATA8Bg0rBgEE\n"
                +"AYKbUQYBAgICMCswKQYIKwYBBQUHAgEWHWh0dHA6Ly93d3cud29zaWduLmNvbS9w\n"
                +"b2xpY3kvMA0GCSqGSIb3DQEBCwUAA4IBAQBDrAI51rcESUoi3VrEPQitN6PfLF3l\n"
                +"r7AcOrzxrBQtfhU6VHkyuekaRO0vSPFNZZHWj0mnx22HJV011ZFbEif8hc4sVher\n"
                +"df/FSctXSK8UdqONw8SbuM14q8W6kajH9KL4bUnhK6lnclNjYeyqQF8DGU1Oz2rW\n"
                +"IKEPQz6wMLPmdDNW3IhgxNE+q8sUtWZv63sXG4jRvPeaVV52OglSjDFuiI4ZZFoB\n"
                +"F7sI+pRxwwlRZMDpdvmn104vTrIJInLfFnB6uNMyybiGIN//0P90ybkbycNfI3n2\n"
                +"oRPfQSLq8c99k9600eWJfLUOaCWbEayrBwirm/btE5IPWZ925/nwN7ig\n"
                +"-----END CERTIFICATE-----\n";
        return new Buffer()
                .writeUtf8(comodoRsaCertificationAuthority)
                .inputStream();
    }


    public static SSLContext sslContextForTrustedCertificates(InputStream in) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
            if (certificates.isEmpty()) {
                throw new IllegalArgumentException("expected non-empty set of trusted certificates");
            }

            // Put the certificates a key store.
            char[] password = "password".toCharArray(); // Any password will work.
            KeyStore keyStore = newEmptyKeyStore(password);
            int index = 0;
            for (Certificate certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificate);
            }

            // Wrap it up in an SSL context.
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                    KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
                    new SecureRandom());
            return sslContext;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }


    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null;
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }


}
