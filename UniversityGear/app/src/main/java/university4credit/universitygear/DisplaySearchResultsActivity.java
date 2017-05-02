package university4credit.universitygear;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;

public class DisplaySearchResultsActivity extends AppCompatActivity {
    public final static String ITEM_ID = "university4credit.universitygear.ID";
    private static final String TAG = "RecyclerViewExample";
    private String searchResults;
    private int searchOffset = 0;

    private List<Item> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Item feed;
    private LinearLayoutManager linearLayoutManager;
    private List<Item> itemFeed = null;
    private int itemCount = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_results);

        Intent intent = getIntent();
        searchResults = intent.getStringExtra(SearchActivity.EXTRA_MESSAGE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                searchOffset++;
                //Toast.makeText(DisplaySearchResultsActivity.this, "Scroll Listener is Working", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedpref= getSharedPreferences("Search",Context.MODE_PRIVATE);
                String word = sharedpref.getString("SearchWord","");
                Log.d("word", word);
                new ScrollSearchTask().execute(word,String.valueOf(searchOffset));

            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);
        new DisplayResultsTask().execute();
    }
    private class ScrollSearchTask extends AsyncTask<String, Void, List <Item>>{
        @Override
        protected List<Item> doInBackground(String... params) {
            String itemLimit = "200";
            URL url = null;
            HttpURLConnection conn = null;
            HttpURLConnection conn3 = null;
            InputStream inputstream = null;
            String oAuthtoken = null;
            String apiResults = "";
            Item items = null;

            SharedPreferences sharedPreference = getSharedPreferences("Authentication", MODE_PRIVATE);
            String urlString = sharedPreference.getString("query", null);
            oAuthtoken = sharedPreference.getString("oAuthToken",null);
            Integer total = Integer.valueOf(sharedPreference.getString("total", null));

            Log.e("total", total.toString());
            if((Integer.valueOf(params[1])*10)>(total+10)){
                items = new Item(apiResults, false);
                itemFeed = items.itemFeed;
            }
            urlString += "&offset=" + params[1];
            Log.e("query",urlString);
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();

            } catch (IOException e) {
                e.printStackTrace();
            }

            //this is the header that you need to pass in. The authorization key is something you get from the eBAy website. I can tell you how to get them but I think we can just use my key
            conn.setRequestProperty("Authorization", "Bearer " + oAuthtoken);

            //I added this header just so it can get the json object. I'm not sure if its necessary but can't hurt to put that in
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            try {
                //since im getting item feeds, I set my request method to get
                conn.setRequestMethod("GET");
                //conn.setDoOutput(false);
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            int stat;
            try {
                //this line is here to test the code. If it returns 200 then it succeeds otherwise theres something wrong with the payload
                stat = conn.getResponseCode();
                Log.e("DSRA - Connection code", " " + stat);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("e", "Params= " + params[0]);
            try {
                //get the item feed
                inputstream = conn.getInputStream();
                apiResults = SearchActivity.getStringFromInputStream(inputstream);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Log.e("RESULT STRING", "" + apiResults);
            if (apiResults != null && apiResults.length() > 0) {
                items = new Item(apiResults, false);
                itemFeed = items.itemFeed;
                return itemFeed;
            }
            else {
                items = new Item(true);
            }
            return items.itemFeed;
        }

        @Override
        protected void onPostExecute(List<Item> result) {
            progressBar.setVisibility(View.GONE);

            if (result.size() > 0) {
                adapter = new MyRecyclerViewAdapter(DisplaySearchResultsActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    //@Override
                    public void onItemClick(Item item) {
                        Intent singleItemIntent = new Intent(DisplaySearchResultsActivity.this, DisplaySingleItemActivity.class);
                        singleItemIntent.putExtra(ITEM_ID, item.itemID);
                        startActivity(singleItemIntent);
                    }
                });
                feedsList.addAll(result);
                if(feedsList.size() > 200) {
                    Log.e("DSRA - state, size:", " " + feedsList.size());
                    adapter.notifyItemRangeInserted(0, result.size());
                }
            } else {
                Toast.makeText(DisplaySearchResultsActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class DisplayResultsTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                    //parseResult(searchResults);
                    feed = new Item(searchResults, false);
                    feedsList = feed.itemFeed;
                    itemCount += 200;
                    result = 1; // Successful
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                adapter = new MyRecyclerViewAdapter(DisplaySearchResultsActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    //@Override
                    public void onItemClick(Item item) {
                        Intent singleItemIntent = new Intent(DisplaySearchResultsActivity.this, DisplaySingleItemActivity.class);
                        singleItemIntent.putExtra(ITEM_ID, item.itemID);
                        startActivity(singleItemIntent);
                    }
                });
            } else {
                Toast.makeText(DisplaySearchResultsActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}