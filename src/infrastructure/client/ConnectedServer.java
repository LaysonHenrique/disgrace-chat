package src.infrastructure.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.json.JSONObject;

public class ConnectedServer {
    private final ObjectOutputStream outputChannel;
    private final ObjectInputStream inputChannel;
    private final Socket socket;

    public ConnectedServer(Socket socket) throws IOException {
        this.socket = socket;
        this.outputChannel = new ObjectOutputStream(this.socket.getOutputStream());
        this.inputChannel = new ObjectInputStream(this.socket.getInputStream());
    }

    private void listenEvents() throws IOException, ClassNotFoundException {
        while(this.socket.isConnected()) { 
            String dataString = (String) this.inputChannel.readObject();
            JSONObject eventObject = new JSONObject(dataString);
            String event = eventObject.getString("event");
            JSONObject data = eventObject.getJSONObject("data");

            System.out.println("Event: " + event + "\nData: " + data.toString(4));
        }
    }

    public void listenEventsInParallel() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    listenEvents();
                } 
                catch (IOException | ClassNotFoundException error) {
                    try { closeConnection(); } catch (IOException error_) {}
                }
            }
        };

        Thread parallelThread = new Thread(runnable);
        parallelThread.start();
    }

    public void closeConnection() throws IOException {
        this.socket.close();
        this.inputChannel.close();
        this.outputChannel.close();
    }
}
