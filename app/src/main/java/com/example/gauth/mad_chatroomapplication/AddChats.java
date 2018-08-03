package com.example.gauth.mad_chatroomapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddChats extends ArrayAdapter<ChatDetails2>
{
    public DatabaseReference databaseReference;
    DatabaseReference conditionReference;
    private FirebaseAuth mAuth;
    long megabyte = 1024 * 1024;
    String mydate;
    public AddChats(@NonNull Context context, int resource, @NonNull ArrayList<ChatDetails2> objects2) {
        super(context, resource, objects2);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final ChatDetails2 chatDetails2=getItem(position);

if(convertView==null) {
    convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_chats, parent, false);
}
        databaseReference= FirebaseDatabase.getInstance().getReference();
        conditionReference=databaseReference.child("TestChatroomChats3");
mAuth=FirebaseAuth.getInstance();

        final TextView message=(TextView)convertView.findViewById(R.id.message);
        TextView name=(TextView)convertView.findViewById(R.id.name);
        final TextView time=(TextView)convertView.findViewById(R.id.time);
        Button deleteComment=(Button) convertView.findViewById(R.id.delete);
        final Button addComment=(Button)convertView.findViewById(R.id.comment);
        final ImageView image_View=(ImageView) convertView.findViewById(R.id.chatImage);


      //DOWNLOAD AND SET IMAGE FROM FIREBASE STORAGE

        /*
if(chatDetails2.ImageId==null ){
    image_View.setVisibility(View.GONE);}
else {
    if (chatDetails2.ImageId.length() > 1) {
        StorageReference tempRef = FirebaseStorage.getInstance().getReference().child("ChatPhotos").child(chatDetails2.ImageId + ".png");
        tempRef.getBytes(megabyte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                image_View.setAdjustViewBounds(true);
                image_View.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });
    }
}
*/


//Different Conditions

        if((chatDetails2.UserId.toString().equals(mAuth.getUid().toString()))&&(chatDetails2.Key.length()>1))
        {
            //CURRENT USER,MESSAGE
            deleteComment.setVisibility(View.VISIBLE);
            addComment.setVisibility(View.VISIBLE);
            message.setText(chatDetails2.Message);
            name.setText(chatDetails2.Name);
            time.setText(chatDetails2.Time);
            image_View.setVisibility(View.VISIBLE);

            if(chatDetails2.ImageId==null)
            {
                image_View.setVisibility(View.GONE);
            }else {
                if (chatDetails2.ImageId.length() > 1) {
                    StorageReference tempRef = FirebaseStorage.getInstance().getReference().child("ChatPhotos").child(chatDetails2.ImageId + ".png");
                    tempRef.getBytes(megabyte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            image_View.setAdjustViewBounds(true);
                            image_View.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        }
                    });
                } else {
                    image_View.setVisibility(View.GONE);
                }
            }
        }

        if((chatDetails2.UserId.toString().equals(mAuth.getUid().toString()))&&(chatDetails2.Key.length()<1))
        {
            //Current user ,NOT A MESSAGE
            deleteComment.setVisibility(View.INVISIBLE);
            addComment.setVisibility(View.INVISIBLE);
            message.setText("                  "+chatDetails2.Message);
            name.setText("                  "+chatDetails2.Name);
            time.setText("                "+chatDetails2.Time);
            image_View.setVisibility(View.GONE);
        }
        if(!(chatDetails2.UserId.toString().equals(mAuth.getUid().toString()))&&(chatDetails2.Key.length()>1))
        {
            //Not a Current User,Message
            deleteComment.setVisibility(View.INVISIBLE);
            addComment.setVisibility(View.VISIBLE);
            message.setText(chatDetails2.Message);
            name.setText(chatDetails2.Name);
            time.setText(chatDetails2.Time);
            image_View.setVisibility(View.VISIBLE);

            if(chatDetails2.ImageId==null){
                image_View.setVisibility(View.GONE);
            }else {
                if (chatDetails2.ImageId.length() > 1) {
                    StorageReference tempRef = FirebaseStorage.getInstance().getReference().child("ChatPhotos").child(chatDetails2.ImageId + ".png");
                    tempRef.getBytes(megabyte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            image_View.setAdjustViewBounds(true);
                            image_View.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        }
                    });
                } else {
                    image_View.setVisibility(View.GONE);
                }
            }
        }

        if(!(chatDetails2.UserId.toString().equals(mAuth.getUid().toString()))&&(chatDetails2.Key.length()<1))
        {
            //Not a Current User, Not a message
            deleteComment.setVisibility(View.INVISIBLE);
        addComment.setVisibility(View.INVISIBLE);
            message.setText("               "+chatDetails2.Message);
            name.setText("               "+chatDetails2.Name);
            time.setText("                "+chatDetails2.Time);
            image_View.setVisibility(View.GONE);
        }


     //DELETE CHATS

        deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context=getContext();
Toast.makeText(context,"Delete clicked on  "+message.getText().toString(),Toast.LENGTH_SHORT).show();
                conditionReference.child(chatDetails2.Key).removeValue();
               Chats.chatDetails2.clear();

            }
        });



//ADD COMMENTS

addComment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        final Context context=getContext();
        Toast.makeText(context,"Add Comment on  "+message.getText().toString(),Toast.LENGTH_SHORT).show();
        final EditText input = new EditText(context);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(input);
        builder.setTitle("Please add comment")
                .setMessage("Do you definately want to add this?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Yes", "Clicked");

                        String comment=input.getText().toString();
                        Toast.makeText(context,comment,Toast.LENGTH_SHORT).show();
                        addComments(comment,chatDetails2);
                    }
                }).setCancelable(true)
                .setNegativeButton("No",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        dialog.dismiss();

                    }
                }).setCancelable(true).
                show();
    }
});
        return  convertView;

    }

    public void addComments(String comments,ChatDetails2 chatDetails2)
    {
        Long timeStamp = System.currentTimeMillis()/1000;
        String currentTimeStamp = timeStamp.toString();
Toast.makeText(getContext(),"Comment by "+Chats.currentUser,Toast.LENGTH_SHORT).show();
        mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        conditionReference.child(chatDetails2.Key).child("Comments")
                .push().setValue(new Comments(Chats.currentUser,comments,mydate,mAuth.getUid().toString()));
                 Chats.chatDetails2.clear();
    }
    }

