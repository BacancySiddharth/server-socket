package com.androidsrc.server;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;

public class TcpClientActivty extends Activity {
    Handler tcpHandler;
    private static final int CONNECTED = 1;
    private static final int CONNECT_ERR = 5;
    private static final int MESSAGE_CLOSE = 3;
    private static final int MESSAGE_ECHO = 4;
    private static final int MESSAGE_READ = 2;
    private static final String TAG = "TcpClientActivty";
    private Socket TcpSocket;
    private Calendar cal;
    private Button connectBt, send;
    private boolean connectOk = false;
    private long currentTimeMillis;
    private boolean dateON;
    boolean hexMode;
    boolean markON;
    ListView messageView;
    tcpReadThread mtcpReadThread;
    EditText portEdit;
    int posi;
    String remoteAddress;
    CheckBox repeat;
    int rpeatWait = 0;
    boolean sendClear;
    EditText sendEdit;
    int sendWait = 0;
    boolean sendloop = false;
    String terminate;
    boolean timeON;
    String localAddress;
    boolean localEchoON;
    TextView msgGet;

    StringBuffer sb = new StringBuffer("start");

    public TcpClientActivty() {
        Log.d((String) "TcpClientActivty", "CONSTRUCTOR CALLED");

        tcpHandler = new Handler() {

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            public void handleMessage(Message message) {
                switch (message.what) {
                    default: {
                        return;
                    }
                    case 1: {
                        Log.d((String) "TcpClientActivty", (String) "CONNECTED");
                        try {
                            TcpClientActivty.this.remoteAddress = TcpClientActivty.this.TcpSocket.getRemoteSocketAddress().toString();
                            TcpClientActivty.this.localAddress = TcpClientActivty.this.TcpSocket.getLocalSocketAddress().toString();
                        } catch (NullPointerException var9_2) {
                            var9_2.printStackTrace();
                            return;
                        }
                        TcpClientActivty.this.mtcpReadThread = new tcpReadThread();
                        TcpClientActivty.this.mtcpReadThread.start();
                        TcpClientActivty.this.connectOk = true;
                        TcpClientActivty.this.connectBt.setText((CharSequence) "Disconnet");
                        TcpClientActivty.this.send.setText((CharSequence) "SEND");
                        TcpClientActivty.this.send.setVisibility(View.VISIBLE);

                        TcpClientActivty.this.messegeDisplay("Connect", TcpClientActivty.this.remoteAddress);
                        return;
                    }
                    case 2: {
                        String string2;
                        byte[] arrby = (byte[]) message.obj;
                        string2 = Function.hexToAs(arrby, arrby.length);
                        /*if (TcpClientActivty.this.hexMode) {
                        } else {
                            try {

                                string2 = new String(arrby, 0, arrby.length);
                            } catch (StringIndexOutOfBoundsException var7_6) {
                                var7_6.printStackTrace();
                                string2 = null;
                            }
                        }*/
                        Log.d((String) "TcpClientActivty", (String) ("MESSAGE_READ" + string2));
                        if (string2 == null) return;
                        if (TcpClientActivty.this.markON) {
                            string2 = "<<" + string2;
                        }
                        TcpClientActivty.this.messegeDisplay(string2, TcpClientActivty.this.remoteAddress);
                        return;
                    }
                    case 4: {
                        String string4 = (String) message.obj;
                        if (TcpClientActivty.this.markON) {
                            string4 = ">>" + string4;
                        }
                        TcpClientActivty.this.messegeDisplay(string4, TcpClientActivty.this.remoteAddress);
                        if (!TcpClientActivty.this.sendClear) return;
                        if (TcpClientActivty.this.sendloop) return;
                        TcpClientActivty.this.sendEdit.getEditableText().clear();
                        return;
                    }
                    case 3: {
                        TcpClientActivty.this.disconnect();
                        TcpClientActivty.this.messegeDisplay("Disconnect", TcpClientActivty.this.remoteAddress);
                        return;
                    }
                    case 5: {
                        TcpClientActivty.this.messegeDisplay("Can't Connect", (String) message.obj);
                        return;
                    }
                    case 10:
                }

            }
        };
    }

    private void disconnect() {
        this.sendloop = false;
        this.connectOk = false;
        if (this.TcpSocket != null) {
            try {
                this.TcpSocket.close();
                this.TcpSocket = null;
            } catch (IOException var1_1) {
                var1_1.printStackTrace();
            }
        }
        this.connectBt.setText((CharSequence) "Connect");
        this.send.setVisibility(View.VISIBLE);
    }


