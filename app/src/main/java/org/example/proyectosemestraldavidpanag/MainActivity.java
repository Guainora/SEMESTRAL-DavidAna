package org.example.proyectosemestraldavidpanag;



import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements  SensorEventListener{
// Cambio de control 1
    TextView txtx,txty,txtz;
    Sensor proximidad, lacelerometro;
    ImageView img;
    SensorManager sensorManager;
    long lastUpdate = 0;
    float last_x, last_y, last_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //buscando los txt view

        txtx=findViewById(R.id.ejeX);
        txty=findViewById(R.id.ejeY);
        txtz=findViewById(R.id.ejeZ);
        img= findViewById(R.id.img);


       //declaracion de los sensores
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        proximidad=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        lacelerometro=sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, proximidad, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lacelerometro, SensorManager.SENSOR_DELAY_NORMAL);





    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        switch (sensorEvent.sensor.getType()){
            case Sensor.TYPE_PROXIMITY:
                if(sensorEvent.values[0]<proximidad.getMaximumRange()){
                    img.setImageResource(R.drawable.acelerometro);

                }else
                {
                    img.setImageResource(R.drawable.acelerometro2);
                }
                break;

            case Sensor.TYPE_LINEAR_ACCELERATION:
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdate) > 100) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
                    last_x = x;
                    last_y = y;
                    last_z = z;

                    if(last_x >1){
                        sonido();


                    }

                    if(last_y >1){
                        getWindow().getDecorView().setBackgroundColor(Color.BLUE);

                    }else
                    {
                        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

                    }





                    txtx.setText(""+last_x+" m/s2");
                    txty.setText(""+last_y+" m/s2");
                    txtz.setText(""+last_z+" m/s2");

                }




                break;



            default:
            //np tenemos default
                break;

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, proximidad, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void sonido()
    {
        MediaPlayer mediaPlayer= MediaPlayer.create(this,R.raw.song);
        mediaPlayer.start();

    }
}
// nueva revisión