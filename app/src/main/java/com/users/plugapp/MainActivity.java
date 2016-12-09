package com.users.plugapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.users.plugapp.adapter.userListAdapter;
import com.users.plugapp.service.RemoveUserService;
import com.users.plugapp.service.UserListService;
import com.users.plugapp.util.Session;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements IService {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Session.CreateSession(this.getApplicationContext(),this);
    }

    public void LoadUserList(){
        UserListService service = new UserListService(this.getApplicationContext(),this);
        service.GetAllUsers();
    }

    public void RemoveUser(int id){
        RemoveUserService service = new RemoveUserService(this.getApplicationContext(),this,"users/"+id+".json");
        service.Remove();
    }

    @Override
    public void onServiceSuccess(String serviceName,String response) {
        switch (serviceName){
            case "session.json":
                LoadUserList();
                break;
            case "users.json":
                try {
                    JSONObject json = new JSONObject(response);
                    userListAdapter adapter = new userListAdapter(this,json.getJSONArray("items"));
                    ListView usersListView = (ListView) findViewById(R.id.listViewUsers);
                    usersListView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                LoadUserList();
        }
    }

    @Override
    public void onServiceError(String serviceName,String messageError) {
        Toast.makeText(this,"Ocorreu um erro ao tentar realizar esta operação",Toast.LENGTH_LONG);
    }
}
