package com.example.psychoapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;

public class MQTTBackend extends AsyncTask<Void, Void, Void>
{
    protected WeakReference<TextView> GSRValueTextViewReference;
    protected WeakReference<TextView> HRValueTextViewReference;
    protected WeakReference<TextView> VoltageValueTextViewReference;

    String broker; 
    String clientID = "AndroidClientID";
    String userName;
    String passwd;
    String startMessageString ="start";
    String stopMessageString ="stop";

    boolean GSRWrite = false;
    boolean HRWrite = false;
    boolean VoltageWrite = false;
    boolean stop = false;

    protected int ExecutionCode = 100; //kod 100 oznacza prawidlowe wykonanie programu

    //char[] userNameChar;
    char[] passwdChar;
    MemoryPersistence Persistance = new MemoryPersistence();

    public MqttClient MainClient;
    public callbacks Callbacks;

    public void SetStop(boolean logic)
    {
        Log.i("stop", "stop");
        stop = logic;
    }

    public int getExecutionCode()
    {
        return ExecutionCode;
    }

    public double getVoltageFromCallBack(String v)
    {
        double V = Double.parseDouble(v);
        return V;
    }

    public MQTTBackend(TextView gsrValueTextView, TextView hrValueTextView, TextView voltageValueTextView, int executionCode) {
        GSRValueTextViewReference = new WeakReference<>(gsrValueTextView);
        HRValueTextViewReference = new WeakReference<>(hrValueTextView);
        VoltageValueTextViewReference = new WeakReference<>(voltageValueTextView);
    }

    public void setMQTTData(String address, String port, String login, String password)
    {

        passwd = password;
        Log.i("password", passwd);
        userName = login;
        Log.i("userName", userName);
        broker = "tcp://"+address+":"+port;
        System.out.println(broker);
        Log.i("Broker_address", broker);
        passwdChar = passwd.toCharArray();

    }

    protected Void doInBackground(Void... voids)
    {
        try {

            MainClient = new MqttClient(broker, clientID, Persistance);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            Callbacks = new callbacks();

            connectOptions.setCleanSession(true);
            connectOptions.setUserName(userName);
            connectOptions.setPassword(passwdChar);
            connectOptions.setAutomaticReconnect(true);
            this.MainClient.setCallback(Callbacks);
            this.MainClient.connect(connectOptions);

            MqttMessage startMessage = new MqttMessage(startMessageString.getBytes());
            MqttMessage stopMessage = new MqttMessage((stopMessageString.getBytes()));
            MainClient.publish("inTopic", startMessage);

            while (true)
            {
                MainClient.subscribe("HRTopic");
                HRValueTextViewReference.get().setText((Callbacks.HR));

                MainClient.subscribe("GSRTopic");
                GSRValueTextViewReference.get().setText((Callbacks.GSR));

                MainClient.subscribe("VoltageTopic");
                VoltageValueTextViewReference.get().setText((Callbacks.Voltage));

                double V = Double.parseDouble(Callbacks.Voltage);

                if(V < 7.00)
                {
                    ExecutionCode = 1343;
                }

                if(Callbacks.getBooleanConnectionLost())
                {
                    MainClient.connect(connectOptions);
                    MainClient.subscribe("HRTopic");
                    MainClient.subscribe("GSRTopic");
                    MainClient.subscribe("VoltageTopic");
                    MainClient.subscribe("inTopic");
                    Callbacks.setBooleanConnectionLost(false);
                }

                if(stop == true)
                {
                    Log.i("Stopped", "stopped");
                    MainClient.publish("inTopic", stopMessage);
                    MainClient.disconnect();
                    break;
                }
            }

        } catch (MqttException me) {

            System.out.println("String: "+me.getReasonCode());
            System.out.println("String: "+me.toString());

            if(me.getReasonCode() == 5)
            {
                ExecutionCode = 5;
            }
            else if(me.getReasonCode() == 0)
            {
                ExecutionCode = 0;
            }
            return null;

        }

        return null;
    }

}

class callbacks implements MqttCallback
{
    byte[] GSRPayload;
    byte[] VoltagePayload;
    byte[] HRPayload;
    byte[] inTopicPayload;

    String GSRPayloadString;
    String HRPayloadString;
    String VoltagePayloadString;
    String inTopicPayloadString;

    String GSRPayloadStringReplaced;
    String HRPayloadStringReplaced;
    String VoltagePayloadStringReplaced;
    String inTopicPayloadStringReplaced;

    String[] HRPayloadsplitted;
    String[] GSRPayloadsplitted;
    String[] VoltagePayloadsplitted;
    String[] inTopicPayloadsplitted;

    String Voltage = "9.00";
    String GSR ="0";
    String HR = "0";

    boolean connectionLost = false;
    boolean endMeasure = false;

    public boolean getBooleanConnectionLost()
    {
        return connectionLost;
    }

    public void setBooleanConnectionLost(boolean logic_value)
    {
        connectionLost = logic_value;
    }

    @Override
    public void connectionLost( Throwable Exception)
    {
        connectionLost = true;
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception
    {
        if(s.equals("HRTopic"))
        {
            HRPayload = mqttMessage.getPayload();
            HRPayloadString = new String(HRPayload, StandardCharsets.UTF_8);
            HRPayloadStringReplaced = HRPayloadString.replace("'", ";");
            HRPayloadsplitted = HRPayloadStringReplaced.split(";");
            HR = HRPayloadsplitted[2];

        }
        else if (s.equals("GSRTopic"))
        {
            GSRPayload = mqttMessage.getPayload();
            GSRPayloadString = new String(GSRPayload, StandardCharsets.UTF_8);
            GSRPayloadStringReplaced = GSRPayloadString.replace("'", ";");
            GSRPayloadsplitted = GSRPayloadStringReplaced.split(";");
            GSR = GSRPayloadsplitted[2];
            System.out.println(GSR);
        }
        else if(s.equals("VoltageTopic"))
        {
            VoltagePayload = mqttMessage.getPayload();
            VoltagePayloadString = new String(VoltagePayload, StandardCharsets.UTF_8);
            VoltagePayloadStringReplaced = VoltagePayloadString.replace("'", ";");
            VoltagePayloadsplitted = VoltagePayloadStringReplaced.split(";");
            Voltage = VoltagePayloadsplitted[2];


        }
        else if (s.equals("inTopic"))
        {
            inTopicPayload = mqttMessage.getPayload();
            inTopicPayloadString= new String(inTopicPayload, StandardCharsets.UTF_8);
            inTopicPayloadStringReplaced=inTopicPayloadString.replace("'", ";");
            inTopicPayloadsplitted = inTopicPayloadStringReplaced.split("");

            if(inTopicPayloadsplitted[2] == "stop")
            {
                endMeasure = true;
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
    {
        System.out.println("Message Delivered");
    }
}
