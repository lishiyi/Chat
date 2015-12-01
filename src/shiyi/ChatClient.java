package shiyi;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.*;
import java.io.*;

public class ChatClient extends Frame {
	
	private static final long serialVersionUID = 5037586126152572214L;
	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	private boolean bConnected = false;

	TextField tfTxt = new TextField();
	TextArea taContent = new TextArea();
	
	Thread tRecv = new Thread(new RecvThread());

	public static void main(String[] args) {

		new ChatClient().lauchFrame();
	}
	
	public void lauchFrame() {
		
		setLocation(400, 300);
		this.setSize(300, 300);
		add(tfTxt, BorderLayout.SOUTH);
		add(taContent, BorderLayout.NORTH);
		pack();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
				System.exit(0);
			}
		});
		tfTxt.addActionListener(new TFListener());
		setVisible(true);
		connect();
		
		tRecv.start();
	}
	
	public void connect(){
		try {
			s = new Socket("127.0.0.1", 8888);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			bConnected = true;
System.out.println("connected");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		
		try{
			dos.close();
			dis.close();
			s.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		/*
		try {
			bConnected = false;
			tRecv.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				dos.close();
				dis.close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		*/
	}
	
	private class TFListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String string = tfTxt.getText().trim();
			//taContent.setText(string);
			tfTxt.setText("");
			
			try {
				dos.writeUTF(string);
				dos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private class RecvThread implements Runnable{

		@Override
		public void run() {
			try{
				while(bConnected){
					String str = dis.readUTF();
					taContent.setText(taContent.getText() + str + "\n");
				}
			} catch (SocketException e1){
				System.out.println("ÍË³öÁË£¬bye");
				e1.printStackTrace();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
		
	}
}
