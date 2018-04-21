package com.example.ivan.telecomando;

import android.media.Image;

/**
 * Created by ivan on 29/03/18.
 */

public class Utility {
    public static final String CONNESSIONE_RIUSCITA = "Connessione con la robot car riuscita.";
    public static final String CONNESSIONE_NON_RIUSCITA = "Connessione con la robot car non riuscita.";
    public static final String ERRORE_SOCKET = "Errore nell'inizializzazione della connessione.";
    public static final String BLUETOOTH_SPENTO = "Assicurati che il bluetooth del tuo smartphone sia acceso.";
    public static final String ARDUINO_MAC = "00:21:13:01:A4:E4";
    public static final String MAC = "78:3B:28:80:63:25";
    public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public static final String ERRORE_INVIO = "Comando non inviato.";
    public static final String DISCONNESSIONE_RIUSCITA = "Disconnessione con la robot car riuscita.";
    public static final String MOD_TURBO = "Modalità TURBO attivata";
    public static final String MOD_NORMALE = "Modalità NORMALE attivata";
    public static final String MOD_TARTARUGA = "Modalità TARTARUGA attivata";
    public static final String CONNESSIONE_ASSENTE = "Non sei connesso alla robot car";
    public static final String DISCONNESSIONE_NON_RIUSCITA = "Non è stato possibile disconnettersi dalla robot car, riprova.";
    public static final String COMANDO_INVIATO = "Comando inviato correttamente";
    public static final String CONNECTION_LOST = "Connessione persa, assicurati che la batteria sia carica";
    public static final int BUTTONS = 8;
    public static final int CHECK_BATTERY_CMD = 8;
    public static final int CMD_TURBO = 5;
    public static final int CMD_NORMALE = 6;
    public static final int CMD_LENTO = 7;
    public static final int TIME_BETWEEN_THREADS_EXCHANGE = 500;
    public static final int EMPTY_BATTERY_STATUS = 1;
    public static final int DELAY = 0;
    public static final int BATTERY_OK = 0;
    public static final int CMD_STOP = 4;
}
