package application.xxmp.chattiesierre;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import co.devcenter.androiduilibrary.ChatView;
import co.devcenter.androiduilibrary.ChatViewEventListener;
import co.devcenter.androiduilibrary.SendButton;


public class ChatActivity extends AppCompatActivity {
    private static final String TAG ="ChatActivity";
    static boolean active = false;
    private String contactJid;
    private ChatView mChatView;
    private SendButton mSendButton;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mChatView =(ChatView) findViewById(R.id.rooster_chat_view);

        for (int i=0;i<LoginActivity.q.size();i++)
        {
            String fromstore=LoginActivity.q.get(i).getFrom();
            String Jidd="";
            if (fromstore.contains("/")){
                Jidd=fromstore.split("/")[0];
            }
            else Jidd=fromstore;
            if (fromstore.equals(Jidd)){
                mChatView.receiveMessage(LoginActivity.q.get(i).getBody());
                LoginActivity.q.remove(i);
            }
        }
        mChatView.sendMessage();

        mChatView.setEventListener(new ChatViewEventListener() {
            @Override
            public void userIsTyping() {
                Toast.makeText(getApplicationContext(),"Typing",Toast.LENGTH_SHORT);
            }

            @Override
            public void userHasStoppedTyping() {
                //Here you know that the user has stopped typing.
            }
        });





        mSendButton = mChatView.getSendButton();
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Only send the message if the client is connected
                //to the server.

                if (ConnectionService.getState().equals(Connection.ConnectionState.CONNECTED)) {
                    Log.d(TAG, "The client is connected to the server,Sendint Message");
                    //Send the message to the server

                    Intent intent = new Intent(ConnectionService.SEND_MESSAGE);
                    intent.putExtra(ConnectionService.BUNDLE_MESSAGE_BODY,
                            mChatView.getTypedString());
                    intent.putExtra(ConnectionService.BUNDLE_TO, contactJid);

                    sendBroadcast(intent);

                    //Update the chat view.
                    mChatView.sendMessage();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Client not connected to server ,Message not sent!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Intent intent = getIntent();
        contactJid = intent.getStringExtra("EXTRA_CONTACT_JID");
        setTitle(contactJid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        active=false;
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        active=true;
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action)
                {
                    case ConnectionService.NEW_MESSAGE:
                        String from = intent.getStringExtra(ConnectionService.BUNDLE_FROM_JID);
                        String body = intent.getStringExtra(ConnectionService.BUNDLE_MESSAGE_BODY);

                        if ( from.equals(contactJid))
                        {
                            mChatView.receiveMessage(body);

                        }else
                        {
                            Log.d(TAG,"Got a message from jid :"+from);
                        }

                        return;

                    case ConnectionService.READ_MESSAGES_FROM_STORE:

                }

            }
        };

        IntentFilter filter = new IntentFilter(ConnectionService.NEW_MESSAGE);
        registerReceiver(mBroadcastReceiver,filter);


    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }
}

;