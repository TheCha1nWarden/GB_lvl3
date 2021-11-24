package Chat.adapter;

import Chat.gui.ChatFrame;

public class ChatAdapter {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8189;

    private final ChatFrame frame;
    private final ChatConnector connector;

    public ChatAdapter() {
        this.connector = new ChatConnector(HOST, PORT);
        this.frame = new ChatFrame(outboundMessage -> connector.sendMessage(outboundMessage));

        while (true) {
            try {
                frame.onReceive().accept(connector.receiveMessage());
            } catch (RuntimeException e) {
                System.out.println("Клиент вышел из чата. Соединение разорвано");
                System.exit(0);
                break;
            }
        }
    }
}
