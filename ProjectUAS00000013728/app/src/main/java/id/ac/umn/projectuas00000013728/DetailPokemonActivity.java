package id.ac.umn.projectuas00000013728;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DetailPokemonActivity extends AppCompatActivity {

    TextView textOrder;
    TextView textName;
    TextView textType;
    TextView textType2;
    TextView textWeight;
    TextView textHeight;
    TextView textAbilities;
    TextView textDescription;
    ImageView imgSprite;
    String descriptionResult;
    FloatingActionButton btnFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pokemon);
        //setToTextView();

        new FetchDescription().execute();
    }

    void setToTextView(){
        Bundle bundle = getIntent().getExtras();
        textOrder = (TextView) findViewById(R.id.detail_order);
        textName = (TextView) findViewById(R.id.detail_name);
        textType = (TextView) findViewById(R.id.detail_type);
        textType2 = (TextView) findViewById(R.id.detail_type2);
        textWeight = (TextView) findViewById(R.id.detail_weight);
        textHeight = (TextView) findViewById(R.id.detail_height);
        textAbilities = (TextView) findViewById(R.id.detail_abilities);
        textDescription = (TextView) findViewById(R.id.detail_description);
        imgSprite = (ImageView) findViewById(R.id.imageviewPokemon);
        textOrder.setText(bundle.getString("id"));
        textName.setText(bundle.getString("name"));
        textType.setText(bundle.getString("type"));
        textType2.setText(bundle.getString("type2"));
        String tempWeight = bundle.getString("weight")+" g";
        textWeight.setText(tempWeight);
        String tempHeight = bundle.getString("height")+" cm";
        textHeight.setText(tempHeight);
        textDescription.setText(descriptionResult);
        String imgPath = bundle.getString("imagePath");
        Glide.with(this).load(imgPath).into(imgSprite);

        String abilities = "";
        for(int i=0;i<bundle.getStringArrayList("abilities").size();i++){
            abilities += bundle.getStringArrayList("abilities").get(i);
            if(i+1 == bundle.getStringArrayList("abilities").size()){
                abilities += ".";
            }else{
                abilities += ", ";
            }
        }
        textAbilities.setText(abilities);
    }
    class FetchDescription extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            Bundle bundle = getIntent().getExtras();
            String indexString = bundle.getString("id");
            int index = Integer.valueOf(indexString);
            String jsonString = null;
            try{
                String urlString = "https://pokeapi.co/api/v2/pokemon-species/" + index;
                //Log.e("hehe",urlString);
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                int lengthOfFile = urlConnection.getContentLength();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if(inputStream == null){
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }

                if(stringBuffer.length() == 0){
                    return null;
                }

                jsonString = stringBuffer.toString();
                Log.d("FETCHDATA",jsonString);
                //KONVERSI STRING -> JSON UNTUK UI
                JSONObject jsonObject = new JSONObject(jsonString);


                JSONArray jsonArrayDescription = jsonObject.getJSONArray("flavor_text_entries");
                JSONObject descriptionLanguageCode;
                String LangCode;
                for(int i=0;i<jsonArrayDescription.length();i++){
                    JSONObject descriptionObj = jsonArrayDescription.getJSONObject(i);
                    descriptionLanguageCode = descriptionObj.getJSONObject("language");
                    LangCode = descriptionLanguageCode.getString("name");
                    if(LangCode.equals("en")){
                        descriptionResult = descriptionObj.getString("flavor_text");
                        break;
                    }
                }
                Log.d("HIHIHI",descriptionResult);
                //Log.d("HIHIHI","1");
                //JSONObject descriptionObj = jsonArrayDescription.getJSONObject(1);
                //Log.d("HIHIHI","2");
                //JSONObject ability = new JSONObject(abilities.getString("ability"));
                //Log.d("HIHIHI","3");
                //descriptionResult = descriptionObj.getString("flavor_text");
                //Log.d("HIHIHI","4");

            }
            catch (MalformedURLException e){
                Log.e("MALFORMED","MalformedURLException: " + e.getMessage());
            }
            catch (IOException e){
                Log.e("IO","IOException: " + e.getMessage());
            }
            catch(JSONException e){
                Log.e("JSON","JSONException: " + e.getMessage());
            }
            finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(bufferedReader != null){
                    try{
                        bufferedReader.close();
                    }
                    catch(IOException e){
                        Log.e("BUFFEREDIOEXCEPTION","IOException: " + e.getMessage());
                    }
                }
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setToTextView();
            btnFab = findViewById(R.id.floatingActionButton);
            btnFab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Bundle bundle =  getIntent().getExtras();
                    Toast.makeText(DetailPokemonActivity.this,"Notification Alert.",Toast.LENGTH_LONG).show();
                    final String CHANNEL_ID = "Channel Notification Project UAS 00000013728";
                    final NotificationManager notificationManager = DetailPokemonActivity.this.getSystemService(NotificationManager.class);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        CharSequence name = "Notification";
                        String description = "Description";
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
                        channel.setDescription(description);
                        notificationManager.createNotificationChannel(channel);
                    }
                    final NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(DetailPokemonActivity.this,CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(bundle.getString("name"))
                            .setContentText(bundle.getString("type"))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true);
                    notificationManager.notify(1,builder.build());
                }
            });
        }


    }
}
