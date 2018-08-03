package com.example.gauth.mad_chatroomapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import static java.security.AccessController.getContext;

public class Chats extends AppCompatActivity {
    public DatabaseReference databaseReference;
    public DatabaseReference conditionReference;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    TextView CurrentUser;
    static String currentUser="";
    static ArrayList<ChatDetails2> chatDetails2=new ArrayList<ChatDetails2>();

    StorageReference storageReference;
    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance("gs://firecast-app-65037.appspot.com");

    static AddChats adapter;
    ListView listView;
EditText chatData;
    static  boolean visibility;
    String path;
    Bitmap bitmap;
    String mydate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        conditionReference=databaseReference.child("TestChatroomChats3");
        mAuth = FirebaseAuth.getInstance();

        CurrentUser=(TextView)findViewById(R.id.currentUser);
        chatData=(EditText)findViewById(R.id.addMessage);
        Intent intent=getIntent();;
        currentUser=intent.getStringExtra("CurrentUser").toString();
        CurrentUser.setText("Logged in as: "+currentUser);

        listView=(ListView)findViewById(R.id.listViewChats);
        adapter=new AddChats(this,R.layout.add_chats,chatDetails2);
        listView.setAdapter(adapter);
         mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    }


    public  void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        MainActivity.firebaseUser=null;
        finish();
    }

    //GET DATA FROM FIREBASE

    @Override
    protected  void onStart()
    {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onStart();
        if(firebaseUser!=null){
        conditionReference
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Context context = getBaseContext();
                            //    Toast.makeText(context, "Cloud storage called", Toast.LENGTH_SHORT).show();

                        chatDetails2.clear();
                        //    chatDetails2.add(new ChatDetails2("DEFAULT","HI HOW ARE YOU", "one hour ago","DEFAULT KEY"));
                            for (DataSnapshot chatObjects : dataSnapshot.getChildren()) {
                                if (chatObjects!= null) {
                                    ChatDetails chats = chatObjects.getValue(ChatDetails.class);
                                    addToArrayList(chats,chatObjects.getKey());


                                //ADDING COMMENTS TO A GIVEN MESSAGE
                                    if(chatObjects.hasChild("Comments")) {
                                        for (DataSnapshot commentObjects : chatObjects.child("Comments").getChildren()) {

//                                            Toast.makeText(getBaseContext(), "Comments present", Toast.LENGTH_SHORT).show();
                                            Comments comments = commentObjects.getValue(Comments.class);
                                                 Log.i("Comments are:!!",comments.Message.toString());
                                                           addToArrayListComments(comments);
                                        }
                                    }
                                }


                                else {
                                    Log.i("Child", "Not available");
                                }
                                adapter.notifyDataSetChanged();
                            }


                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    }


    //ADD MESSAGE TO ARRAY LIST
    public void addToArrayList(ChatDetails chatDetails, String Key)
    {
        chatDetails2.add(new ChatDetails2(chatDetails.Name,chatDetails.Message,chatDetails.Time,
                Key,chatDetails.UserId,chatDetails.ImageId));
        adapter.notifyDataSetChanged();
    }


    //ADD COMMENTS TO A PARTICULAR MESSAGE
    public void addToArrayListComments(Comments comments)
    {
        chatDetails2.add(new ChatDetails2(comments.Name,comments.Message,comments.Time,"",comments.UserId,""));
        adapter.notifyDataSetChanged();
    }
public void addMessage(View view)
{
    Toast.makeText(getBaseContext(),"Current date is  "+mydate,Toast.LENGTH_SHORT).show();
   if(chatData.getText().toString().length()>0){
    Long timeStamp = System.currentTimeMillis()/1000;
    String currentTimeStamp = timeStamp.toString();

    conditionReference.push()
            .setValue(new ChatDetails(currentUser,chatData.getText().toString(),
                    mydate,mAuth.getUid().toString(),""));
    adapter.notifyDataSetChanged();
   }
}



//ADD IMAGEID TO FIREBASE STORAGE

public void addImage(View view)
{
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
        Log.i("Check button","Clicked!!!");
        int permissionCheck=Chats.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
        permissionCheck+=Chats.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
        if(permissionCheck!=0)
        {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else {
            Log.i("No need","To check permission");
            getPhoto();
        }
    }else
        {
            getPhoto();
        }
    String ImageUid="";
    Toast.makeText(this,"SAVE CLICKED",Toast.LENGTH_SHORT).show();

}


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED);
            {
                getPhoto();
            }

        }
    }



    public  void getPhoto()
    {
        Toast.makeText(this,"get photo called!!",Toast.LENGTH_SHORT).show();
        Intent intent_photo=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  intent_photo.setType("image/*");
        startActivityForResult(intent_photo,1);
    }



    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK &&data!=null)
            try {
                Uri selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                image_view.setImageBitmap(bitmap);
Toast.makeText(this,"Photo selected!!",Toast.LENGTH_SHORT).show();
                uploadPhoto();
            } catch (Exception e) {
                Log.i("Some error", "in converting the image!!!");
            }
    }



    public String uploadPhoto()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        //   imageContainer.setDrawingCacheEnabled(false);
        byte[] byteArray = baos.toByteArray();
        final String random = UUID.randomUUID().toString();
        path = "ChatPhotos/" + random + ".png";
        storageReference = firebaseStorage.getReference(path);

        UploadTask uploadTask = storageReference.putBytes(byteArray); //this line
        uploadTask.addOnSuccessListener(Chats.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                StorageReference tempRef = FirebaseStorage.getInstance().getReference().child("ChatPhotos").child(random + ".png");
                Log.i("Address is", tempRef.toString());
                long megabyte = 1024 * 1024;
                tempRef.getBytes(megabyte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                       //THIS CODE IS NOT NEEDED
                  //      BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                     Toast.makeText(Chats.this,"PHOTO UPLOADED TO FIREBASE STORAGE!!",Toast.LENGTH_SHORT).show();

//UPLOADING THE IMAGE TO FIREBASE STORAGE
               Long timeStamp = System.currentTimeMillis()/1000;
               String currentTimeStamp = timeStamp.toString();
               conditionReference.push()
                     .setValue(new ChatDetails(currentUser,"",
                       mydate,mAuth.getUid().toString(),random.toString()));
                        Toast.makeText(Chats.this,"ImageId UPLOADED TO FIREBASE DATABASE!!",Toast.LENGTH_SHORT).show();
                 //       image_view.setAdjustViewBounds(true);
                   //     image_view.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    }
                });
            }
        });
        return random;
    }

}
