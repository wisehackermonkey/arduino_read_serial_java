package edu.srjc.Final.Oran.Collins;

import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JSlider;

import com.fazecast.jSerialComm.SerialPort;

public class Main
{

    public static void main(String[] args)
    {
        JFrame window = new JFrame();
        JSlider slider = new JSlider();
        slider.setMaximum(1023);
        window.add(slider);
        window.pack();
        window.setVisible(true);

        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Select a port:");
        int i = 1;
        for (SerialPort port : ports)
        {
            System.out.println(i++ + ": " + port.getSystemPortName());
        }
        Scanner s = new Scanner(System.in);
        int chosenPort = s.nextInt();

        SerialPort serialPort = ports[chosenPort - 1];
        if (serialPort.openPort())
            System.out.println("Port opened successfully.");
        else
        {
            System.out.println("Unable to open the port.");
            return;
        }
//        serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
//        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
//        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0,0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
        Scanner data = new Scanner(serialPort.getInputStream());
        String value = " ";
//        while (data.hasNextLine())
//        {
//            try
//            {
//
//                value = data.nextLine();
//                if(!value.equals("\n"))
//                {
//                    System.out.println(value);
//                }
//            }
//            catch (Exception e)
//            {
//            }
//        }


        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                Scanner text = new Scanner(serialPort.getInputStream());
                while (text.hasNext())
                {


                    try
                    {
                        String line = text.nextLine();
                        if(line.matches("[ABCD0-9#*]")){
                            System.out.println(line);

                        }

                    }
                    catch (Exception e)
                    {
                        System.err.println("Expcetion");
                        text.close();
                    }

                }

            }
        };

        thread.start();

        System.out.println("Done.");
    }
}
