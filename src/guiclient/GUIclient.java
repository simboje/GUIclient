/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guiclient;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
/**
 *
 * @author Simba
 */
public class GUIclient extends JFrame{

    
    Socket echoSocket=null;
    int portNumber = 6999;
    String hostName="localhost";
    
    JScrollPane sp;
    JPanel panel1 = new JPanel();  
    JButton b1 = new JButton("Send.");
    JTextField tf1 = new JTextField(30);
    JTextArea ta1 = new JTextArea("Welcome to client app\n",20,50);
    
    BufferedReader bReader=null;
    PrintWriter pWriter=null;
    
    public static void main(String[] args) 
    {
        GUIclient gc=new GUIclient();
        gc.start();
    }

    private void start() 
    {       
        this.add(panel1);       
        b1.addMouseListener(new ButtonListener());
        ta1.setLineWrap(true);
        tf1.addKeyListener(new KeyboardListener());
        panel1.add(b1);
        panel1.add(tf1);
        sp=new JScrollPane(ta1);
        panel1.add(sp);
        this.setSize(580,400);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        try { 
            echoSocket=new Socket(hostName,portNumber);
            bReader=new BufferedReader(new InputStreamReader(this.echoSocket.getInputStream()));
            pWriter = new PrintWriter(this.echoSocket.getOutputStream(),true);      // autoflush set to trueS
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(new JButton("OK"), "Connection error - server not found.");
        }
    }


    private class ButtonListener implements MouseListener {

        public ButtonListener() 
        {}

        @Override
        public void mouseClicked(MouseEvent me) 
        {
            sendText();
        }

        @Override
        public void mousePressed(MouseEvent me) {}

        @Override
        public void mouseReleased(MouseEvent me) {}

        @Override
        public void mouseEntered(MouseEvent me) {}

        @Override
        public void mouseExited(MouseEvent me) {}
    }

    private class KeyboardListener implements KeyListener {

        public KeyboardListener() {        }

        @Override
        public void keyTyped(KeyEvent ke) {}

        @Override
        public void keyPressed(KeyEvent ke) 
        {
            if(ke.getKeyCode() == KeyEvent.VK_ENTER)
            {
                sendText();
            }
        }

        @Override
        public void keyReleased(KeyEvent ke) {}
    }
    
    public void sendText()
    {
        pWriter.println(tf1.getText());
        tf1.setText("");
        try 
        {
            ta1.append(bReader.readLine()+"\n");
            ta1.setCaretPosition(ta1.getDocument().getLength());
        } 
        catch (IOException ex) 
        {
            ta1.append("Error while reading data from server.\n");
        }
    }
    
}
