import java.io.*;
import java.net.*;

public class Web1 {
	public static String contenttype(String filename) {
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
	
	
	public static void main(String[] args) throws IOException {

		ServerSocket server = new ServerSocket(9999);

		System.out.println("服务器建立完成");

		while (true) {

			Socket socket = server.accept();

			System.err.println("接收到请求！");

			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));

			StringBuilder sb = new StringBuilder();

			Integer bufferSize = 1024;

			char[] buffer = new char[bufferSize];

			Integer count = bufferSize;

			while (count.equals(bufferSize)) {
				count = input.read(buffer);
				sb.append(buffer);
			}
			
			String trans = ""+sb;
			
			String[] temp = trans.split(" ");
			
			String target = temp[1].substring(1);

			System.err.println("获得的请求报文如下：\n"+sb);

			PrintStream output = new PrintStream(socket.getOutputStream());

			System.out.println("开始响应请求");
			
			File a = new File(target);
			
			File wrongpage = new File("404.html");
			
			try {
				if(a.isDirectory()) {
					File[] files = a.listFiles();
					
					if(files.length == 0) {
						FileInputStream fis = new FileInputStream(wrongpage);
						
						String repsonsedHead = "HTTP/1.1 200 \r\n" + "Content-Type:text/html\r\n" + "\r\n";

						byte[] b = new byte[1024];

						output.write(repsonsedHead.getBytes());
					
						int len;

						while((len = fis.read(b)) != -1){
							output.write(b,0,len);
						}
					
						output.write(("MSS Studio").getBytes());
						
						fis.close();
					}
					else {
						for(int m=0;m<files.length;m++) {
							
						FileInputStream fis = new FileInputStream(files[m]);
						
						String repsonsedHead = "HTTP/1.1 200 \r\n" + "Content-Type:"+contenttype(files[m].getName())+"\r\n" + "\r\n";

						byte[] b = new byte[1024];

						output.write(repsonsedHead.getBytes());
					
						int len;

						while((len = fis.read(b)) != -1){
							output.write(b,0,len);
						}
					
						output.write(("MSS Studio").getBytes());
						
						fis.close();
						}
					}				
				}
				
				else {
					FileInputStream fis = new FileInputStream(a);
				
					String repsonsedHead = "HTTP/1.1 200 \r\n" + "Content-Type:"+contenttype(a.getName())+"\r\n" + "\r\n";

					byte[] b = new byte[1024];

					output.write(repsonsedHead.getBytes());
				
					int len;

					while((len = fis.read(b)) != -1){
						output.write(b,0,len);
					}
				
					output.write(("MSS Studio").getBytes());
					
					fis.close();
				}
			}catch(IOException e) {
				
				FileInputStream fis = new FileInputStream(wrongpage);
				
				String repsonsedHead = "HTTP/1.1 200 \r\n" + "Content-Type:text/html\r\n" + "\r\n";

				byte[] b = new byte[1024];

				output.write(repsonsedHead.getBytes());
			
				int len;

				while((len = fis.read(b)) != -1){
					output.write(b,0,len);
				}
			
				output.write(("MSS Studio").getBytes());
				
				fis.close();
			}catch(ArrayIndexOutOfBoundsException f) {
				
			}

			output.close();

			System.err.println("成功响应本次请求\n\n\n");
			
		}

	}
}
