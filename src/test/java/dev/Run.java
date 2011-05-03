package dev;

import java.io.IOException;
import java.nio.ByteBuffer;

import tx.codec.Codec;
import tx.codec.DeflateCodec;
import tx.codec.LZFCodec;
import tx.codec.NullCopyCodec;
import tx.codec.SnappyCodec;

import com.hp.hpl.jena.tdb.base.block.BlockMgr;
import com.hp.hpl.jena.tdb.base.block.BlockMgrFactory;
import com.hp.hpl.jena.tdb.sys.SystemTDB;

public class Run {

	public static void main(String[] args) throws IOException {
		Codec[] codecs = new Codec[] { 
			new SnappyCodec(),
			new LZFCodec(),
			new NullCopyCodec(),
			new DeflateCodec(0),
			new DeflateCodec(2),
			new DeflateCodec(6),
			new DeflateCodec(9),
		};
		
		for (Codec codec : codecs) {
			run("/opt/datasets/tdb/jake-dev1/SPO.dat", codec);			
		}
	}

	private static void run(String filename, Codec codec) throws IOException {
		BlockMgr blockMgr = BlockMgrFactory.createMMapFile(filename, SystemTDB.BlockSize);
		int i = 0;
		long bytes_total_uncompressed = 0l;
		long bytes_total_compressed = 0l;
		long start = System.currentTimeMillis();
		while ( blockMgr.valid(i) ) {
			ByteBuffer bb = blockMgr.get(i);
			ByteBuffer compressed = codec.compress(bb);
			ByteBuffer uncompressed = codec.uncompress(compressed);
			bytes_total_uncompressed += bb.remaining();
			bytes_total_compressed += compressed.remaining();
			if ( !bb.equals(uncompressed) ) {
				System.out.println(bb);
				System.out.println(uncompressed);
				throw new IOException("Uncompressed content is not equal to the original content.");
			}
			i++; 
		}
		long stop = System.currentTimeMillis();
		System.out.printf("%s: blocks = %d, bytes uncompressed = %d, compressed = %d (%.0f%%), elapsed time = %d ms\n", 
				codec.getName(), i, bytes_total_uncompressed, bytes_total_compressed, bytes_total_compressed * 1.0 / bytes_total_uncompressed * 100, (stop-start));		
	}
	
}
