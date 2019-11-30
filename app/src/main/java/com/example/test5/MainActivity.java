package com.example.test5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar bar = (SeekBar)findViewById(R.id.seekBar);
        final TextView textView = (TextView) findViewById(R.id.textview);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {     //在主线程中接受并处理该消息
                textView.setText(msg.arg1+"");
                bar.setMax(100);
                bar.setProgress(msg.arg1);
            }
        };
        final Runnable myWorker = new Runnable() {
            @Override
            public void run() {
                int progess = 0;
                while (progess<=100){
                    Message msg = new Message();
                    msg.arg1 = progess;
                    handler.sendMessage(msg);  //在线程中每秒产生一个数字，然后通过Hander.sendMessage(Message)将消息发送给主线程,
                                              // 在主线程Handler.handleMessage()中接收并处理该消息
                    progess+=1;
                    try{
                        Thread.sleep(100);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                Message msg = handler.obtainMessage(); //同 new Message();
                msg.arg1 = -1;
                handler.sendMessage(msg);
            }

        };
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread workThread = new Thread(null, myWorker, "WorkThread");

                workThread.start();
            }
        });
    }
}
