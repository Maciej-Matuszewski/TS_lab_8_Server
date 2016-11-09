import java.awt.HeadlessException;
import java.io.*;
import java.net.*;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

public class Main{
	
    ServerSocket server;
    Socket client = null;
    ObjectOutputStream out;
    ObjectInputStream in;

    void run()
    {
        try{
            server = new ServerSocket(9666, 10);
            System.out.println("Serwer: Start");
//            JOptionPane.showMessageDialog(null,"Serwer: Start");
            client = server.accept();
            System.out.println("Serwer: Connection received from " + client.getInetAddress().getHostName());
//            JOptionPane.showMessageDialog(null,"Serwer: Connection received from " + client.getInetAddress().getHostName());
            
            out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            in = new ObjectInputStream(client.getInputStream());
            String recivedMessage;
            
            java.util.Date date= new java.util.Date();
            System.out.println("Serwer ODS v0.0.1, " + new Timestamp(date.getTime()));
            this.sendMessage("220", out);
            
            recivedMessage = (String)in.readObject();
            if (!recivedMessage.contains("helo")){
        		JOptionPane.showMessageDialog(null, "Serwer: Coś poszło nie tak :(");
        		this.run();
        		return;
            }
            
            System.out.println("uzytkownik.internet.com Hello at serwer.email.com [" + client.getInetAddress() + "]");
            this.sendMessage("250", out);
            
            recivedMessage = (String)in.readObject();
            if (!recivedMessage.contains("mail from")){
        		JOptionPane.showMessageDialog(null, "Serwer: Coś poszło nie tak :(");
        		this.run();
        		return;
            }
            
            String from = recivedMessage.substring(12, recivedMessage.length() - 1);
            System.out.println("OK");
            this.sendMessage("250", out);

            
            recivedMessage = (String)in.readObject();
            if (!recivedMessage.contains("rcpt to")){
        		JOptionPane.showMessageDialog(null, "Serwer: Coś poszło nie tak :(");
        		this.run();
        		return;
            }
            
            String to = recivedMessage.substring(10, recivedMessage.length() - 1);
            System.out.println("Accepted");
            this.sendMessage("250", out);

            
            recivedMessage = (String)in.readObject();
            if (!recivedMessage.contains("data")){
        		JOptionPane.showMessageDialog(null, "Serwer: Coś poszło nie tak :(");
        		this.run();
        		return;
            }
            
            System.out.println("Enter message");
            this.sendMessage("354", out);
            
            recivedMessage = (String)in.readObject();
            
            System.out.println("\nFrom: " + from + "\nTo: " + to + "\nData: " + recivedMessage + "\n");
            
            System.out.println("OK id=1Coql6-0003Qi-MP");
            this.sendMessage("250", out);

            
            recivedMessage = (String)in.readObject();
            if (!recivedMessage.contains("quit")){
        		JOptionPane.showMessageDialog(null, "Serwer: Coś poszło nie tak :(");
        		this.run();
        		return;
            }
            
            System.out.println("serwer.email.com closing connection");
            this.sendMessage("221", out);
            
    		
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        } catch (HeadlessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        finally{
        }
    }
	
	void sendMessage(String msg, ObjectOutputStream out)
    {
        try{
        	System.out.println("Server -> " + client + ": "+msg);
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public static void main(String args[]){
        Main server = new Main();
        server.run();  
    }
}