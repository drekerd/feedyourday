package theawesomeguy.drekerd.feedyourday;

import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Started Synctask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("URL HERE");
        Log.d(TAG, "onCreate: Done");

    }

    private class DownloadData extends AsyncTask<String, Void, String>{
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is " + s);
        }//onPostEnd

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if(rssFeed == null){
                Log.e(TAG, "doInBackground: Error Downloading");
            }
            return rssFeed;

        }//doInBackground end

        private String downloadXML (String urlPath){

            StringBuilder xmlResult = new StringBuilder();
            try{
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The Response code was "+response);
                /*InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);*/
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charCount;
                char[] inputBuffer = new char[500];

                while(true){
                    charCount = reader.read(inputBuffer);
                    if(charCount < 0){
                        break;
                    }
                    if(charCount > 0){
                        xmlResult.append(String.copyValueOf(inputBuffer,0,charCount));
                    }
                }
                reader.close();
                return xmlResult.toString();

            }catch(MalformedURLException e){
                Log.e(TAG, "downloadXML: Invalid URL" + e.getMessage());
            }catch(IOException e){
                Log.e(TAG, "downloadXML: IO Exception"+ e.getMessage());
            }
            return null;

        }



    } //AsycnTask finish
} //main finish