    private void messegeDisplay(String string2, String string3) {
        String string4 = Function.replaceCode(string2);
        try {
            Log.e("response", "tcpwrite>>" + Arrays.toString(Function.decodeHexString(string2)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d((String) "TcpClientActivty", string2 + " :: " + string4 + "" + ("count" + this.posi));
        sb.append(string2);
        sb.append(string3);

        msgGet.setText(Function.getHexString(sb.toString().getBytes()));

        if (this.dateON || this.timeON) {
            this.currentTimeMillis = System.currentTimeMillis();
            this.cal = Calendar.getInstance();
            this.cal.setTimeInMillis(this.currentTimeMillis);
        }

    }

    private void tcpWrite(String msg) {
//        String msg = sendEdit.getText().toString();
        byte[] var4_2 = Function.asToHex(msg);
//        byte[] var4_1 = Function.toByteArray(msg);
        try {
            this.TcpSocket.getOutputStream().write(var4_2);
            this.tcpHandler.obtainMessage(4, (Object) msg).sendToTarget();
            return;
        } catch (IOException var6_5) {
            var6_5.printStackTrace();
            return;
        } catch (NullPointerException var5_6) {
            var5_6.printStackTrace();
            return;
        }
    }

    public void connect() {
        String string2 = null;
        try {
            string2 = "192.168.0.1";
            int n = 1001;
            Log.d((String) "TcpClientActivty", (String) ("ipAddress " + string2 + " port " + n));
            this.TcpSocket = new Socket(string2, n);
        } catch (SocketException var4_3) {
            var4_3.printStackTrace();
        } catch (IOException var3_4) {
            var3_4.printStackTrace();
        } catch (NumberFormatException var2_5) {
            var2_5.printStackTrace();
        }
        if (this.TcpSocket != null) {
            if (this.TcpSocket.isConnected()) {
                Log.d((String) "TcpClientActivty", (String) ("ipAddress " + string2 + " port " + this.TcpSocket.isConnected()));

                this.tcpHandler.obtainMessage(1).sendToTarget();
                return;
            }
            this.tcpHandler.obtainMessage(5, (Object) string2).sendToTarget();
            return;
        }
        this.tcpHandler.obtainMessage(5, (Object) string2).sendToTarget();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);
        Log.d((String) "TcpClientActivty", "ON CREATE");

        connectBt = (Button) findViewById(R.id.connect);
        sendEdit = (EditText) findViewById(R.id.msg);
        send = (Button) findViewById(R.id.send);
        msgGet = (TextView) findViewById(R.id.msgGet);

        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TcpClientActivty.this.connectOk) {
                    new Thread() {
                        public void run() {
                            TcpClientActivty.this.connect();
                        }
                    }.start();
                    return;
                }
                TcpClientActivty.this.disconnect();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = sendEdit.getText().toString();

                new Thread() {
                    /*
                     * Enabled aggressive block sorting
                     * Enabled unnecessary exception pruning
                     * Enabled aggressive exception aggregation
                     */
                    public void run() {
                        do {
                            TcpClientActivty.this.tcpWrite(msg);
                            try {
                                Thread.sleep((long) TcpClientActivty.this.sendWait);
                                continue;
                            } catch (InterruptedException var1_1) {
                                var1_1.printStackTrace();
                            }
                        } while (TcpClientActivty.this.sendloop);
                    }
                }.start();
            }
        });


    }

    protected void onResume() {
        super.onResume();
        Log.d((String) "TcpClientActivty", (String) "onResume");
    }

    private class tcpReadThread
            extends Thread {
        private final InputStream tcpInStream;

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public tcpReadThread() {
            InputStream inputStream;
            try {
                InputStream inputStream2;
                inputStream = inputStream2 = TcpClientActivty.this.TcpSocket.getInputStream();
                Log.d((String) "TcpClientActivty", (String) "input " + inputStream2);

            } catch (IOException var2_4) {
                Log.e((String) "TcpClientActivty", (String) "temp sockets not created", (Throwable) var2_4);
                inputStream = null;
            }
            this.tcpInStream = inputStream;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void run() {
            super.run();
            int[] arrn = new int[]{100, 4096};
            byte[][] arrby = (byte[][]) Array.newInstance((Class) Byte.TYPE, (int[]) arrn);
            int n = 0;
            Log.e("TcpClientActivty", "TCP>>" + Function.bytesToHexString(arrby[n]));

            try {
                int n3;
                n3 = this.tcpInStream.read(arrby[n]);
                Log.d((String) "size", n3 + "");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                while (TcpClientActivty.this.connectOk) {
                    int n2 = this.tcpInStream.read(arrby[n]);
                    this.tcpInStream.read(arrby[n]);
//                     Log.e("TAG", "TCP>>" + Function.hexToAs(arrby[n], arrby.length));
                    Log.e("TcpClientActivty", "TCP>>" + Function.bytesToHexString(arrby[n]));
//                    Log.e("TcpClientActivty", "COME >" +  Function.bytesToHex(arrby[n]));
                    TcpClientActivty.this.tcpHandler.obtainMessage(2, n2, -1, (Object) arrby[n]).sendToTarget();

                    if (n >= 99) {
                        n = 0;
                        continue;
                    }
                    n = (byte) (n + 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            /**
             *  this is Completly working Code
             */
            /*while (TcpClientActivty.this.connectOk) {
                int n2;
                try {
                    int n3;
                    n2 = n3 = this.tcpInStream.read(arrby[n]);
                    Log.d((String) "TcpClientActivty.recv", n2 + "");


                } catch (IOException var4_4) {
                    Log.d((String) "TcpClientActivty.recv", (String) var4_4.toString());
                    var4_4.printStackTrace();
                    n2 = 0;
                }
                if (n2 == -1) {
                    TcpClientActivty.this.connectOk = false;
                    TcpClientActivty.this.tcpHandler.obtainMessage(3).sendToTarget();
                    Log.d((String) "TcpClientActivty", "3 send");

                    continue;
                }
                if (n2 <= 0) continue;
//                Log.d((String) "TcpClientActivty", (String) ("Rcv" + arrby[n]) + " : data : " + Arrays.toString(arrby[n]) + "data" + Function.hexToAs(arrby[n], arrby.length));
                Log.d((String) "TcpClientActivty", "data" + Function.hexToAs(arrby[n], arrby.length));
                TcpClientActivty.this.tcpHandler.obtainMessage(2, n2, -1, (Object) arrby[n]).sendToTarget();
                Log.e("TAG", ">>" + Arrays.toString(arrby[n]));

                if (n >= 99) {
                    n = 0;
                    continue;
                }
                n = (byte) (n + 1);
            }
        }*/
        }
    }
}
