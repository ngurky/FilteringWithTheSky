package MajorFolder;

import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;

import java.io.IOException;

public class SensorListener implements IODeviceEventListener {
    private final Pin light;
    private final SSD1306 oled;
    private final Pin pump;
    private final Pin moistureSensor;
    private final Pin button;
    private final Pin buzzer;
    SensorListener(Pin moistureSensor, Pin pump, SSD1306 oled, Pin light, Pin button, Pin buzzer ) {
        this.moistureSensor = moistureSensor;
        this.pump = pump;
        this.oled = oled;
        this.light = light;
        this.button = button;
        this.buzzer = buzzer;
        oled.getCanvas().setTextsize(2);

    }
    @Override
    public void onPinChange(IOEvent event) {
        if (event.getPin().getIndex() != button.getIndex()){
            return;
        }
        else if (moistureSensor.getValue() <= 710 && light.getValue() <= 100){
            oled.clear();
            oled.getCanvas().drawString(0,0, "Possible \nRain");
            oled.getCanvas().drawString(0,50, "Pump ON");
            oled.display();
            try {
                pump.setValue(1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                buzzer.setValue(1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
        else if (moistureSensor.getValue() >= 736 && light.getValue() > 100 ){
            oled.clear();
            oled.getCanvas().drawString(0,0,"Sunny");
            oled.getCanvas().drawString(0,50,"Pump OFF");
            try {
                pump.setValue(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            oled.display();
            try {
                buzzer.setValue(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void onStart(IOEvent ioEvent) {

    }

    @Override
    public void onStop(IOEvent ioEvent) {

    }

    @Override
    public void onMessageReceive(IOEvent ioEvent, String s) {

    }
}
