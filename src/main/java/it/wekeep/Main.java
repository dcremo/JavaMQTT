package it.wekeep;

import org.apache.commons.lang3.RandomUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
public class Main {


    public static final String kSONOFF_PROD = "SONOFF cloud";
    public static final String kSONOFF_PROD_TLS = "kSONOFF_PROD_TLS";
    public static final String kGATEWAY_PROD = "GATEWAY cloud";
    public static final String kSEMIOTLY = "SEMIOTLY sandbox";

    public static void main(String[] args) {
        int qos = 2;
        String topic = "v1/devices/me/telemetry";
        //10:55:42 MQT: v1/devices/me/telemetry/STATE = {"Time":"2020-01-06T10:55:42","Uptime":"0T00:01:27","UptimeSec":87,"Heap":25,"SleepMode":"Dynamic","Sleep":50,"LoadAvg":19,"MqttCount":1,"POWER":"OFF","Wifi":{"AP":1,"SSId":"CarraraRocks","BSSId":"A4:2B:B0:E9:32:9E","Channel":7,"RSSI":80,"Signal":-60,"LinkCount":1,"Downtime":"0T00:00:06"}}

        //11:23:56 MQT: v1/devices/me/telemetry/tele/tasmota/STATE = {"Time":"2020-01-06T11:23:56","Uptime":"0T00:01:12","UptimeSec":72,"Heap":26,"SleepMode":"Dynamic","Sleep":50,"LoadAvg":19,"MqttCount":1,"POWER":"OFF","Wifi":{"AP":1,"SSId":"CarraraRocks","BSSId":"A4:2B:B0:E9:32:9E","Channel":7,"RSSI":82,"Signal":-59,"LinkCount":1,"Downtime":"0T00:00:06"}}

        // TOPIC: v1/devices/me/telemetry
        // %prefix%/%topic%
        // 11:34:35 MQT: tele/v1/devices/me/telemetry/STATE = {"Time":"2020-01-06T11:34:35","Uptime":"0T00:05:27","UptimeSec":327,"Heap":26,"SleepMode":"Dynamic","Sleep":50,"LoadAvg":19,"MqttCount":2,"POWER":"OFF","Wifi":{"AP":1,"SSId":"CarraraRocks","BSSId":"A4:2B:B0:E9:32:9E","Channel":7,"RSSI":82,"Signal":-59,"LinkCount":1,"Downtime":"0T00:00:06"}}


        // POW r2
        // 11:35:39 MQT: tele/v1/devices/me/telemetry/STATE = {"Time":"2020-01-06T11:35:39","Uptime":"0T00:00:13","UptimeSec":13,"Heap":27,"SleepMode":"Dynamic","Sleep":50,"LoadAvg":34,"MqttCount":1,"POWER":"OFF","Wifi":{"AP":1,"SSId":"CarraraRocks","BSSId":"A4:2B:B0:E9:32:9E","Channel":7,"RSSI":84,"Signal":-58,"LinkCount":1,"Downtime":"0T00:00:06"}}
        // 11:35:39 MQT: tele/v1/devices/me/telemetry/SENSOR = {"Time":"2020-01-06T11:35:39","ENERGY":{"TotalStartTime":"2019-12-31T14:11:05","Total":0.000,"Yesterday":0.000,"Today":0.000,"Period":0,"Power":0,"ApparentPower":0,"ReactivePower":0,"Factor":0.00,"Voltage":0,"Current":0.000}}

        // TH

        String dest = kSEMIOTLY;

        // DEMO
        String broker = "tcp://demo.thingsboard.io:1883";
        String clientId = "9291b9e0-2fa3-11ea-8ddd-390ddd94abf9";
        String assetId = "9291b9e0-2fa3-11ea-8ddd-390ddd94abf9";
        String username = "Yj9XivNgh39tvQG8jGNQ";
        String password = "";

        switch(dest) {
            case kSONOFF_PROD:
                broker = "tcp://cloud.thingsboard.io:1883";
                clientId = "feeca430-2f97-11ea-9974-0d2c3d0cb3dd";
                topic = "v1/devices/me/telemetry";//?what=SENSOR";

                username = "rktl9O298JNIbp0KMzrp";
                break;
            case kSONOFF_PROD_TLS:
                broker = "ssl://cloud.thingsboard.io:8883";
                clientId = "feeca430-2f97-11ea-9974-0d2c3d0cb3dd";
                username = "rktl9O298JNIbp0KMzrp";
                break;
            case kGATEWAY_PROD:
                broker = "tcp://cloud.thingsboard.io:1883";
                clientId = "591aa0f0-2fa8-11ea-9974-0d2c3d0cb3dd";

                username = "5QCTyGz6xiUhT0RyFDEL";
                break;
            case kSEMIOTLY:

                broker = "ssl://mqtt.semioty-sandbox.com:8883";
                assetId = "123456789";
                clientId = assetId + "_CLIENT";

                username = "DVES_703B0E";
                password = "Test1234";
                topic = username + "/" + assetId + "/measures";//username/asset-id
                break;
        }

        MemoryPersistence persistence = new MemoryPersistence();
        try {
            ObjectNode payload = JsonNodeFactory.instance.objectNode();

            if (true) {
                // primo fomato {ts:ttttt, data: {attr1: xxx, attr2: yyy}}
                payload.put("ts",System.currentTimeMillis());
                //payload.put("devName", "ddaas");
                ObjectNode values = JsonNodeFactory.instance.objectNode();
                values.put("measure1", 100 + RandomUtils.nextInt(0, 10));
                values.put("measure2", 24 + RandomUtils.nextInt(0, 10));
                //payload.put("values", values);
                payload.put("data", values);
            } else {

                // secondo fomato {attr1: xxx, attr2: yyy}

                // il nome è case sensitive
                payload.put("measure1", 100 + RandomUtils.nextInt(0, 10));
                payload.put("measure2", 24 + RandomUtils.nextInt(0, 10));
            }
            // 20:52:15 MQT: v1/devices/me/telemetry?what=/SENSOR = {"Time":"2020-01-07T20:52:15","ENERGY":{"TotalStartTime":"2019-12-31T14:11:05","Total":0.000,"Yesterday":0.000,"Today":0.000,"Period":0,"Power":0,"ApparentPower":0,"ReactivePower":0,"Factor":0.00,"Voltage":0,"Current":0.000}}
            // thingsboard non supporta oggetti composti
            //ObjectNode energy = JsonNodeFactory.instance.objectNode();
            //energy.put("VOLTAGE", RandomUtils.nextInt(0, 10));
            //energy.put("Power", RandomUtils.nextInt(0, 10));
            //payload.set("ENERGY", energy);

            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: " + payload.toString());
            MqttMessage message = new MqttMessage(payload.toString().getBytes());
            message.setQos(qos);
            System.out.println("Publishing to topic: " + topic);
            sampleClient.publish(topic, message);

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