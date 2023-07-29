import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;


public class Client extends JFrame {

    Socket socket;
    
   BufferedReader br;
    PrintWriter out;


    //
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    



    public Client(){
        
        try {
          System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1",7778);
             System.out.println("connection done");

              br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          
             out = new PrintWriter(socket.getOutputStream());

              createGUI();
              handleEvent();
              startReading();
        //      startWriting();

            
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void handleEvent(){

        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                
               // System.out.println("key realesed" + e.getKeyCode());
                if(e.getKeyCode()==10){

                   // System.out.println("you have pressed enter button");
                    String contentToSend = messageInput.getText();
                    messagArea.append("Me :"+contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                    
                }
            }
           
            
        });

    }

    private void createGUI(){


        this.setTitle("Client Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // coding for component

        heading.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        messagArea.setEditable(false);
        //frame ka layout set karenge
        this.add(heading,BorderLayout.NORTH);
       JScrollPane sCroll = new JScrollPane(messagArea);
       this.add(sCroll, BorderLayout.CENTER);
        
        this.add(messageInput,BorderLayout.SOUTH);



        this.setVisible(true);

    }



    public void startReading(){

        // thread for read
        Runnable r1=()->{
            
            System.out.println("reader started");
        
           try {
             while(true){
        
            
                 String msg = br.readLine();
             if(msg.equals("exit")){
               System.out.println("Server terminated the chat");
               JOptionPane.showMessageDialog(this, "Sever terminated the chat");
               messageInput.setEnabled(false);
               socket.close();
               break;
             }
        
            // System.out.println("Server : "+msg);
            messagArea.append("Server :" + msg+"\n");
            
        
            }
           } catch (Exception e) {
        
             System.out.println("Connection closed");
            // e.printStackTrace();
           }
        };
        new Thread(r1).start();
            }


            public void startWriting(){

                System.out.println("Writer started");
        // thread for take and send to user
                Runnable r2=()->{
              
                    try {
                        while(true && !socket.isClosed()){
        
                        
                            
                            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        
                            String content = br1.readLine();
                            out.println(content);
                            out.flush();

                            if(content.equals("exit")){
                                socket.close();
                                break;
                            }
        
        
        
                    
                    }
            
                    } catch (Exception e) {
                       
                        System.out.println("Connection closed");
                    }
        };
        
        new  Thread(r2).start();
        
        
            
        
            }
    

    public static void main(String[] args) {
        System.out.println("This is client");
       new Client();
    }
}
