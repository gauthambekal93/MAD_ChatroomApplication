package com.example.gauth.mad_chatroomapplication;

import android.os.IInterface;

public class ChatDetails {

    String Name;
    String Message;
    String Time;
String UserId;
String ImageId;
    public ChatDetails(String Name, String Message, String Time,String UserId,String ImageId) {
        this.Name = Name;
        this.Message = Message;
        this.Time = Time;
        this.UserId=UserId;
        this.ImageId= ImageId;
    }

    ChatDetails() {

    }
}
    class ChatDetails2 {

        String Name;
        String Message;
        String Time;
        String Key;
        String UserId;
        String ImageId;
        public ChatDetails2(String Name, String Message, String Time, String Key,String UserId,String ImageId) {
            this.Name = Name;
            this.Message = Message;
            this.Time = Time;
            this.Key = Key;
            this.UserId=UserId;
            this.ImageId=ImageId;
        }

        ChatDetails2() {

        }
    }


class Comments {

    String Name;
    String Message;
    String Time;
    String UserId;

    public Comments(String Name, String Message, String Time,String UserId) {
        this.Name = Name;
        this.Message = Message;
        this.Time = Time;
        this.UserId=UserId;
    }

    Comments() {

    }
}