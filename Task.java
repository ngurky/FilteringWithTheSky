package MajorFolder;

import org.firmata4j.Pin;

import java.util.TimerTask;

public class Task extends TimerTask {
    private Pin moistureSensor;
    private long moistureValue;
    private Pin light;
    private long lightValue;

    public Task(Pin moistureSensor, Pin light) {
        this.moistureSensor = moistureSensor;
        this.light = light;
    }

    public long getMoistureValue() {return this.moistureValue;}
    public long getLightValue() {return this.lightValue;}

    @Override
    public void run(){
        this.moistureValue = this.moistureSensor.getValue();
        this.lightValue = this.light.getValue();
    }
}
