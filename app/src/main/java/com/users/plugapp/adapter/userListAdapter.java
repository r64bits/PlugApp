package com.users.plugapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.users.plugapp.MainActivity;
import com.users.plugapp.R;
import com.users.plugapp.UserActivity;
import com.users.plugapp.model.userItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andrade on 08/12/2016.
 */
public class userListAdapter extends BaseAdapter {
    MainActivity mainActivity;
    ArrayList<userItem> items;
    private static LayoutInflater inflater=null;

    public userListAdapter(MainActivity activity, JSONArray users) {
        mainActivity=activity;
        items = new ArrayList<userItem>();
        FillUserList(users);


        inflater = ( LayoutInflater )mainActivity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void FillUserList(JSONArray users) {
        for(int i=0; i<users.length(); i++){
            try {
                userItem u = new userItem();
                JSONObject jObject =users.getJSONObject(i).getJSONObject("user");
                u.Id = Integer.parseInt(jObject.getString("id"));
                u.FullName = jObject.getString("full_name");
                u.Email = jObject.getString("email");
                items.add(u);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return  items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  items.get(position).Id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
       final userItem currentItem = (userItem) getItem(position);
       View rowView = inflater.inflate(R.layout.user_item, null);
        Button editButton,removeButton;
        TextView fullNameTextView,emailTextView;

        editButton = (Button)rowView.findViewById(R.id.l_editButton);
        removeButton = (Button)rowView.findViewById(R.id.l_removeButton);

        fullNameTextView = (TextView)rowView.findViewById(R.id.l_fullName);
        emailTextView = (TextView)rowView.findViewById(R.id.l_email);

        fullNameTextView.setText(currentItem.FullName);
        emailTextView.setText(currentItem.Email);


        if(currentItem.Id == 21428759){
            removeButton.setVisibility(View.GONE);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(mainActivity, UserActivity.class);
                Bundle b = new Bundle();
                b.putInt("id",  currentItem.Id); //Your id
                b.putString("fullname",  currentItem.FullName);
                b.putString("email",  currentItem.Email);
                intent.putExtras(b);
                mainActivity.startActivity(intent);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setCancelable(true);
                builder.setTitle("Remover");
                builder.setMessage("Tem certeza que deseja excluir este usuário?");
                builder.setPositiveButton("Confirmar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mainActivity.RemoveUser(currentItem.Id);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return rowView;
    }
}
