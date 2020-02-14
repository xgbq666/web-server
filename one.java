import java.io.*;
import java.net.*;

class MyUrl{
	StringBuilder sb;
	
	String target;
	
	public MyUrl(StringBuilder sb1) {
		this.sb = sb1;
	}
	
	public void getUrl() {
		String trans = ""+sb;
		
		String[] temp = trans.split(" ");
		
		this.target = temp[1].substring(1);
	}
}


class Myresponse implements Runnable{
	Socket socket;
	
	String errorpage = "404.html";
	
	public Myresponse(Socket socket1) {
		this.socket = socket1;
	}
	
	public static String contentType(String filename) {
		String type = filename.substring(filename.lastIndexOf(".")+1);
		if(type == "html") {
			return "text/html";
		}
		else if(type == "jpg") {
			return "image/jpeg";
		}
		else if(type == "png") {
			return "image/png";
		}
		else {
			System.err.println("Can not identify the filetype");
			return "abc";
		}
	}
	
	public void response(String filename) {
		try {
			PrintStream output = new PrintStream(socket.getOutputStream());
		
			FileInputStream fis = new FileInputStream(new File(filename));
		
			String repsonsedHead = "HTTP/1.1 200 \r\n" + "Content-Type:"+contentType(filename)+"\r\n" + "\r\n";

			byte[] b = new byte[1024];

			output.write(repsonsedHead.getBytes());
	
			int len;

			while((len = fis.read(b)) != -1){
				output.write(b,0,len);
			}
	
			output.write(("MSS Studio").getBytes());
		
			fis.close();
			
			output.close();

			System.err.println("成功响应本次请求\n\n\n");
		}catch(IOException b) {
			
		}
		
		
	}

	@Override
	public void run(){
		
		try {
			
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			StringBuilder sb = new StringBuilder();

			Integer bufferSize = 1024;

			char[] buffer = new char[bufferSize];

			Integer count = bufferSize;

			while (count.equals(bufferSize)) {
				count = input.read(buffer);
				sb.append(buffer);
			}
			
			System.err.println("获得的请求报文如下：\n"+sb);

			System.out.println("开始响应请求");
			
			MyUrl url =new MyUrl(sb);
			
			url.getUrl();
			
			File a = new File(url.target);
				
			if(a.isDirectory()) {
				File[] files = a.listFiles();
				
				for(int m=0;m<files.length;m++) {
					
					this.response(files[m].getName());

				}
			}
			else {
				this.response(a.getName());
			}
			
		} catch (IOException p) {
			this.response(this.errorpage);
		}
		
	}
	
}

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(9999);
		
		System.out.println("服务器建立完成");
		
		while(true) {
			Socket socket = server.accept();
			
			System.err.println("接收到请求！");
			
			Thread t = new Thread(new Myresponse(socket));
			
			t.start();
		}
	}
}