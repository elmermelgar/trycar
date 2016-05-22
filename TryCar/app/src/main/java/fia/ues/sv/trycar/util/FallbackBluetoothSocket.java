package fia.ues.sv.trycar.util;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * Created by David-PC on 22/5/2016.
 */
public class FallbackBluetoothSocket extends BluetoothConnector.NativeBluetoothSocket {

    private BluetoothSocket fallbackSocket;

    public FallbackBluetoothSocket(BluetoothSocket tmp) throws BluetoothConnector.FallbackException {
        super(tmp);
        try
        {
            Class<?> clazz = tmp.getRemoteDevice().getClass();
            Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};
            Method m = clazz.getMethod("createRfcommSocket", paramTypes);
            Object[] params = new Object[] {Integer.valueOf(1)};
            fallbackSocket = (BluetoothSocket) m.invoke(tmp.getRemoteDevice(), params);
        }
        catch (Exception e)
        {
            throw new BluetoothConnector.FallbackException(e);
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return fallbackSocket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return fallbackSocket.getOutputStream();
    }


    @Override
    public void connect() throws IOException {
        fallbackSocket.connect();
    }


    @Override
    public void close() throws IOException {
        fallbackSocket.close();
    }
}
