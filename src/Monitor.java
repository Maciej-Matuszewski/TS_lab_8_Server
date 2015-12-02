import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Monitor implements Runnable{

    ObjectOutputStream out;
    ObjectOutputStream cOut;
    ObjectInputStream in;
    String client;

    public Monitor(ObjectOutputStream output, ObjectOutputStream clientOutput, ObjectInputStream input, String sender) {
    	
    	this.client = sender;
    	this.out = output;
    	this.cOut = clientOutput;
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
				
				switch (message) {
				case "INVITE":
					sendMessage("100 Trying", cOut);
					sendMessage("180 Ringing", cOut);
					sendMessage("200 OK", cOut);
					break;

				case "100 Trying":
					break;
					
				default:
					sendMessage(message, out);
					if(!lastMessage.equals("180 Ringing") && message.equals("200 OK")) break;
					break;
				}
				
			} catch (ClassNotFoundException e) {
			} catch (IOException e) {
			}
             
         }while(!message.equals("BYE"));
		
	}
	
	void sendMessage(String msg, ObjectOutputStream out)
    {
        try{
        	//System.out.println("Server -> " + client + ": "+msg);
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

}
