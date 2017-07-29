package com.example.god.tankimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.colorutils.GodUtils;
import com.example.tankimage.tankImageView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Switch.OnCheckedChangeListener {

    private tankImageView src;
    private MaterialEditText colorText;

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

        SeekBar speedSeekBar = (SeekBar)findViewById(R.id.speed_seekbar);
        speedSeekBar.setMax(5);
        speedSeekBar.setProgress(3);
        speedSeekBar.setOnSeekBarChangeListener(this);

        Switch direction =(Switch)findViewById(R.id.direction);
        direction.setOnCheckedChangeListener(this);

        colorText = (MaterialEditText)findViewById(R.id.color_edit);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "点我？", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.speed_seekbar:
                src.setmSpeed(i + 1);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(Switch view, boolean checked) {
        if (src.getmDirection()==0) {
            src.setmDirection(1);
        }else {
            src.setmDirection(0);
        }
    }


    public void change(View view) {
        String color = colorText.getText().toString();
        if (GodUtils.judgeColorString(color)) {
            src.setmTextColor(GodUtils.StringTransInt("FF" + color.substring(2)));
            //已知问题：输入颜色的透明度，也就是前两位为00，会导致ImageView黑屏
        }else {
            Toast.makeText(this, "请输入ARGB格式的颜色值", Toast.LENGTH_SHORT).show();
        }
    }
}
