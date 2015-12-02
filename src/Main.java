import java.io.*;
import java.net.*;
public class Main{
	
    ServerSocket server;
    Socket client = null;
    Socket client2 = null;
    ObjectOutputStream out;
    ObjectOutputStream out2;
    ObjectInputStream in;
    ObjectInputStream in2;

    void run()
    {
        try{
            server = new ServerSocket(9666, 10);
            System.out.println("Serwer: Start");
            client = server.accept();
            System.out.println("Connection 1 received from " + client.getInetAddress().getHostName());
            client2 = server.accept();
            System.out.println("Connection 2 received from " + client2.getInetAddress().getHostName());
            
            out = new ObjectOutputStream(client.getOutputStream());
            out2 = new ObjectOutputStream(client2.getOutputStream());
            out.flush();
            out2.flush();
            in = new ObjectInputStream(client.getInputStream());
            in2 = new ObjectInputStream(client2.getInputStream());
            
            Thread t1 = new Thread(new Monitor(out, out2, in2, client2.getInetAddress().getHostName()));
    		t1.start();
            
    		Thread t2 = new Thread(new Monitor(out2, out, in, client.getInetAddress().getHostName()));
    		t2.start();
    		
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
        }
    }
    
    void sendMessage(String msg, ObjectOutputStream out)
    {
        try{
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    public static void main(String args[])
    {
        Main server = new Main();
        server.run();
        
    }
}