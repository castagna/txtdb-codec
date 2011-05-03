package dev;

import java.util.zip.Adler32;

public class RunAdler32 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		byte[] b = new String("Hello World").getBytes();
		Adler32 adler = new Adler32();
		adler.update(b);
		System.out.println(adler.getValue());
	}

}
