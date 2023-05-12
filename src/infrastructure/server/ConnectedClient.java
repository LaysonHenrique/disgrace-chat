package src.infrastructure.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import java.net.Socket;

import org.json.JSONObject;

import src.entities.Client;

public class ConnectedClient extends Client {
    private final ObjectOutputStream outputChannel;
    private final ObjectInputStream inputChannel;
    private final Socket socket;

    public ConnectedClient(String name, int avatarId, Socket socket) throws IOException {
        super(name, avatarId);
        this.socket = socket;
        this.outputChannel = new ObjectOutputStream(this.socket.getOutputStream());
        this.inputChannel = new ObjectInputStream(this.socket.getInputStream());
    }

    public void emitEvent(String event, JSONObject data) throws IOException {
        String dataString = data.toString();
        this.outputChannel.writeObject(dataString);
    }

    private void listenEvents() throws IOException, ClassNotFoundException {
        while(this.socket.isConnected()) { 
            String dataString = (String) this.inputChannel.readObject();
            JSONObject eventObject = new JSONObject(dataString);
            String event = eventObject.getString("event");
            JSONObject data = eventObject.getJSONObject("data");
        
            //this.executeEventHandler(event, data);
        }
    }

    public void closeConnection() throws IOException {
        this.socket.close();
    }
}
