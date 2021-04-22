package config;

import java.net.UnknownHostException;

public class MyThread1 extends Thread {
    int k;
    public MyThread1(int i) {
            k = i;
    }

    @Override
    public void run() {
        //Your Code
    	System.out.println("Thread no. "+k);
    	try {
			sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("Thread no. "+k);

    }
}
class MainClass {

    public static void main(String arg[]) throws UnknownHostException {
        Refresh() ;
    }

    public static void Refresh(){

        //create 255 Thread using for loop
        for (int x = 0; x < 256; x++) {
            // Create Thread class 
            MyThread1 temp = new MyThread1(x);
                temp.start();
            try {
                temp.join(10);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}