package com.servitly.testMQTT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class OnMessageCallback implements MqttCallback {
    public void connectionLost(Throwable cause) {
        // Reconnect after lost connection.
        System.out.println("Connection lost, and re-connect here.");
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Message handler after receiving message
        System.out.println("Topic:" + topic);
        System.out.println("QoS:" + message.getQos());
        System.out.println("Payload:" + new String(message.getPayload()));
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}