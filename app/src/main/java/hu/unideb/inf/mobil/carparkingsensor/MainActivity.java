package hu.unideb.inf.mobil.carparkingsensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private MediaPlayer mediaPlayer;
    private Sensor proximitySensor;
    private TextView proximityValue;

    private SensorEventListener sel = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                float distance = sensorEvent.values[0];
                proximityValue.setText("Proximity Value: " + distance);
                if (distance < proximitySensor.getMaximumRange()) {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                } else {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        proximityValue = findViewById(R.id.proximityValue);
        mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (proximitySensor != null) {
            sensorManager.registerListener(sel, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sel);
    }
}