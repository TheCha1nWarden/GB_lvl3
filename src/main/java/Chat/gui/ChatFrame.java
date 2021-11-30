package Chat.gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ChatFrame extends JFrame{
    private JFrame frame;
    private ChattingFrame chattingFrame;
    private SendingFrame sendingFrame;

    public ChatFrame(Consumer<String> onSubmit) {
        frame = new JFrame();
        chattingFrame = new ChattingFrame();
        sendingFrame = new SendingFrame(onSubmit);
        frame.setTitle("Chat");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setBounds(450,100,700,600);

        frame.add(chattingFrame.getFrame(), BorderLayout.CENTER);
        frame.add(sendingFrame.getFrame(),BorderLayout.SOUTH);

        frame.setVisible(true);
    }
    public Consumer<String> onReceive() {
        return chattingFrame.getOnReceive();
    }
}
