package com.example.god.tankimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tankimage.tankImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tankImageView src = (tankImageView)findViewById(R.id.tank);
        List<String> main = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            main.add(i,"第"+i+"条数据");
        }
        src.setLists(main);
    }
}
