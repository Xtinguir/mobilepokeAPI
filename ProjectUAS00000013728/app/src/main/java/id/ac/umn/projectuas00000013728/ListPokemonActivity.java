package id.ac.umn.projectuas00000013728;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

public class ListPokemonActivity extends AppCompatActivity {
    String url = "https://pokeapi.co/api/v2/pokemon/";
    ArrayList<Pokemon> listPokemon;
    private RecyclerView recyclerView;
    private PokemonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pokemon);
        listPokemon = new ArrayList<Pokemon>();
        new FetchData().execute();

    }

    private class FetchData extends AsyncTask<String, Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String jsonString = null;

            for(int i=1;i<152;i++) {
                try {
                    String urlString = "";
                    //String urlString2="";

                    urlString = "https://pokeapi.co/api/v2/pokemon/" + i;
                    //urlString2 = "https://pokeapi.co/api/v2/pokemon-species/" + i;
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    int lengthOfFile = urlConnection.getContentLength();

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line + "\n");
                    }
                    if (stringBuffer.length() == 0) {
                        return null;
                    }
                    jsonString = stringBuffer.toString();
                    Log.d("FETCHDATA", jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    /*Import Required Data Down Here*/
                    if(true){
                        JSONObject jsonSprite = new JSONObject(jsonObject.getString("sprites"));
                        //Log.d("FETCHDATA", "Sprite");
                        //Log.d("FETCHDATA", jsonSprite.toString());
                        String jsonFrontImagePath = jsonSprite.getString("front_default");
                        //Log.d("FETCHDATA", "Front Default");
                        //Log.d("FETCHDATA", jsonFrontImagePath.toString());

                        Pokemon pokemon = new Pokemon();
                        pokemon.setSpriteURL(jsonFrontImagePath);

                        //Log.d("FETCHDATA", "Create pokemon object");
                        pokemon.setOrder(jsonObject.getInt("id"));
                        //Log.d("FETCHDATA", "Order");
                        pokemon.setName(jsonObject.getString("name"));
                        //Log.d("FETCHDATA", pokemon.getName());
                        pokemon.setWeight(jsonObject.getInt("weight"));
                        //Log.d("FETCHDATA", "Weight");
                        pokemon.setHeight(jsonObject.getInt("height"));
                        Log.d("FETCHDATA", String.valueOf(pokemon.getHeight()));


                        /*Ability Import Data*/
                        JSONArray abilityArray = jsonObject.getJSONArray("abilities");
                        ArrayList<String> abilityList = new ArrayList<String>();
                        for(int j=0;j<abilityArray.length();j++){
                            JSONObject abilities = abilityArray.getJSONObject(j);
                            JSONObject ability = new JSONObject(abilities.getString("ability"));
                            String abilityName = ability.getString("name");
                            abilityList.add(abilityName);
                            //Log.d("FETCHDATA",abilityName);
                        }
                        pokemon.setAbilityList(abilityList);

                        /*Type Import Data*/
                        JSONArray typeArray = jsonObject.getJSONArray("types");
                        JSONObject type1Object = typeArray.getJSONObject(0);
                        JSONObject type1 = new JSONObject(type1Object.getString("type"));
                        String type1Name = type1.getString("name");
                        //Log.d("FETCHDATA",type1Name);
                        pokemon.setType(type1Name);
                        if(typeArray.length()==2){
                            JSONObject type2Object = typeArray.getJSONObject(1);
                            JSONObject type2 = new JSONObject(type2Object.getString("type"));
                            String type2Name = type2.getString("name");
                            //Log.d("FETCHDATA",type2Name);
                            pokemon.setTyp2(type2Name);
                        }
                        else{
                            String typeEmpty = "-";
                            pokemon.setTyp2(typeEmpty.toString());
                        }
                        Log.d("FETCHDATA",pokemon.getTyp2());
                        listPokemon.add(pokemon);
                    }

                } catch (MalformedURLException e) {
                    Log.e("MALFORMED", "MalformedURLException: " + e.getMessage());
                } catch (IOException e) {
                    Log.e("IO", "IOException: " + e.getMessage());
                } catch (JSONException e) {
                    Log.e("JSON", "JSONException: " + e.getMessage());
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            Log.e("BUFFEREDIOEXCEPTION", "IOException: " + e.getMessage());
                        }
                    }
                }
            }//END LOOP
            return jsonString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview_listPokemon);
            Log.d("SHOWDATA", "2");
            adapter = new PokemonAdapter(listPokemon);
            Log.d("SHOWDATA", "3");
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListPokemonActivity.this);
            Log.d("SHOWDATA", "4");
            recyclerView.setLayoutManager(layoutManager);
            Log.d("SHOWDATA", "5");
            recyclerView.setAdapter(adapter);
        }

    }
}
