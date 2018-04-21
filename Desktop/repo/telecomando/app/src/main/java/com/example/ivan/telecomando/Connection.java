package com.example.ivan.telecomando;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by ivan on 16/04/18.
 */
public class Connection {
    public UUID uuid = UUID.fromString(Utility.UUID);
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket blueSocket = null;
    private BluetoothDevice hc06 = null;
    private OutputStream outStream;
    private boolean connected = false; //identifica lo stato della connessione
    private boolean wasConnected = false; //per rilevare cadute di connessione
    private InputStream inputStream;
    private int lastCommand = Utility.CMD_STOP;

    public Connection() {

    }

    /**
     * Avvia una connessione tra lo smartphone e la robot car tramite socket
     * alla pressione del tasto bluetooth_on
     * @return restituisce una stringa da far visualizzare sulla UI che informa
     * l'utente circa l'esito del comando.
     */
    public String setupConnection() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Controlla se il bluetooth dello smartphone è acceso
        if (!bluetoothAdapter.isEnabled()) {
            return Utility.BLUETOOTH_SPENTO;

        } else {
            hc06 = bluetoothAdapter.getRemoteDevice(Utility.ARDUINO_MAC);
            try {
                blueSocket = hc06.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                return Utility.ERRORE_SOCKET;
            }
            try {
                blueSocket.connect();
                outStream = blueSocket.getOutputStream();
                connected = true;
                wasConnected = true;
                lastCommand = Utility.CMD_STOP;
                return Utility.CONNESSIONE_RIUSCITA;
            } catch (IOException closeException) {
                try {
                    blueSocket.close();
                    connected = false;
                } catch (IOException ioe) {}
                return Utility.CONNESSIONE_NON_RIUSCITA;
            }
        }
    }

    /**
     * Chiude la connessione alla pressione del tasto bluetooth_off
     * @return restituisce una stringa che identifica l'esito dell'operazione.
     */
    public String closeConnection() {
        try {
            outStream.close();
            blueSocket.close();
            outStream = null;
            connected = false;
            wasConnected = false;
            return Utility.DISCONNESSIONE_RIUSCITA;
        } catch (IOException ioe) {
            return Utility.DISCONNESSIONE_NON_RIUSCITA;
        }
    }


    /**
     * Invia un comando di test per vedere se la connessione è caduta oppure no.
     * Qualora fosse caduta, l'applicazione non permette di scrivere su outStream
     * e lancia una IOException, che viene gestita settando a false la variabile
     * connected.
     * Per minimizzare possibili effetti collaterali, il comando di test da inviare
     * è l'ultimo comando che l'app ha inviato alla robot car.
     */
    public void checkConnection() {
        if(outStream != null) {
            try {
                outStream.write(this.lastCommand);
            } catch (IOException ioe) {
                connected = false;
            }
        }
    }

    /**
     * Invia il comando da far eseguire alla robot car
     * @param command comando da far eseguire
     * @return una stringa che identifica l'esito dell'operazione.
     */
    public String sendCommand(int command) {
        if (outStream != null) {
            try {
                outStream.write(command);
                lastCommand = command;
                if(command == Utility.CMD_TURBO) {
                    return Utility.MOD_TURBO;
                }
                if(command == Utility.CMD_NORMALE) {
                    return Utility.MOD_NORMALE;
                }
                if(command == Utility.CMD_LENTO) {
                    return Utility.MOD_TARTARUGA;
                }
                return Utility.COMANDO_INVIATO;
            } catch (IOException ioe) {
                return Utility.ERRORE_INVIO;
            }
        } else {
            return Utility.CONNESSIONE_ASSENTE;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean wasConnected() {
        return this.wasConnected;
    }

    public InputStream getInputStream() {
        try {
            if(this.connected)
                inputStream = blueSocket.getInputStream();
        } catch (IOException ioe) {
        }
        return this.inputStream;
    }

    public void setLastCommand(int _lastCommand) {
        this.lastCommand = _lastCommand;
    }
}
