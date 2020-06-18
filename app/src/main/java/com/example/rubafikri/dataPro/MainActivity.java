package com.example.rubafikri.dataPro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  MainActivity extends AppCompatActivity {

    EditText editTextHeroId, editTextName, editTextPrice , editTextKey;
    ProgressBar progressBar;
    ListView listView;
    Button buttonAddUpdate;
    List<proudct> proudctList;
    RequestQueue queue;
    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextHeroId = (EditText) findViewById(R.id.editTextHeroId);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPrice = (EditText) findViewById(R.id.editTextRealname);
        editTextKey = (EditText) findViewById(R.id.editTextKey);

        buttonAddUpdate = (Button) findViewById(R.id.buttonAddUpdate);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listViewProudcts);
        queue = Volley.newRequestQueue(getApplicationContext());
        proudctList = new ArrayList<>();


        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdating) {
                    updateHero();
                } else {
                    createProducts();
                }
            }
        });
        readProduct();
    }


    private void createProducts() {
        final String name = editTextName.getText().toString().trim();
        final String price = editTextPrice.getText().toString().trim();
        final String key = editTextKey.getText().toString().trim();




        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Please enter name");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(price)) {
            editTextPrice.setError("Please enter real name");
            editTextPrice.requestFocus();
            return;
        }



        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.URL_CREATE_PROUDCT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if (!object.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                refreshProductList(object.getJSONArray("pros"));
                            }}
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("price", price);
                params.put("keyword", key);
                return params;
            }
        };

        editTextName.setText("");
        editTextPrice.setText("");
        editTextKey.setText("");
        queue.add(postRequest);
    }


    class ProductAdapter extends ArrayAdapter<proudct> {

        List<proudct> proudctList;

        public ProductAdapter(List<proudct> proudctList) {
            super(MainActivity.this, R.layout.layout_hero_list, proudctList);
            this.proudctList = proudctList;
        }


        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_hero_list, null, true);

            TextView textViewName = listViewItem.findViewById(R.id.textViewName);

            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final proudct proudct = proudctList.get(position);

            textViewName.setText(proudct.getName());

            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isUpdating = true;

                    editTextHeroId.setText(String.valueOf(proudct.getPid()));
                    editTextName.setText(proudct.getName());
                    editTextPrice.setText(proudct.getPrice());
                    editTextKey.setText(proudct.getKey());

                    buttonAddUpdate.setText("Update");
                }
            });

            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteProduct(proudct.getPid());
                }
            });

            return listViewItem;
        }
    }



    private void readProduct() {
        StringRequest getRequest = new StringRequest(Request.Method.GET, Api.URL_READ_PROUDCT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if (!object.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                refreshProductList(object.getJSONArray("pros"));
                            }}
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }} ,

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue.add(getRequest);
    }
    private void refreshProductList(JSONArray pros) throws JSONException {
        proudctList.clear();
        for (int i = 0; i < pros.length(); i++) {
            JSONObject obj = pros.getJSONObject(i);

            //adding the hero to the list
            proudctList.add(new proudct(
                    obj.getInt("pid"),
                    obj.getString("name"),
                    obj.getString("price"),
                    obj.getString("keyword"))
            );
        }

        //creating the adapter and setting it to the listview
        ProductAdapter adapter = new ProductAdapter(proudctList);
        listView.setAdapter(adapter);
    }
    private void updateHero() {
        final String pid = editTextHeroId.getText().toString();
        final String name = editTextName.getText().toString().trim();
        final String price = editTextPrice.getText().toString().trim();
        final String Keyword = editTextKey.getText().toString().trim();




        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Please enter name");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(price)) {
            editTextPrice.setError("Please enter real name");
            editTextPrice.requestFocus();
            return;
        }
        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.URL_UPDATE_PROUDCT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try{
                            JSONObject object = new JSONObject(response);
                            if (!object.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                refreshProductList(object.getJSONArray("pros"));
                            }}
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }} ,

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String, String> params = new HashMap<>();
                params.put("pid", pid);

                params.put("name", name);
                params.put("price", price);
                params.put("keyword",Keyword);

                return params;
            }
        };
        queue.add(postRequest);





        buttonAddUpdate.setText("Add");

        editTextName.setText("");
        editTextPrice.setText("");
        editTextKey.setText("");

        isUpdating = false;
    }


    private void deleteProduct(int pid) {
        StringRequest getRequest = new StringRequest(Request.Method.GET, Api.URL_DELETE_PROUDCT+pid,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try{
                            JSONObject object = new JSONObject(response);
                            if (!object.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                //refreshing the herolist after every operation
                                //so we get an updated list
                                //we will create this method right now it is commented
                                //because we haven't created it yet
                                refreshProductList(object.getJSONArray("pros"));
                            }}
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }} ,

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue.add(getRequest);
    }
}

