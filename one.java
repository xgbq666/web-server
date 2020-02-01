import java.io.*;
import java.net.*;

public class Web1 {
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
			
			StringBuilder Filepath = new StringBuilder();
			
			for(int i=5;i<sb.length();i++) {			
				if(buffer[i] == ' ') {
					break;
				}
				Filepath.append(buffer[i]);
			}
			
			String target = ""+Filepath;

			System.err.println("获得的请求报文如下：\n"+sb);

			PrintStream output = new PrintStream(socket.getOutputStream());

			System.out.println("开始响应请求");
			
			File a = new File(target);
			
			File wrongpage = new File("/page/404.html");
			
			if(a.isDirectory()) {
				File[] files = a.listFiles();
				
				FileInputStream fis = new FileInputStream(files[0]);
				
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
				FileInputStream fis = new FileInputStream(a);
			
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

			output.close();

			System.err.println("成功响应本次请求\n\n\n");
			
		}

	}
}
