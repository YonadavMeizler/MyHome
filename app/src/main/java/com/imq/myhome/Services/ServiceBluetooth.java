package com.imq.myhome.Services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class ServiceBluetooth {
    // Debugging
    private static final String TAG = "R2Y2";

    private static final String NAME_SECURE = "R2Y2 SECURE";
    private static final String NAME_INSECURE = "R2Y2 INSECURE";

    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothAdapter mAdapter;

    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private BluetoothDevice mDevice;
    private UUID deviceUUID;
    private final Handler mHandler;


    public ServiceBluetooth(Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mHandler = handler;
        start();
    }

    public synchronized void start() {
        Logging("Start: started");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device) {
        Logging("StartClient: started");
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }

    private void connected(BluetoothSocket socket, BluetoothDevice device) {
        Logging("connected: started");
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
    }

    public void write(byte[] out) {
        Logging("write: started");
        if (mConnectedThread != null) {
            mConnectedThread.write(out);
        }
    }

    public void Logging(String text) {

        Log.d(TAG, text);
        // Create a message in child thread.
        Message childThreadMessage = new Message();
        childThreadMessage.what = 1;
        childThreadMessage.obj = text;
        // Put the message in main thread message queue.
        mHandler.sendMessage(childThreadMessage);
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mServerSocket;

        AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_INSECURE, MY_UUID_INSECURE);
                Logging("AcceptThread: setting up server using " + MY_UUID_INSECURE);

            } catch (IOException e) {
                Logging("AcceptThread: IOException: " + e.getMessage());
            }
            mServerSocket = tmp;
        }

        public void run() {
            Logging("AcceptThread: run: AcceptThread Running");
            BluetoothSocket socket = null;
            try {
                Logging("AcceptThread: run: RFCOM server socket start.....");
                socket = mServerSocket.accept();
            } catch (IOException e) {
                Logging("AcceptThread: run: IOException: " + e.getMessage());
            }
            if (socket != null) {
                connected(socket, mDevice);
            }
            Logging("AcceptThread: run: End AcceptThread");
        }

        public void cancel() {
            Logging("AcceptThread: cancel: Cancelling AcceptThread");
            try {
                mServerSocket.close();
            } catch (IOException e) {
                Logging("AcceptThread: cancel: IOException: " + e.getMessage());
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        ConnectThread(BluetoothDevice device) {
            Logging("ConnectThread: started to device: " + device.getName());
            mDevice = device;
        }

        public void run() {
            Logging("ConnectThread: run: started");

            mAdapter.cancelDiscovery();
            BluetoothSocket tmp = null;
            try {

                Logging("ConnectThread: ConnectThread: Tring to create an RF insecure socket using UUID: " + MY_UUID_INSECURE);
                tmp = mDevice.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);

            } catch (IOException e) {
                Logging("ConnectThread: ConnectThread: IOException: " + e.getMessage());
            }

            mmSocket = tmp;
            try {
                mmSocket.connect();
                Logging("ConnectThread: run: Connected");
            } catch (IOException e) {
                try {
                    Logging("ConnectThread: run: Closing client socket");
                    mmSocket.close();
                } catch (IOException e1) {
                    Logging("ConnectThread: run: Unable to close the connection of socket - IOException: " + e1.getMessage());
                }
                Logging("ConnectThread: run: Unable to connect to device - IOException: " + e.getMessage());

            }

            connected(mmSocket, mDevice);
        }

        void cancel() {
            try {
                Logging("ConnectThread: run: Closing client socket");
                mmSocket.close();
            } catch (IOException e1) {
                Logging("ConnectThread: run: Unable to close the connection of socket - IOException: " + e1.getMessage());
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mSocket;
        private final InputStream mInStream;
        private final OutputStream mOutStream;

        ConnectedThread(BluetoothSocket socket) {
            Logging("ConnectedThread: started");
            mSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpout = null;

            try {
                tmpIn = mSocket.getInputStream();
                tmpout = mSocket.getOutputStream();
            } catch (IOException e) {
                Logging("ConnectedThread: Unable to get stream - IOException: " + e.getMessage());
            }
            mInStream = tmpIn;
            mOutStream = tmpout;
        }

        public void run() {

            Logging("ConnectedThread: run: started");
            byte[] buffer = new byte[1024];

            int bytes;
            while (true) {
                try {
                    bytes = mInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Logging("ConnectedThread: input stream: " + incomingMessage);
                } catch (IOException e) {
                    Logging("ConnectedThread: error reading input " + e.getMessage());
                    break;
                }
            }
        }

        void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Logging("ConnectedThread: write: writing to output stream: " + text);
            try {
                mOutStream.write(bytes);
            } catch (IOException e) {
                Logging("ConnectedThread: error writing output" + e.getMessage());
            }
        }

        public void cancel() {
            try {
                Logging("ConnectedThread: run: Closing client socket");
                mSocket.close();
            } catch (IOException e1) {
                Logging("ConnectedThread: run: Unable to close the connection of socket - IOException: " + e1.getMessage());
            }
        }
    }
}
