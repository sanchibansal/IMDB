package com.example.sakshi.imdb;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;

import static android.R.attr.id;

//TODO: add code for exception when there is no internet connection

public class MainActivity extends AppCompatActivity implements DataListener {
    ListView listView;
    ArrayList<Model> modelArrayList;
    CustomAdapter customAdapter;
    String url;
    Database database;
    LinearLayout linearLayout;
    int flag ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        database = new Database(MainActivity.this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        modelArrayList = new ArrayList<>();
        url = "http://api.themoviedb.org/3/movie/upcoming?api_key=8496be0b2149805afa458ab8ec27560c";
        listView = (ListView) findViewById(R.id.listview);
        checkConnection();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("getid", modelArrayList.get(position).getId());
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void checkConnection() {
        //TODO: shows is true even if wifi is not connected to the system.
        if (isConnectedToInternet()) {
            //calling Async Task
            DataProcess dataProcess = new DataProcess(MainActivity.this, url, this,linearLayout);
            dataProcess.execute();
        } else {
            Snackbar snackbar = Snackbar.make(linearLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //isInternetAvailable();
                    checkConnection();
                }
            });
            snackbar.show();
        }

        //custom adapter for displaying listitems
        customAdapter = new CustomAdapter(this, modelArrayList);
        //setting adapter to listview
        listView.setAdapter(customAdapter);

    }

    private boolean isConnectedToInternet() {
        boolean isconnected = false;
        //connectivity managee for checking the internet connection of the device
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            isconnected = true;
        }
        return isconnected;
    }
/*    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }
*/

    @Override
    public void updatelist(String data) {
        try {
            //creating jsonObject
            JSONObject jsonObject = new JSONObject(data);
            //getting array from the json object
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                //getting object i from the JsonArray
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                //getting data from i object in the array
                String title = jsonObject1.getString("title");
                String release_date = jsonObject1.getString("release_date");
                String popularity = jsonObject1.getString("popularity");
                String vote_count = jsonObject1.getString("vote_count");
                String vote_average = jsonObject1.getString("vote_average");
                String poster_path = jsonObject1.getString("poster_path");
                String id = jsonObject1.getString("id");
                Model model = new Model();
                //setting name, votes and id
                model.setTitle(title);
                model.setRelease_date(release_date);
                model.setPopularity(popularity);
                model.setVote_count(vote_count);
                model.setVote_average(vote_average);
                model.setPoster_path(poster_path);
                model.setId(id);
                //adding model to mmodelArrayList
                modelArrayList.add(model);
            }
            customAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mostpop:
                Mostpop();
                break;
            case R.id.UpcomingMovies:
                upcoming();
                break;
            case R.id.toprated:
                topRated();
                break;
            case R.id.NowPlaying:
                nowPlaying();
                break;
            case R.id.Fav:
                Intent favintent = new Intent(MainActivity.this, FavoritesList.class);
                flag=1;
                favintent.putExtra("fav",flag);
                startActivity(favintent);
                break;
            case R.id.watchlist:
                Intent watchintent = new Intent(MainActivity.this,FavoritesList.class);
                flag = 2;
                watchintent.putExtra("fav",flag);
                startActivity(watchintent);
                break;
            case R.id.refresh:
                String title = this.getTitle().toString();
                switch (title) {
                    case "Most Popular":
                        Mostpop();
                        break;
                    case "Nowplaying":
                        nowPlaying();
                        break;
                    case "Upcoming":
                        upcoming();
                        break;
                    case "Top Rated":
                        topRated();
                        break;
                    default:

                }
        }
        return super.onOptionsItemSelected(item);
    }

    void topRated() {
        url = "http://api.themoviedb.org/3/movie/top_rated?api_key=f47dd4de64c6ef630c2b0d50a087cc33";
        modelArrayList.clear();
        this.setTitle("Top Rated");
        checkConnection();
    }

    void upcoming() {
        url = "http://api.themoviedb.org/3/movie/upcoming?api_key=8496be0b2149805afa458ab8ec27560c";
        modelArrayList.clear();
        this.setTitle("Upcoming");
        checkConnection();
    }

    void Mostpop() {
        url = "http://api.themoviedb.org/3/movie/popular?api_key=f47dd4de64c6ef630c2b0d50a087cc33";
        modelArrayList.clear();
        this.setTitle("Most Popular");
        checkConnection();
    }

    void nowPlaying() {
        url = "http://api.themoviedb.org/3/movie/now_playing?api_key=8496be0b2149805afa458ab8ec27560c";
        modelArrayList.clear();
        this.setTitle("Nowplaying");
        checkConnection();
    }
}
