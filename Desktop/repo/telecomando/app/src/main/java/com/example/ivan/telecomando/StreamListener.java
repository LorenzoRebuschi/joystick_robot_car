package com.example.ivan.telecomando;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.TimerTask;

/**
 * Created by ivan on 16/04/18.
 */

public class StreamListener extends TimerTask {

    Connection connection;
    MainActivity mainInstance;

    public StreamListener(Connection _connection, MainActivity _mainInstance) {
        this.connection = _connection;
        this.mainInstance = _mainInstance;
    }

    /**
     * Codice che viene eseguito periodicamente.
     */
    @Override
    public void run() {
        try {
            checkOutStream();
            checkInputStream();
            //Mette a "dormire" il thread per un tempo prefissato dopo che ha svolto
            //le sue operazioni.
            Thread.sleep(Utility.TIME_BETWEEN_THREADS_EXCHANGE);
        } catch (InterruptedException ie) {

        }
    }

    /**
     * Controlla lo stato del canale outStream in modo tale da rilevare improvvise
     * cadute di connessione.
     * wasConnected resta a true se la connessione è caduta bruscamente, serve
     * per distinguere il caso "robot car non ancora connessa con l'app" dal caso
     * "caduta di connessione".
     */
    public void checkOutStream() {
       if (connection.wasConnected()) {
           connection.checkConnection();
           if (!connection.isConnected()) {
               connection.setLastCommand(Utility.CMD_STOP);
               mainInstance.showConnectionLost();
           }
       }
    }

    /**
     * Controlla il canale inputStream restando in attesa di un messaggio da
     * arduino che informa che la batteria è completamente scarica.
     */
    public void checkInputStream() {
        InputStream inputStream;
        int command;
        try {
            if (connection.isConnected() && connection.getInputStream() != null) {
                inputStream = connection.getInputStream();
                if (inputStream.available() > 0) {
                    command = inputStream.read();
                    if (command == Utility.CHECK_BATTERY_CMD) {
                        mainInstance.updateBatteryStatus(Utility.EMPTY_BATTERY_STATUS);
                        connection.setLastCommand(Utility.CMD_STOP);
                    }
                }
            }
        } catch (IOException e) {

        }
    }
}
