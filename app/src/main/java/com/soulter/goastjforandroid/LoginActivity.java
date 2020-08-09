package com.soulter.goastjforandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {


    static String INTENT_EXTRA = "login_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        final EditText ipEditText = findViewById(R.id.userip);
        final EditText portEditText = findViewById(R.id.userport);
        final EditText passwordEditText = findViewById(R.id.password);

        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final TextView loginStatus = findViewById(R.id.login_status);
        loadingProgressBar.setVisibility(View.GONE);

        ipEditText.setText("39.100.5.139");
        portEditText.setText("1034");
        passwordEditText.setText("abbccc");

        String loginStatusStr = getIntent().getStringExtra(INTENT_EXTRA);
        if (loginStatusStr != null){
            loginStatus.setText(loginStatusStr);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                final String ip = ipEditText.getText().toString();
                final String port = portEditText.getText().toString();
                final String psw = passwordEditText.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.v("TAG", "link start" + ip + port + psw);

                            SocketManager.socket = new Socket(ip, Integer.parseInt(port));
                            SocketManager.inputStreamReader = new InputStreamReader(SocketManager.socket.getInputStream(), "GBK");
                            SocketManager.bufferedWriter = new BufferedWriter(new OutputStreamWriter(SocketManager.socket.getOutputStream(), "GBK"));

                            Log.v("TAG", "link success" + ip + port + psw);

                            loginStatus.post(new Runnable() {
                                @Override
                                public void run() {
                                    loginStatus.setText("连接成功");
                                }
                            });
                            //发送密码
                            SocketManager.bufferedWriter.write("#pw "+psw);
                            SocketManager.bufferedWriter.newLine();
                            SocketManager.bufferedWriter.flush();
                            Thread.sleep(1000);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
//
//

//
//                            int c= MainActivity.inputStreamReader.read();
//                            Log.v("TAG",String.valueOf((char)c));
//                            if ((char)c == '!'){
//                                StringBuffer cmds=new StringBuffer("!");
//                                while(true){
//                                    int c0=MainActivity.inputStreamReader.read();
//                                    if((char)c0=='!') {
//                                        cmds.append("!");
//                                        break;
//                                    }
//                                    if ((char)c0=='\n') {
//                                        cmds.append("\n");
//                                        break;
//                                    }
//                                    cmds.append((char)c0);
//
//                                }
//                                String cmd[]=cmds.toString().substring(0,cmds.length()-1).split(" ");
//
//                                if (cmd[0] )
//                            }


                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                }).start();

            }
        });
    }
}