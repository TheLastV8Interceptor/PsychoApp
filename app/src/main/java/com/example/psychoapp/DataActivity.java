package com.example.psychoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DataActivity extends AppCompatActivity {

    public TextView HRView = null;
    public TextView GSRView = null;
    public TextView VoltageView = null;
    public TextView CommErrorView = null;
    public TextView StatusView = null;


    public MQTTBackend Backend;

    protected String address_message;
    protected String port_message;
    protected String mqtt_login_message;
    protected String mqtt_password_message;

    protected int ExecutionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.LOGIN_MESSAGE);
        address_message = intent.getStringExtra(LoginActivity.ADDRESS_MESSAGE);
        port_message = intent.getStringExtra(LoginActivity.PORT_MESSAGE);
        mqtt_login_message = intent.getStringExtra(LoginActivity.MQTT_LOGIN_MESSAGE);
        mqtt_password_message = intent.getStringExtra(LoginActivity.MQTT_PASSWORD_MESSAGE);
        Log.i("address_message", address_message);
        Log.i("port_message", port_message);
        Log.i("mqtt_login_message", mqtt_login_message);
        Log.i("mqtt_password_message", mqtt_password_message);

        HRView = (TextView) findViewById(R.id.Data_show_heart_rate_window);
        GSRView = (TextView) findViewById(R.id.Data_show_gsr_window);
        VoltageView = (TextView) findViewById(R.id.Data_show_voltage_window);
        CommErrorView = (TextView) findViewById(R.id.activity_error_comm_TextView);

        HRView.setText("0");
        GSRView.setText("0");
        VoltageView.setText("0");
        StartTask();

    }

    public void StartTask()
    {
        this.Backend = new MQTTBackend(GSRView, HRView, VoltageView, ExecutionCode);
        this.Backend.setMQTTData(address_message, port_message, mqtt_login_message, mqtt_password_message);
        this.Backend.execute();

    }

    public static final String DATA_MESSAGE = "data_message";
    public static final String ERROR_MESSAGE = "error_message";
    public String error_message;

    public void switch_to_decision_or_error(View view)
    {
        launchDecisionOrErrorActivity(view);
    }

    public void launchDecisionOrErrorActivity(View view)
    {
        Intent intent;
        Backend.SetStop(true);
        ExecutionCode = this.Backend.getExecutionCode();
        Log.i("ExecutionCode", String.valueOf(ExecutionCode));
        if(ExecutionCode == 100)
        {
            intent = new Intent(this, DecisionActivity.class);
            String data_message ="data_message";
            intent.putExtra(DATA_MESSAGE, data_message);
            startActivity(intent);
            this.finish();
        }
        else
        {
            if(ExecutionCode == 0)
            {
                error_message = "Brak możliwości połączenia z brokerem";
            }
            else if(ExecutionCode == 5)
            {
                error_message = "Błędna nazwa użytkownika lub hasło";
            }
            else if(ExecutionCode == 1343)
            {
                error_message = "Za niskie napięcie na baterii urządzenia";
            }
            else
            {
                error_message = "Inny błąd komunikacji z brokerem MQTT";
            }

            intent = new Intent(this, ErrorActivity.class);
            intent.putExtra(ERROR_MESSAGE, error_message);
            startActivity(intent);
            this.finish();
        }

    }

}

