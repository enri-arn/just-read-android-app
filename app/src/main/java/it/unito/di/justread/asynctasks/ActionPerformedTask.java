package it.unito.di.justread.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import it.unito.di.justread.model.RestClient;


public class ActionPerformedTask extends AsyncTask<String, Integer, Boolean> {

    private TaskCompletionListener<Boolean> listener;

    public ActionPerformedTask(TaskCompletionListener<Boolean> listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String output = "";
        DefaultHttpClient httpClient = RestClient.getInstance();
        HttpGet httpGet = new HttpGet(strings[0]);
        boolean reviewAdded = false;
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            output = EntityUtils.toString(httpEntity); // boolean true o false
            if (output.equalsIgnoreCase("true")){
                reviewAdded = true;
            }
        } catch (Exception e){
            Log.i("EXCEPTION", "Exception in http ........ c'è qualquadra che non cosa....");
        }
        return reviewAdded;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        listener.onComplete(aBoolean);
    }
}
