package com.androidsrc.server;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    Server server;
    TextView infoip, msg;

    EditText edtTxt;
    Button sendBtn;

    TcpClient mTcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoip = (TextView) findViewById(R.id.infoip);
        msg = (TextView) findViewById(R.id.msg);
        server = new Server(this);
        infoip.setText(server.getIpAddress() + ":" + server.getPort());

        edtTxt = (EditText) findViewById(R.id.sendEdt);
        sendBtn = (Button) findViewById(R.id.send);
        new ConnectTask().execute();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg("f0160000000000f7");
            }
        });

    }

    public void sendMsg(String msg) {
//        if (this.server == null || !this.server.getServerSocket().isBound())

        if(mTcpClient != null){
            mTcpClient.sendMessage("f0160000000000f7");
        }else return;


//		server.getServerSocket().

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.onDestroy();
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                    Log.e("message recevied", "::" + message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("start Socket","start");
        }


    }

}