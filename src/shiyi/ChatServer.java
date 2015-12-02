package shiyi;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class ChatServer {
	
	private boolean started = false;
	private ServerSocket ss = null;
	
	List<Client> clients = new ArrayList<Client>();
	
	public static void main(String[] args) {
		new ChatServer().start();
	}
	
	public void start(){
		try {
			ss = new ServerSocket(8888);
			started = true;
		} catch(BindException e){
			System.out.println("�˿�ʹ���С�����");
			System.out.println("��ص���س����������з�����");
			System.exit(0);
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		try{
			
			while(started){
				Socket s = ss.accept();
				Client c = new Client(s);
System.out.println("a client connected");
				new Thread(c).start();
				clients.add(c);
			}
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private class Client implements Runnable{
		
		private Socket s; //����һ����
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bConnected = false;
		
		public Client(Socket s){
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		public void send(String str){

				try {
					dos.writeUTF(str);
				} catch (IOException e) {
					clients.remove(this);
					System.out.println("�Է��˳��ˣ� �Ҵ�list����ȥ����");
				}
		}
		
		@Override
		public void run() {
			//Client c = null;
			try {
				while(bConnected){
					String str = dis.readUTF();
System.out.println(str);
					for(int i = 0; i < clients.size(); i++){
						Client c = clients.get(i);
						c.send(str);
					}
				}

			} catch(SocketException e){
				System.out.println("Client Quit");
			}
			catch(EOFException e){
				System.out.println("Client Closed");
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				try {
					if(dis != null) dis.close();
					if(dos != null) dos.close();
					if(s != null) {
						s.close();
						s = null;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		
	}
}
