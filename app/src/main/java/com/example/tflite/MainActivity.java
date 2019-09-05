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

        try {
            tflite = new Interpreter(loadModelFile(),null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float prediction = getOutput();
                out.setText(Float.toString(prediction));
            }
        });
    }

    private float getOutput(){
        float[] inputVal = {40.72f,390,-0.29885602f,0.47655082f,-1.8878422f,-0.29885602f,0.47655082f,-1.8878422f};
        float[] outputVal = new float[1];
        tflite.run(inputVal,outputVal);

        float prediction = outputVal[0];
        Log.e("OUT",""+prediction);
        return prediction;
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        listAssetFiles("");
        AssetFileDescriptor fileDescriptor = getApplicationContext().getAssets().openFd("linear.tflite");
        Log.e("FILE","dfsfdsfsd   "+fileDescriptor.getFileDescriptor().toString());
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private boolean listAssetFiles(String path) {
        String [] list;
        try {
            list = getAssets().list(path);
            if (list.length > 0) {
                // This is a folder
                for (String file : list) {
                    if (!listAssetFiles(path + "/" + file))
                        return false;
                    else {
                        Log.e("FILE", file);
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
