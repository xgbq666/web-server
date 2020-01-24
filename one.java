import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.File; 
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintStream;

public class Example {

	public static void main(String[] args) throws IOException {


		ServerSocket server = new ServerSocket(9999);

		System.out.println("服务器建立完成");

		while (true) {

			Socket socket = server.accept();

			System.err.println("接收到请求！");

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

			PrintStream output = new PrintStream(socket.getOutputStream());

			System.out.println("开始响应请求");

			output.print("MSS Studio");

			output.close();
			
			System.err.println("成功响应本次请求\n\n\n");
			
		}

	}

}