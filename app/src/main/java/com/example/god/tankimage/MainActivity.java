package com.example.god.tankimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tankimage.tankImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private tankImageView src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        src = (tankImageView)findViewById(R.id.tank);
        List<String> main = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            main.add(i,"第"+i+"条数据");
        }
        src.setLists(main);
        src.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "点我？", Toast.LENGTH_SHORT).show();
    }

    public void ADDSPeed(View view) {
        src.setmSpeed(30);
    }
}
