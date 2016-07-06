package io.lazluiz.challengesocialbasenyt.data;

import android.test.InstrumentationTestCase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by luiz on 06/07/16.
 * E-mail: lf.lazzarin@gmail.com
 * GitHub: github.com/luizfelippe
 */


public class TestFetchAPIData  extends InstrumentationTestCase {

    private static final String LOG_TAG = TestFetchAPIData.class.getSimpleName();
    private static final String URL_API = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/arts/30.json?offset=40";

    public void testFetchData() throws Throwable {
        // Criaremos um sinal para avisar quando o teste for finalizado
        // já que o teste é assíncrono
        final CountDownLatch signal = new CountDownLatch(1);

        final NetworkQueue networkQueue = NetworkQueue.getInstance();

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkQueue.doGet(URL_API, LOG_TAG, new NetworkQueue.NetworkRequestCallback<JSONObject>() {

                    @Override
                    public void onRequestResponse(JSONObject response) {
                        assertNotNull(response);

                        try {
                            Log.i(LOG_TAG, "JSON saindo do forno!!!\n" + response.toString(2));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        signal.countDown();
                    }

                    @Override
                    public void onRequestError(Exception error) {
                        Log.e(LOG_TAG, "Ocorreu um erro: " + error.getMessage());
                        signal.countDown();
                    }


                });
            }
        });

        // Timeout de 12 segundos
        signal.await(12, TimeUnit.SECONDS);
    }
}
