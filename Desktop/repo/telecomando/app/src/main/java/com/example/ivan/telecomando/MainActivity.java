package com.example.ivan.telecomando;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageButton blueButton;
    ImageButton[] buttons;
    ImageView batteryImage;
    TextView textView, connectionText;
    ImageView mod;
    Connection connection;
    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttons = new ImageButton[Utility.BUTTONS];
        batteryImage = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView2);
        mod = findViewById(R.id.mod);
        connectionText = findViewById(R.id.connect);
        batteryImage.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        connection = new Connection();

        initializeButtons();
        blueButtonListener();
        controllerListener();
    }

    /**
     * Carica all'interno dell'array buttons, tutti i bottoni presenti nell'interfaccia
     * grafica.
     */
    public void initializeButtons() {
        buttons[0] = findViewById(R.id.btnUp);
        buttons[1] = findViewById(R.id.btnDown);
        buttons[2] = findViewById(R.id.btnLeft);
        buttons[3] = findViewById(R.id.btnRight);
        buttons[4] = findViewById(R.id.btnStop);
        buttons[5] = findViewById(R.id.btnSquare);
        buttons[6] = findViewById(R.id.btnTriangle);
        buttons[7] = findViewById(R.id.btnCircle);
        blueButton = findViewById(R.id.blueButton);
    }

    /**
     * Resta in ascolto sul pulsante blueButton, se questo viene premuto e non c'è
     * connessione, allora viene inizializzata una nuova connessione, altrimenti
     * chiude la connessione esistente.
     * Gestisce inoltre il cambio di icona del tasto blueButton a seconda dello
     * stato della connessione.
     */
    public void blueButtonListener() {
        final MainActivity mainInstance = this;
        blueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String message;
                if(!connection.isConnected()) {
                    message = connection.setupConnection();
                    if(message.equals(Utility.CONNESSIONE_RIUSCITA)) {
                        blueButton.setImageResource(R.drawable.bluetooth_off);
                        connectionText.setText(R.string.disconnect);
                        mod.setImageResource(R.drawable.car);
                        timerTask = new StreamListener(connection, mainInstance);
                        timer = new Timer(true);
                        timer.scheduleAtFixedRate(timerTask, Utility.DELAY, Utility.TIME_BETWEEN_THREADS_EXCHANGE);
                    }
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    message = connection.closeConnection();
                    timer.cancel();
                    if(message.equals(Utility.DISCONNESSIONE_RIUSCITA)) {
                        blueButton.setImageResource(R.drawable.bluetooth);
                        connectionText.setText(R.string.connect);
                    }
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * I bottoni che costituiscono la pulsantiera sono gestiti tutti allo stesso modo.
     * Se l'utente preme un pulsante, verrà inviato come comando l'indice di tale
     * pulsante all'interno dell'array buttons.
     * 0 - Avanti, 1 - Indietro, 2 - Sinistra, 3 - Destra, 4 - Stop
     * 5 - Modalità turbo, 6 - Modalità normale, 7 - Modalità lenta
     * La variabile i non è visibile all'interno della classe anonima, e di conseguenza
     * è stata dichiarata un'apposita variabile k (final) che ne fa le veci.
     */
    public void controllerListener() {
        for (int i = 0; i < Utility.BUTTONS; i++) {
            final int k = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String message = connection.sendCommand(k);
                    if(message.equals(Utility.MOD_TURBO)) {
                        mod.setImageResource(R.drawable.rocket);
                    }
                    if(message.equals(Utility.MOD_NORMALE)) {
                        mod.setImageResource(R.drawable.car);
                    }
                    if(message.equals(Utility.MOD_TARTARUGA)) {
                        mod.setImageResource(R.drawable.turtle);

                    }
                    if(message.equals(Utility.CONNESSIONE_ASSENTE)) {
                        blueButton.setImageResource(R.drawable.bluetooth);
                        connectionText.setText(R.string.connect);
                    }
                    if(!message.equals(Utility.COMANDO_INVIATO)) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    /**
     * Rende visibile o meno l'icona della batteria scarica e ne cambia il testo
     * a seconda del valore ricevuto.
     * 0 -> La batteria non è scarica
     * 1 -> La batteria è completamente scarica
     * 2 -> E' avvenuta una caduta di connessione, quindi la batteria è quasi scarica.
     * runOnUiThread si assicura che la modifica di variabili dell'interfaccia
     * utente avvenga solo tramite il thread che li ha generati.
     * @param status lo stato della batteria
     */
    public void updateBatteryStatus(int status) {
        switch (status) {
            case Utility.BATTERY_OK :
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        batteryImage.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case Utility.EMPTY_BATTERY_STATUS:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(R.string.low_battery);
                        batteryImage.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }
                });
                break;
            default:
                break;
        }
    }

    public void showConnectionLost() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, Utility.CONNECTION_LOST, Toast.LENGTH_SHORT).show();
                blueButton.setImageResource(R.drawable.bluetooth);
                connectionText.setText(R.string.connect);
                timer.cancel();
            }
        });
    }
}