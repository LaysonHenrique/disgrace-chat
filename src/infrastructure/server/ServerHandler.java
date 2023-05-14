package src.infrastructure.server;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import src.interfaces.InputEventHandler;

public class ServerHandler {
    private final ChatServer server;

    public ServerHandler(ChatServer server) {
        this.server = server;
    }

    public InputEventHandler handleEventOnReceive() {
        return new InputEventHandler() {
            @Override
            public void execute(ConnectedClient connectedClient, String event, JSONObject data) throws IOException {
                switch (event) {
                    case "join":
                        joinEvent(connectedClient, data);
                        break;

                    default:
                        break;
                }
            }
        };
    }

    private void joinEvent(ConnectedClient client, JSONObject data) throws IOException {
        String username = data.getString("name");
        int avatarId = data.getInt("avatarId");

        client.setName(username);
        client.setAvatarId(avatarId);
        client.setAsIdentified();

        JSONObject eventObject = new JSONObject();
        JSONObject serverInfoObject = new JSONObject();

        ArrayList<ConnectedClient> connectedClients = this.server.getConnectedClients();
        JSONArray clientsArray = new JSONArray();

        for (ConnectedClient clientItem: connectedClients) {
            if (clientItem != client && clientItem.isIdentified()) {
                JSONObject clientObject = new JSONObject();
                clientObject.put("name", clientItem.getName());
                clientObject.put("avatarId", clientItem.getAvatarId());
                clientObject.put("channelId", clientItem.getChannelId());

                clientsArray.put(clientObject);
            }
        }
        
        serverInfoObject.put("connectedClients", clientsArray);
        serverInfoObject.put("name", this.server.getName());
        
        eventObject.put("channelId", client.getChannelId());
        eventObject.put("server", serverInfoObject);

        client.emitEvent("joined", eventObject);
    }
}
