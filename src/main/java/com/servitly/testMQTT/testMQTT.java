package com.servitly.testMQTT;

import org.apache.commons.lang3.RandomUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
public class testMQTT {
    public static void main(String[] args) {
        int qos = 2;
        String topic = "DVES_703B0E/123456789/measures";
        String broker = "ssl://mqtt.servitly-sandbox.com:8883";
        String clientId = "12345689_CLIENT";
        String username = "DVES_703B0E";
        String password = "Test1234";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            ObjectNode payload = JsonNodeFactory.instance.objectNode();
            payload.put("ts", System.currentTimeMillis());
            ObjectNode data = JsonNodeFactory.instance.objectNode();
            data.put("measure1", 42 + RandomUtils.nextInt(0, 100));
            data.put("measure2", 24 + RandomUtils.nextInt(0, 100));
            payload.set("data", data);
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            sampleClient.setCallback(new OnMessageCallback());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing to topic: " + topic);
            System.out.println("Publishing message: " + payload.toString());
            MqttMessage message = new MqttMessage(payload.toString().getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            sampleClient.getDebug().dumpClientDebug();
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}