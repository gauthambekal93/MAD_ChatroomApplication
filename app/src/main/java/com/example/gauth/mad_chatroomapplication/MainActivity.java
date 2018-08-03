package com.example.gauth.mad_chatroomapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public DatabaseReference databaseReference;
    public DatabaseReference conditionReference;
    private FirebaseAuth mAuth;
    static FirebaseUser firebaseUser;
    EditText email;
    EditText password;
 String currentUserName="DEFAULT USER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Toast.makeText(MainActivity.this,"USER SIGNED IN IS  "+firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();

        }else
        {
            Toast.makeText(MainActivity.this,"USER IS NOT SIGNED IN",Toast.LENGTH_SHORT).show();
        }
        Button login=(Button)findViewById(R.id.login);
        Button signUp=(Button)findViewById(R.id.signUp);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        conditionReference=databaseReference.child("TestChatroomUsers1");
        mAuth = FirebaseAuth.getInstance();

        //LOGIN CLICKED

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Login ","Pressed");

                login(email.getText().toString(),password.getText().toString());
            }
        });

        //SIGNUP CLICKED

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("SignUp","Pressed");
                //conditionReference.push().setValue("SIGNUP PRESSED");
                Intent signUpIntent= new Intent(MainActivity.this, SignUpPage.class);
                startActivity(signUpIntent);
            }
        });

    }


    public void login(final String email, final String password)
    {

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Context context=MainActivity.this;
                            Toast.makeText(context,"Successful Login with "+email+" "+password
                                    ,Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,MainActivity.class);

                          //  intent.putExtra("CurrentUser",currentUserName);
                            startActivity(intent);

                        }
                        else {
                            Context context=MainActivity.this;
                            Toast.makeText(context,"Unsuccessful Login with "+email +" "+password
                                    ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    //TO GET DATA FROM CLOUD
    @Override
    protected  void onStart()
    {
        super.onStart();
        if(firebaseUser!=null){
        conditionReference
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Context context=getBaseContext();
                Toast.makeText(context,"Cloud storage called",Toast.LENGTH_SHORT).show();

                for (DataSnapshot users : dataSnapshot.getChildren()) {
if(users.child("Email").getValue().equals(firebaseUser.getEmail().toString()))
{
   Toast.makeText(MainActivity.this,"NAME IS "+users.child("Firstname").getValue(),Toast.LENGTH_LONG).show();
   currentUserName=users.child("Firstname").getValue().toString();

Intent intent=new Intent(MainActivity.this,Chats.class);
    intent.putExtra("CurrentUser",currentUserName);
    startActivity(intent);

}
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    }
}
