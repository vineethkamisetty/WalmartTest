package com.vineethkamisetty.app.walmarttest;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private String apiKey = "kbw5f9h5khnrbhjjc9cugb34";
    private RequestQueue requestQueue;
    private ProgressDialog dialog;
    private List<Item> itemList;
    private RecyclerView.Adapter adapter;
    private ViewFlipper viewFlipper;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFlipper = findViewById(R.id.viewFlipper);
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.textLayout)));

        RecyclerView mList = findViewById(R.id.main_list);

        itemList = new ArrayList<>();
        adapter = new ItemAdapter(getApplicationContext(), itemList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        requestQueue = Volley.newRequestQueue(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                itemList.clear();
                String search_query = "http://api.walmartlabs.com/v1/search?apiKey=" + apiKey + "&query=" + query;
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.viewLayout)));
                getData(search_query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.textLayout)));
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void getData(String url) {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching Items...");
        showDialog();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                try {
                    int length = response.getInt("numItems");
                    JSONArray jsonArray = response.getJSONArray("items");

                    for (int i = 0; i < length; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Item item = new Item();

                        String name = jsonObject.getString("name");
                        if (name.length() <= 50)
                            item.setItemName(name);
                        else
                            item.setItemName(name.substring(0, 45) + "....");

                        item.setItemPrice(jsonObject.getDouble("salePrice"));

                        item.setItemId(jsonObject.getLong("itemId"));

                        item.setItemShortDescription(jsonObject.getString("shortDescription"));

                        if (!jsonObject.getBoolean("bundle")) {
                            if (jsonObject.has("customerRating"))
                                item.setItemRating("Rating : " + jsonObject.getString("customerRating"));
                            else
                                item.setItemRating("Rating Not Available");
                            item.setItemThumbnailImage(jsonObject.getString("thumbnailImage"));
                        } else {
                            item.setItemRating("Not Available");
                            item.setItemThumbnailImage(null);
                        }
                        itemList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
                adapter.notifyDataSetChanged();
                dismissDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(),
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                    dismissDialog();
                    return;
                }

                setContentView(R.layout.error_text);
                TextView tv = findViewById(R.id.textView_error);

                if (error != null) {
                    Log.e(TAG, error.toString());

                    if (error.getMessage().contains("No recommendations found")) {
                        tv.setText(R.string.no_recommendation);
                    } else {
                        tv.setText(R.string.service_unavailable);
                    }
                } else {
                    tv.setText(R.string.service_unavailable);
                }
                dismissDialog();
            }
        });
        requestQueue.add(request);
    }

    private void showDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    private void dismissDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
