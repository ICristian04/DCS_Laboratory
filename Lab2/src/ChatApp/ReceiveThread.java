package ChatApp;


import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ReceiveThread extends Thread {
  private DatagramSocket socket;
  private InetAddress address;
  private byte[] buffer;
  private DatagramPacket packet;
  private JTextArea ta;

  public ReceiveThread(DatagramSocket socket, InetAddress address, byte[] buffer, DatagramPacket packet, JTextArea ta) {
    this.socket = socket;
    this.address = address;
    this.buffer = buffer;
    this.packet = packet;
    this.ta = ta;
  }

  public void run() {
    while (true) {
      try {
        buffer = new byte[256];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String str = new String(buffer);
        ta.append(str + "\n");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
