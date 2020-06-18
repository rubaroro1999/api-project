package com.example.rubafikri.dataPro;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

public class showUser extends AppCompatActivity {

    EditText editTextPassword, editTextName, editTextEmail;
    EditText editTextuserId;

    ProgressBar progressBar;
    ListView listView;
    Button buttonAddUpdate;
    List<user> userList;
    RequestQueue queue;
    boolean isUpdating = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);
        editTextuserId = (EditText) findViewById(R.id.editTextuserId);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonAddUpdate = (Button) findViewById(R.id.buttonAddUpdate);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listViewHeroes);
        queue = Volley.newRequestQueue(getApplicationContext());
        userList = new ArrayList<>();


        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdating) {
                    updateHero();
                } else {
                    createHero();
                }
            }
        });
        readHeroes();
    }


    private void createHero() {
        final String uname = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        final String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(uname)) {
            editTextName.setError("Please enter name");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter Email");
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter Password");
            editTextPassword.requestFocus();
            return;
        }

        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.URL_CREATE_USER,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if (!object.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();

                                refreshHeroList(object.getJSONArray("users"));
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
                params.put("uname", uname);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        queue.add(postRequest);
    }


    class userAdapter extends ArrayAdapter<user> {
        List<user> userList;

        public userAdapter(List<user> userList) {
            super(showUser.this, R.layout.layout_hero_list, userList);
            this.userList = userList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_hero_list, null, true);

            TextView textViewName = listViewItem.findViewById(R.id.textViewName);

            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final user user = userList.get(position);

            textViewName.setText(user.getUname());

            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isUpdating = true;

                    editTextuserId.setText(String.valueOf(user.getId()));

                    editTextName.setText(user.getUname());
                    editTextEmail.setText(user.getEmail());
                    editTextPassword.setText(user.getPassword());

                    buttonAddUpdate.setText("Update");
                }
            });

            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(showUser.this);

                    builder.setTitle("Delete " + user.getUname())
                            .setMessage("Are you sure you want to delete it?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteUser(user.getId());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

            return listViewItem;
        }
    }



    private void readHeroes() {
        StringRequest getRequest = new StringRequest(Request.Method.GET, Api.URL_READ_USER,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if (!object.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                refreshHeroList(object.getJSONArray("users"));
                            }}
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }} ,

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue.add(getRequest);
    }
    private void refreshHeroList(JSONArray users) throws JSONException {
        userList.clear();


        for (int i = 0; i < users.length(); i++) {
            JSONObject obj = users.getJSONObject(i);

            userList.add(new user(
                    obj.getInt("uid"),
                    obj.getString("uname"),
                    obj.getString("email"),
                    obj.getString("password")));
        }

        showUser.userAdapter adapter = new showUser.userAdapter(userList);
        listView.setAdapter(adapter);
    }
    private void updateHero() {
        final String id = editTextuserId.getText().toString();
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();




        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Please enter name");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter real name");
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter real name");
            editTextPassword.requestFocus();
            return;
        }
        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.URL_UPDATE_USER,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if (!object.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                refreshHeroList(object.getJSONArray("users"));
                            }}
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }} ,

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String, String> params = new HashMap<>();
                params.put("uid", id);

                params.put("uname", name);
                params.put("email", email);
                params.put("password",password);
                return params;
            }
        };
        queue.add(postRequest);





        buttonAddUpdate.setText("Add");

        editTextName.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        isUpdating = false;
    }


    private void deleteUser(int uid) {
        StringRequest getRequest = new StringRequest(Request.Method.GET, Api.URL_DELETE_USER+uid,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if (!object.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                refreshHeroList(object.getJSONArray("users"));
                            }}
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }} ,

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue.add(getRequest);
    }
}
