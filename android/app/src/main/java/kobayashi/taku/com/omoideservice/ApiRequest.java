/****************************************************************************
Copyright (c) 2015 Chukong Technologies Inc.
 
http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
package kobayashi.taku.com.omoideservice;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiRequest extends AsyncTask<String, Void, Map<String, String>> {
    private Map<String, Object> mParams = new HashMap<String, Object>();
    private ArrayList<ResponseCallback> callbackList = new ArrayList<ResponseCallback>();

    public void setParams(HashMap<String, Object> params){
        this.mParams = params;
    }

    public void addCallback(ResponseCallback callback){
        callbackList.add(callback);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        callbackList.clear();
    }

    protected Map<String, String> doInBackground(String... urls){
        HashMap<String, String> urlResponse = new HashMap<String, String>();
        OkHttpClient client = new OkHttpClient();
        for(String url : urls){
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
            bodyBuilder.setType(MultipartBody.FORM);
            for(Map.Entry<String, Object> e : mParams.entrySet()) {
                bodyBuilder.addFormDataPart(e.getKey(), e.getValue().toString());
                Log.d(Config.TAG, "key:" + e.getKey() + " value:" + e.getValue());
            }
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);
            Request request = requestBuilder.post(bodyBuilder.build()).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    urlResponse.put(url, response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(Config.TAG, e.getMessage());
            }
        }
        return urlResponse;
    }

    @Override
    protected void onPostExecute(Map<String, String> result) {
        super.onPostExecute(result);
        for(Map.Entry<String, String> e : result.entrySet()) {
            for (ResponseCallback c : callbackList) {
                c.onSuccess(e.getKey(), e.getValue());
            }
        }
        callbackList.clear();
    }

    public interface ResponseCallback{
        public void onSuccess(String url, String body);
    }
}