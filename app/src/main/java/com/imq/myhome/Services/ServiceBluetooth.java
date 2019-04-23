package com.imq.myhome.Services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class ServiceBluetooth {

    private static final String TAG = "R2Y2";

    private static final String NAME_SECURE = "R2Y2secure";
    private static final String NAME_INSECURE = "R2Y2";

    private static final UUID MY_UUID_INSECURE = UUID.fromString("70e5fd65-b637-40bc-9615-e33b2bfc9a3d");

    private final BluetoothAdapter mAdapter;

    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private BluetoothDevice mDevice;
    private UUID deviceUUID;


    public ServiceBluetooth(Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public synchronized void start() {
        Log.d(TAG, "Start: started");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "StartClient: started");
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    private void connected(BluetoothSocket socket, BluetoothDevice device) {
        Log.d(TAG, "connected: started");
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
    }

    public void write(byte[] out) {
        Log.d(TAG, "write: started");
        mConnectedThread.write(out);
    }


    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mServerSocket;

        AcceptThread() {

            BluetoothServerSocket tmp = null;
            try {
                tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME_INSECURE, MY_UUID_INSECURE);
                Log.d(TAG, "AcceptThread: setting up server using " + MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.d(TAG, "AcceptThread: IOException: " + e.getMessage());
            }
            mServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "AcceptThread: run: AcceptThread Running");

            BluetoothSocket socket = null;


            try {
                Log.d(TAG, "AcceptThread: run: RFCOM server socket start.....");
                socket = mServerSocket.accept();
            } catch (IOException e) {
                Log.d(TAG, "AcceptThread: run: IOException: " + e.getMessage());
            }

            if (socket != null) {
                connected(socket, mDevice);
            }
            Log.d(TAG, "AcceptThread: run: End AcceptThread");
        }

        public void cancel() {
            Log.d(TAG, "AcceptThread: cancel: Cancelling AcceptThread");
            try {
                mServerSocket.close();
            } catch (IOException e) {
                Log.d(TAG, "AcceptThread: cancel: IOException: " + e.getMessage());
            }
        }
    }


    private class ConnectThread extends Thread {
        private BluetoothSocket mSocket;

        ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started");
            mDevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            BluetoothSocket tmp = null;
            Log.d(TAG, "ConnectThread: run: started");

            try {
                Log.d(TAG, "ConnectThread: run: Tring to create an RF insecure socket using UUID");
                tmp = mDevice.createInsecureRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.d(TAG, "ConnectThread: run: IOException: " + e.getMessage());
            }

            mSocket = tmp;

            mAdapter.cancelDiscovery();

            try {
                mSocket.connect();
                Log.d(TAG, "ConnectThread: run: Connected");
            } catch (IOException e) {
                try {
                    Log.d(TAG, "ConnectThread: run: Closing client socket");
                    mSocket.close();
                } catch (IOException e1) {
                    Log.d(TAG, "ConnectThread: run: Unable to close the connection of socket - IOException: " + e1.getMessage());
                }
                Log.d(TAG, "ConnectThread: run: Unable to connect to device - IOException: " + e.getMessage());
            }

            connected(mSocket, mDevice);
        }

        void cancel() {
            try {
                Log.d(TAG, "ConnectThread: run: Closing client socket");
                mSocket.close();
            } catch (IOException e1) {
                Log.d(TAG, "ConnectThread: run: Unable to close the connection of socket - IOException: " + e1.getMessage());
            }
        }
    }



    private class ConnectedThread extends Thread {
        private final BluetoothSocket mSocket;
        private final InputStream mInStream;
        private final OutputStream mOutStream;

        ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: started");
            mSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpout = null;

            try {
                tmpIn = mSocket.getInputStream();
                tmpout = mSocket.getOutputStream();
            } catch (IOException e) {
                Log.d(TAG, "ConnectedThread: Unable to get stream - IOException: " + e.getMessage());
            }
            mInStream = tmpIn;
            mOutStream = tmpout;
        }

        public void run() {

            Log.d(TAG, "ConnectedThread: run: started");
            byte[] buffer = new byte[1024];

            int bytes;
            while (true) {
                try {
                    bytes = mInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "ConnectedThread: input stream: " + incomingMessage);
                } catch (IOException e) {
                    Log.d(TAG, "ConnectedThread: error reading input " + e.getMessage());
                    break;
                }
            }
        }

        void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "ConnectedThread: write: writing to output stream: " + text);
            try {
                mOutStream.write(bytes);
            } catch (IOException e) {
                Log.d(TAG, "ConnectedThread: error writing output" + e.getMessage());
            }
        }

        public void cancel() {
            try {
                Log.d(TAG, "ConnectedThread: run: Closing client socket");
                mSocket.close();
            } catch (IOException e1) {
                Log.d(TAG, "ConnectedThread: run: Unable to close the connection of socket - IOException: " + e1.getMessage());
            }
        }
    }
}
