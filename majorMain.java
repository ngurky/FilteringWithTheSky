package MajorFolder;

import edu.princeton.cs.introcs.StdDraw;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;

public class majorMain {
     public static void main(String[] args) throws IOException, InterruptedException {
         //CLO1
         //CLO5
         //CLO2
        var num = 1;
        String port = "COM4";

        //Initialize Variables
        var board = new FirmataDevice(port);
        board.start();
        System.out.println("Board started.");
        board.ensureInitializationIsDone();

        //Water Transport
        var pump = board.getPin(Pins.D7); //pump
        pump.setMode(Pin.Mode.OUTPUT);

        //Update
        var button = board.getPin(Pins.D6); //button
        button.setMode(Pin.Mode.INPUT);

        //Notification System
        var buzzer = board.getPin(Pins.D5);//buzzer
        buzzer.setMode(Pin.Mode.PWM);
        var i2cObject = board.getI2CDevice(Pins.I2C0); //oled
        SSD1306 oled = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
        oled.init();

        //Weather Station
        var light = board.getPin(Pins.A1);
        var moistureSensor = board.getPin(Pins.A2 + 1);

        //Event Listener for moisture and light
        SensorListener sensorListener = new SensorListener(moistureSensor,pump,oled, light, button, buzzer); //CLO4
        board.addEventListener(sensorListener);

        //Timer task
        var task = new Task(moistureSensor,light);
        new Timer().schedule(task,0,1000);

        HashMap<Integer, Integer> data = new HashMap<>(); //CLO3
        HashMap<Integer, Integer> data2 = new HashMap<>();

        //Set up graph
        StdDraw.setXscale(-13,100);
        StdDraw.setYscale(-30,1100);
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.line(0,0,0,1000);
        StdDraw.line(0,0,100,0);
        StdDraw.text(50,-30, "Time [s]");
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(-9,550, "Moisture");
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.text(-9,450, "Light");
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.text(50,1100,"Moisture/Light vs Time Graph");

        while (true) {
            data.put(num, (int) task.getMoistureValue());
            data2.put(num, (int) task.getLightValue());
            StdDraw.setPenColor(StdDraw.RED);
            data.forEach((xValue,yValue) -> StdDraw.text(xValue,yValue, "*"));
            StdDraw.setPenColor(StdDraw.BLUE);
            data2.forEach((xValue,yValue) -> StdDraw.text(xValue,yValue,"*"));
            Thread.sleep(1000);
            if (num<50){
                num++;
            }
            else{
                num=1;
            }
        }
    }
}
