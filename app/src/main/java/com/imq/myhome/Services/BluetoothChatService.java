package com.imq.myhome.Services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothChatService {
    // Debugging
    private static final String TAG = "R2Y2";

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "R2Y2 Secure";
    private static final String NAME_INSECURE = "R2Y2 Insecure";

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    // Member fields
    private final BluetoothAdapter mAdapter;
    private AcceptThread mSecureAcceptThread;
    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private BluetoothDevice mDevice;
    private BluetoothSocket mSocket;
    private String mSocketType;
    private boolean mSocketTypeBoolean;
    private BluetoothServerSocket mServerSocket;

    public BluetoothChatService(BluetoothDevice device, boolean secure) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mDevice = device;
        mSocketTypeBoolean = secure;
        mSocketType = secure ? "Secure" : "Insecure";
        start();
    }

    private synchronized void start() {
        Log.d(TAG, "start");

        // Start the thread to listen on a BluetoothServerSocket
        if (mSecureAcceptThread == null) {
            mSecureAcceptThread = new AcceptThread();
            mSecureAcceptThread.start();
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    public synchronized void connect() {
        Log.d(TAG, "connect to: " + mDevice);

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread();
        mConnectThread.start();
    }

    private synchronized void connected() {
        Log.d(TAG, "connected, Socket Type:" + mSocketType);

        // Cancel the accept thread because we only want to connect to one device
        if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }
        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread();
        mConnectedThread.start();
    }


    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }

        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }
    }

    public void write(byte[] out) {
        if (mConnectedThread != null) {
            if (out != null) {
                mConnectedThread.write(out);
            } else {
                Log.e(TAG, "out is null");
            }
        } else {
            Log.e(TAG, "mConnectedThread is null");
        }
    }

    private void connectionFailed() {
        // Start the service over to restart listening mode
        BluetoothChatService.this.start();
    }


    private void connectionLost() {
        // Start the service over to restart listening mode
        BluetoothChatService.this.start();
    }

    private class AcceptThread extends Thread {
        // The local server socket


        AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                if (mSocketTypeBoolean) {
                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
                } else {
                    tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME_INSECURE, MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + " listen() failed", e);
            }
            mServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "Socket Type: " + mSocketType + " BEGIN mAcceptThread" + this);
            setName("AcceptThread" + mSocketType);
            BluetoothSocket socket = null;
            try {
                socket = mServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + " accept() failed", e);
            }

            // If a connection was accepted
            if (socket != null) {
                synchronized (BluetoothChatService.this) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Could not close unwanted socket", e);
                    }

                }
            }
            Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);
        }

        void cancel() {
            Log.d(TAG, "Socket Type" + mSocketType + " cancel " + this);
            try {
                mServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type" + mSocketType + " close() of server failed", e);
            }
        }
    }


    private class ConnectThread extends Thread {

        ConnectThread() {
            BluetoothSocket tmp = null;

            try {
                if (mSocketTypeBoolean) {
                    tmp = mDevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                } else {
                    tmp = mDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + " create() failed", e);
            }
            mSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread SocketType: " + mSocketType);

            mAdapter.cancelDiscovery();
            try {
                mSocket.connect();
            } catch (IOException e) {

                try {
                    mSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + mSocketType + " socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            connected();
        }

        void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }


    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        ConnectedThread() {
            Log.d(TAG, "create ConnectedThread: " + mSocketType);

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = mSocket.getInputStream();
                tmpOut = mSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);

            } catch (IOException e) {
                Log.e(TAG, "disconnected", e);
                connectionLost();

            }

        }


        void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}