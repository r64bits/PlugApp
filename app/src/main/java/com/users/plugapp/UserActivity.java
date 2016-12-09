package com.users.plugapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.users.plugapp.service.UpdateUserService;
import com.users.plugapp.util.Session;


public class UserActivity extends AppCompatActivity implements IService {
    private  int userId;
    private  String fullName;
    private  String email;

    EditText fullNameEditText,emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intentExtras = getIntent();
        Bundle extrasBlundle = intentExtras.getExtras();
        this.userId = extrasBlundle.getInt("id");
        this.fullName = extrasBlundle.getString("fullname");
        this.email = extrasBlundle.getString("email");

        fullNameEditText = (EditText)findViewById(R.id.e_fullName);
        emailEditText = (EditText)findViewById(R.id.e_email);

        fullNameEditText.setText(this.fullName);
        emailEditText.setText(this.email);
    }


    @Override
    public void onServiceSuccess(String serviceName,String response) {
        switch (serviceName){
            default:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    @Override
    public void onServiceError(String serviceName,String messageError) {
        Toast.makeText(this,"Ocorreu um erro ao tentar realizar esta operação",Toast.LENGTH_LONG);
    }

    public void SaveUser(View v){
        UpdateUserService service = new UpdateUserService(this.getApplicationContext(),this,"users/"+userId+".json");
        service.Update(fullNameEditText.getText().toString(),emailEditText.getText().toString());
    }

    public  void CancelEdit(View v){
        finish();
    }
}
