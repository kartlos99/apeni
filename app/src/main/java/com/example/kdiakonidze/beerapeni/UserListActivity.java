package com.example.kdiakonidze.beerapeni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kdiakonidze.beerapeni.adapters.UserListAdapter;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;

import java.io.Serializable;

public class UserListActivity extends AppCompatActivity {

    UserListAdapter userListAdapter;
    ListView userslistView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userslistView = findViewById(R.id.listview_users);

        userListAdapter = new UserListAdapter(this, Constantebi.USERsLIST);
        userslistView.setAdapter(userListAdapter);

        userslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (Constantebi.USER_TYPE.equals(Constantebi.USER_TYPE_user)){
                    Toast.makeText(getApplicationContext(), R.string.ufleba_false, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent_newuser = new Intent(getApplicationContext(), AddEditUser.class);
                    intent_newuser.putExtra(Constantebi.REASON, Constantebi.EDIT);
                    intent_newuser.putExtra("user", (Serializable) userListAdapter.getItem(i));
                    startActivity(intent_newuser);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userListAdapter.notifyDataSetChanged();
    }
}
