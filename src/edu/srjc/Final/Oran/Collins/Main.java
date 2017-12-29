package edu.srjc.Final.Oran.Collins;
import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;

public class Main
{

    public static void main(String[] args)
    {
        SerialPort[] ports = SerialPort.getCommPorts();

        System.out.println("Select a port:");

        int i = 1;
        for (SerialPort port : ports)
        {
            System.out.println(i++ + ": " + port.getSystemPortName());
        }

        Scanner s = new Scanner(System.in);

        int portChoice = s.nextInt();


        SerialPort serialPort = ports[portChoice - 1];

        if (serialPort.openPort())
        {
            System.out.println("Port opened successfully.");
        }
        else
        {
            System.out.println("Unable to open the port.");
            return;
        }

        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                Scanner serialData = new Scanner(serialPort.getInputStream());
                while (serialData.hasNext())
                {
                    try
                    {
                        String line = serialData.nextLine();

                        if(line.matches("[ABCD0123456789#*]")){
                            System.out.println(line);
                        }
                    }
                    catch (Exception err)
                    {
                        System.err.println("Exception: Reading Arduino serial port");
                        serialData.close();
                    }

                }

            }
        };

        thread.start();

        System.out.println("Done.");
    }
}
