package it.unito.di.justread.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import it.unito.di.justread.model.RestClient;

public class GetJsonTaskWithParameter  extends AsyncTask<String, Void, String> {

    private String type;
    private TaskCompletionListener<String> listener;
    private List<NameValuePair> nameValuePairs;

    public GetJsonTaskWithParameter(String type, TaskCompletionListener<String> listener, List<NameValuePair> nameValuePairs) {
        this.type = type;
        this.listener = listener;
        this.nameValuePairs = nameValuePairs;
    }

    @Override
    protected String doInBackground(String... params) {
        String output = "";
        DefaultHttpClient httpClient = RestClient.getInstance();
        switch (type) {
            case "GET":
                HttpGet httpGet = new HttpGet(params[0]);
                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    output = EntityUtils.toString(httpEntity);
                    Log.i("EXCEPTION", output);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                break;
            case "POST":
                HttpPost httpPost = new HttpPost(params[0]);
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    output = EntityUtils.toString(httpEntity);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(params[0]);
                try {
                    HttpResponse httpResponse = httpClient.execute(httpPut);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    output = EntityUtils.toString(httpEntity);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                break;
            case "DELETE":
                HttpDelete httpDelete = new HttpDelete(params[0]);
                try {
                    HttpResponse httpResponse = httpClient.execute(httpDelete);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    output = EntityUtils.toString(httpEntity);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                break;
        }
        return output;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onComplete(s);
    }
}