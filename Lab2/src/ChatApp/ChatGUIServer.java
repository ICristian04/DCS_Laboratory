package ChatApp;


import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;

public class ChatGUIServer extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JTextField tf;
  private JTextArea ta;
  private JButton send;
  private DatagramSocket socket;
  private InetAddress address;
  private byte[] buffer;
  private String name;
  private DatagramPacket packet;


  public ChatGUIServer(String name) {
    super(name);
    this.name = name;
    tf = new JTextField();
    ta = new JTextArea();
    ta.setEditable(false);
    send = new JButton("Send");
    socket = null;
    address = null;
    packet = null;
    buffer = new byte[256];
    setLayout(new BorderLayout());
    tf.addActionListener(this);
    send.addActionListener(this);
    add(new JScrollPane(ta), "Center");
    JPanel p = new JPanel();
    p.setLayout(new GridLayout(1, 2));
    p.add(tf);
    p.add(send);
    add(p, "South");
    setSize(350, 350);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);

    try {
      socket = new DatagramSocket(2001);
      address = InetAddress.getByName("localhost");
    } catch (Exception e) {
      e.printStackTrace();
    }

    Thread threadReceive = new Thread(new ReceiveThread(socket, address, buffer, packet, ta));

    threadReceive.start();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == send) {
      String message = tf.getText();
      if (!message.isEmpty()) {
        String str = name + ": " + message;
        buffer = str.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 2000);
        try {
          socket.send(packet);
          ta.append(str + "\n");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        tf.setText("");
      }
    }
  }


  public static void main(String[] args) {
    new ChatGUIServer("Server");
  }
}