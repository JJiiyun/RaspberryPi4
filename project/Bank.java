package project;

import javax.net.ssl.SSLContext;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import week7.I2CLCD;

public class Bank {
	int waiting[];
	int customerCnt; //대기 손님 번호
	int current; //창구 직원이 담당하는 손님 번호
	
	public Bank() {
		customerCnt = 0;
	    current = 0;
	    waiting = new int[50];
	    for (int i = 0; i < waiting.length; i++) {
	        waiting[i] = i + 1;  // i + 1로 변경
	    } //waiting = [1,2,3,4,5, ... , 50]
	}
		public void setBank() {
		customerCnt = 0;
		current = 0;
	}
	
	public static void main(String[] args) {
		GpioController gpio = GpioFactory.getInstance();
		GpioPinDigitalOutput r_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
		GpioPinDigitalOutput g_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
		GpioPinDigitalOutput b_led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
		GpioPinDigitalInput customer_btn = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28);
		GpioPinDigitalInput employee_btn = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29);
		
		boolean customer_pressed = false;
		boolean employee_pressed = false;
		boolean set_pressed = false;
		
		try {
	        Bank s = new Bank();
	        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
	        I2CDevice dev = bus.getDevice(0x27);
	        I2CLCD lcd = new I2CLCD(dev);
	        lcd.init();
	        lcd.backlight(true);

	        while (true) {
	            r_led.low();
	            g_led.low();
	            b_led.low();

	            customer_pressed = customer_btn.isHigh();
	            employee_pressed = employee_btn.isHigh();

	            if (customer_pressed == false && employee_pressed == false) {
	                // 화면에 창구 직원의 번호 표시
	                lcd.clear();
	                lcd.display_string("Current", 1);
	                lcd.display_string("Number " + s.current, 2);
	            }

	            if (customer_pressed == true) {
	            	// 고객 번호 증가
	            	s.customerCnt++;

	            	// 고객 버튼이 눌렸을 때
	                r_led.low();
	                g_led.low();
	                b_led.high();

	                // 화면에 고객의 번호 표시
	                lcd.clear();
	                lcd.display_string("Welcome", 1);
	                lcd.display_string("Number " + s.customerCnt, 2);


	                customer_pressed = false;

	                try {
	                    Thread.sleep(2000);
	                } catch (Exception e) {
	                }
	            } else if (employee_pressed == true) {
	                r_led.low();
	                g_led.high();
	                b_led.low();

	                lcd.clear();
	                lcd.display_string("Next", 1);
	                lcd.display_string("Welcome " + s.waiting[s.current], 2);

	                s.current++;

	                employee_pressed = false;

	                try {
	                    Thread.sleep(2000);
	                } catch (Exception e) {
	                }
	            }

	            Thread.sleep(2000);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	
}
		