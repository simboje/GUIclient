/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guiclient;

import java.awt.BorderLayout;
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
/**
 *
 * @author Simba
 */
public class GUIclient extends JFrame{

    
    Socket echoSocket=null;
    int portNumber = 7777;
    String hostName="localhost";
    
    JScrollPane sp;
    JPanel panelUser = new JPanel();
    JPanel panelSettings = new JPanel();    // TO DO: needs better alignment of elements!
    
    
    JButton b1 = new JButton("Send.");
    JButton b2 = new JButton("Update port number.");
    JLabel labelSet = new JLabel();
    JTextField tf1 = new JTextField(30);
    JTextField tfPort = new JTextField(6);
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
        this.add(BorderLayout.CENTER,panelUser);
        this.add(BorderLayout.EAST,panelSettings);
        //panelSettings.setLayout(new BorderLayout(panelSettings,BorderLayout.NORTH));
        panelSettings.add(tfPort);
        panelSettings.add(b2);
        panelSettings.add(labelSet);
        b1.addMouseListener(new ButtonListener());
        b2.addMouseListener(new UpdatePort());
        ta1.setLineWrap(true);
        tf1.addKeyListener(new KeyboardListener());
        panelUser.add(b1);
        panelUser.add(tf1);
        sp=new JScrollPane(ta1);
        panelUser.add(sp);
        this.setSize(580,400);
        //this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        connectToServer();
        
    }

    private void connectToServer() 
    {
        try { 
            echoSocket=new Socket(hostName,portNumber);
            bReader=new BufferedReader(new InputStreamReader(this.echoSocket.getInputStream()));
            pWriter = new PrintWriter(this.echoSocket.getOutputStream(),true);      // autoflush set to true
            labelSet.setText("Connection succesfull!");
        }
        catch (IOException ex)
        {
            tfPort.setText(null);
            labelSet.setText("Connection failed.");
            JOptionPane.showMessageDialog(new JButton("OK"), "Connection error - server not found.");
        }
    }

    private class UpdatePort implements MouseListener {

        public UpdatePort() {
        }

        @Override
        public void mouseClicked(MouseEvent me) 
        {
            portNumber = Integer.parseInt(tfPort.getText());
            connectToServer();
            
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
