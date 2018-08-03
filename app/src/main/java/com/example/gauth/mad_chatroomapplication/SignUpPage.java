package com.example.gauth.mad_chatroomapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String FirstName;
    String LastName;
    String email;
    String password;
    String Uid;

    EditText username;
    EditText Password;
    EditText firstName;
    EditText lastName;

    public DatabaseReference databaseReference;
    public DatabaseReference conditionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        conditionReference=databaseReference.child("TestChatroomUsers1");

        mAuth = FirebaseAuth.getInstance();
        firstName=(EditText)findViewById(R.id.firstName);
        lastName=(EditText)findViewById(R.id.lastName);
        username=(EditText)findViewById(R.id.username);
        Password=(EditText)findViewById(R.id.password2);

    }


    public void SignUp(View view)
    {
        FirstName=firstName.getText().toString();
        LastName=lastName.getText().toString();
        email=username.getText().toString();
        password=Password.getText().toString();
        Toast.makeText(this,"FirstName "+FirstName+"LastName "+LastName,Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Username "+email+"Password "+password,Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Context context=SignUpPage.this;
                            Toast.makeText(context,"Successful sign up!",Toast.LENGTH_SHORT).show();
                            Uid = mAuth.getCurrentUser().getUid();
                            UserSignUpDetails userSignUpDetails=
                                    new UserSignUpDetails(FirstName,LastName,email,password,Uid);

                          //  firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                            conditionReference.push().setValue(userSignUpDetails);

                            Intent intent=new Intent(SignUpPage.this,Chats.class);
                            intent.putExtra("CurrentUser",FirstName.toString());
                            startActivity(intent);
                        }else {
                            Context context=SignUpPage.this;
                            Toast.makeText(context,"Unsuccessful in signing up!",Toast.LENGTH_SHORT).show();
                            Log.i("Exception", task.getException().getMessage());
                        }
                    }
                });
    }
public void cancel(View view)
{
    firstName.setText("");
    lastName.setText("");
    username.setText("");
    Password.setText("");
    finish();
}
}
