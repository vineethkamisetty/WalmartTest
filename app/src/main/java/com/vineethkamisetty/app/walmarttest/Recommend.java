package com.vineethkamisetty.app.walmarttest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recommend extends AppCompatActivity {

    private static String TAG = "Recommend";
    private RequestQueue requestQueue;
    private ProgressDialog dialog;
    private List<Item> itemList;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        Intent intent = getIntent();

        long itemId = intent.getLongExtra("itemId", 0);
        RecyclerView mList = findViewById(R.id.recommendation_list);

        itemList = new ArrayList<>();
        adapter = new RecommendationAdapter(getApplicationContext(), itemList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(this);
        getData(itemId);
    }

    private void getData(long itemId) {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching Items...");
        showDialog();

        String apiKey = "kbw5f9h5khnrbhjjc9cugb34";
        String recommend_query = "http://api.walmartlabs.com/v1/nbp?apiKey=" + apiKey + "&itemId=" + itemId;
        JsonArrayRequest request = new JsonArrayRequest(recommend_query,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = (JSONObject) response.get(i);

                                Item item = new Item();

                                String name = jsonObject.getString("name");

                                if (name.length() <= 50)
                                    item.setItemName(name);
                                else {
                                    item.setItemName(name.substring(0, 45) + "....");
                                }
                                item.setItemPrice(jsonObject.getDouble("salePrice"));

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
                    finish();
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
