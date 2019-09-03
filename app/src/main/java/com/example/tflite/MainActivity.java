package com.example.tflite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    Interpreter tflite;
    Button btn;
    TextView out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        out = (TextView)findViewById(R.id.output);
        btn = (Button)findViewById(R.id.predict);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float prediction = getOutput();
                out.setText(Float.toString(prediction));
            }
        });
        try {
            tflite = new Interpreter(loadModelFile(),null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getOutput(){
        float[] inputVal = {};
        float[] outputVal = new float[1];
        tflite.run(inputVal,outputVal);

        float prediction = outputVal[0];
        Log.e("OUT",""+prediction);
        return prediction;
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("linear.tflite");
        Log.e("FILE","dfsfdsfsd   "+fileDescriptor.getFileDescriptor().toString());
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
