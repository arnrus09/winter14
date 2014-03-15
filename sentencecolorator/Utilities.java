package sentencecolorator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utilities {

	public static void command(String[] args){
		ProcessBuilder pb = new ProcessBuilder(args);
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	static String convertStreamToString(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader( new InputStreamReader(input) );
		String result = "";
		String s;
		while( ( s = reader.readLine() ) != null )
			result += s + "\n";
		return result;
	}
	
	public static void main(String[] args){
		try {
			System.out.println(convertStreamToString(new ProcessBuilder("pwd").start().getInputStream() ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
