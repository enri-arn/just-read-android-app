package it.unito.di.justread.asynctasks;


import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import it.unito.di.justread.model.RestClient;

public class AuthenticationTask extends AsyncTask<String, Integer, Boolean> {

    private TaskCompletionListener<Boolean> listener;
    private String URL;

    public AuthenticationTask(TaskCompletionListener<Boolean> listener, String url) {
        this.listener = listener;
        this.URL = url;
    }



    @Override
    protected Boolean doInBackground(String... strings) {
        String output;
        try {
            DefaultHttpClient httpClient = RestClient.getInstance();
            HttpPost httpPost = new HttpPost(URL);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("email", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("password", strings[1]));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            output = EntityUtils.toString(httpEntity);
            if (output.equalsIgnoreCase("true")){
                return true;
            }
        } catch (Exception e){
            Log.i("EXCEPTION", "Exception in http ........ c'è qualquadra che non cosa....");
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        listener.onComplete(aBoolean);
    }
}
