import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Monitor implements Runnable{

    ObjectOutputStream out;
    ObjectInputStream in;
    String client;

    public Monitor(ObjectOutputStream output, ObjectInputStream input, String sender) {
    	
    	this.client = sender;
    	this.out = output;
    	this.in = input;
    	
	}
    
	@Override
	public void run() {
		String message = "";
		String lastMessage = "";
		do{
			try {
				lastMessage = message;
				message = (String)in.readObject();
				System.out.println(this.client + ": " +message);
				sendMessage(message, out);
				if(!lastMessage.equals("180 Ringing") && message.equals("200 OK")) break;
			} catch (ClassNotFoundException e) {
			} catch (IOException e) {
			}
             
         }while(!message.equals("BYE"));
		
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

}
