package com.example.pokedox;

import android.content.Context;
import android.content.Intent;
import android.security.ConfirmationNotAvailableException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PokedoxAdapter extends RecyclerView.Adapter<PokedoxAdapter.PokedoxViewHolder> {

    public static class PokedoxViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout containerview;
        public  TextView textView;

        PokedoxViewHolder(View view){
            super(view);

            containerview = view.findViewById(R.id.pokedox_row);
            textView = view.findViewById(R.id.pokedox_row_text_view);

            containerview.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Pokemon current = (Pokemon) containerview.getTag();
                    Intent intent = new Intent(v.getContext(), PokemonActivity.class);
                    intent.putExtra("url", current.getUrl());


                    v.getContext().startActivity(intent);
                }

            });
        }
    }

    private List<Pokemon> pokemon = new ArrayList<>();
    private RequestQueue requestQueue;

    PokedoxAdapter(Context context){
         requestQueue = Volley.newRequestQueue(context);
         loadPokemon();
    }

    public void loadPokemon() {

        String url = "https://pokeapi.co/api/v2/pokemon/?limit=151";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++){

                        JSONObject result = results.getJSONObject(i);
                        String name = result.getString("name");

                        pokemon.add(new Pokemon(
                                name.substring(0, 1).toUpperCase() + name.substring(1),
                                result.getString("url")
                        ));
                    }

                    notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.e("error", "JSON error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "List error");
            }
        });

        requestQueue.add(request);
    }


    @NonNull
    @Override
    public PokedoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokedox_row, parent, false);

        return new PokedoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokedoxViewHolder holder, int position) {
         Pokemon current = pokemon.get(position);
         holder.textView.setText(current.getName());
         holder.containerview.setTag(current);
    }

    @Override
    public int getItemCount() {
        return pokemon.size();
    }
}
