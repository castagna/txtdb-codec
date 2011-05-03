package tx.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hp.hpl.jena.tdb.sys.SystemTDB;

public class TestCodec {

	private static int BLOCK_SIZE = SystemTDB.BlockSize;
	private static int NUM_BLOCKS = 100000;
	private static byte[] random_b = new byte[BLOCK_SIZE];
	private static byte[] fixed_b = new byte[BLOCK_SIZE];
	private static ByteBuffer random_bb;
	private static ByteBuffer random_bb_direct;
	private static ByteBuffer fixed_bb;
	private static ByteBuffer fixed_bb_direct;
	private static Codec[] CODECS = new Codec[] { 
			new SnappyCodec(),
			new LZFCodec(),
			new NullCopyCodec(),
			new DeflateCodec(0),
			new DeflateCodec(1),
			new DeflateCodec(2),
			new DeflateCodec(6),
			new DeflateCodec(9),
	};

	@BeforeClass
	public static void setupBeforeClass() {
		Random r = new Random(System.currentTimeMillis());
		r.nextBytes(random_b);
		random_bb = ByteBuffer.wrap(random_b);
		
		random_bb_direct = ByteBuffer.allocateDirect(random_b.length);
		random_bb_direct.put(random_b);
		random_bb_direct.flip();

		for ( int i = 0; i < BLOCK_SIZE; i++ ) {
			fixed_b[i] = (byte)(i); 
		}
		fixed_bb = ByteBuffer.wrap(fixed_b);

		fixed_bb_direct = ByteBuffer.allocateDirect(fixed_b.length);
		fixed_bb_direct.put(fixed_b);
		fixed_bb_direct.flip();
	}
	
	@Test
	public void test() throws Exception {
		int n = 1;
		for (Codec codec : CODECS) {
			for ( int i = 0; i < n; i++ ) test(codec, true, true, false);
			for ( int i = 0; i < n; i++ ) test(codec, false, true, false);
			for ( int i = 0; i < n; i++ ) test(codec, true, true, true);
			for ( int i = 0; i < n; i++ ) test(codec, false, true, true);
			for ( int i = 0; i < n; i++ ) test(codec, true, false, false);
			for ( int i = 0; i < n; i++ ) test(codec, false, false, false);
		}			
	}
	
	private void test (Codec codec, boolean random, boolean useByteBuffer, boolean direct) throws IOException {
		long start = System.currentTimeMillis();
		long bytes_total_uncompressed = 0l;
		long bytes_total_compressed = 0l;
		for ( int i = 0; i < NUM_BLOCKS; i++ ) {
			if ( useByteBuffer ) {
				ByteBuffer bb = getByteBuffer(random, direct);
				ByteBuffer compressed = codec.compress(bb);
				ByteBuffer uncompressed = codec.uncompress(compressed);
				bytes_total_uncompressed += bb.remaining();
				bytes_total_compressed += compressed.remaining();
				assertEquals(bb, uncompressed);				
			} else {
				byte[] b = getByteArray(random);
				byte[] compressed = codec.compress(b);
				byte[] uncompressed = codec.uncompress(compressed);
				bytes_total_uncompressed += b.length;
				bytes_total_compressed += compressed.length;
				assertTrue(Arrays.equals(b, uncompressed));
			}
		}
		long elapsed = (System.currentTimeMillis() - start);

		System.out.printf("%12s: %d blocks (%d bytes each, rnd=%5s, bb=%5s, direct=%5s), uncompressed = %12d, compressed = %12d, elapsed time %5d ms = %5.4f ms per block = %5.0f MB/sec\n", 
				codec.getName(), 
				NUM_BLOCKS, 
				BLOCK_SIZE,
				random,
				useByteBuffer,
				direct,
				bytes_total_uncompressed, 
				bytes_total_compressed, 
				elapsed, 
				elapsed * 1.0 / NUM_BLOCKS,
				( bytes_total_uncompressed + bytes_total_compressed ) * 1.0 / ( elapsed * 1.0 / 1000 ) / ( 1024 * 1024 )
				
		);
	}
	
	private ByteBuffer getByteBuffer(boolean random, boolean direct) {
		if ( random ) {
			if ( direct ) {
				return random_bb_direct;				
			} else {
				return random_bb;
			}
		} else {
			if ( direct ) {
				return fixed_bb_direct;
			} else {
				return fixed_bb;
			}
		}
	}
	
	private byte[] getByteArray(boolean random) {
		if ( random ) {
			return random_b;	
		} else {
			return fixed_b;
		}
	}
	
}
