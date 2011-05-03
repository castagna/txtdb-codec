package tx.codec;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.ning.compress.lzf.LZFDecoder;
import com.ning.compress.lzf.LZFEncoder;

public class LZFCodec implements Codec {

	@Override
	public String getName() {
		return "LZF";
	}

	@Override
	public ByteBuffer compress(ByteBuffer bb) throws IOException {
		byte[] b;
		if ( bb.hasArray() ) {
			b = bb.array();
		} else {
			b = new byte[bb.remaining()];
			bb.mark();
			bb.get(b);
			bb.reset();
		}
		
		ByteBuffer result = ByteBuffer.wrap(LZFEncoder.encode(b));
		
		return result;
	}

	@Override
	public ByteBuffer uncompress(ByteBuffer bb) throws IOException {
		byte[] b;
		if ( bb.hasArray() ) {
			b = bb.array();
		} else {
			b = new byte[bb.remaining()];
			bb.mark();
			bb.get(b);
			bb.reset();
		}
		
		ByteBuffer result = ByteBuffer.wrap(LZFDecoder.decode(b));
		
		return result; 
	}

	@Override
	public byte[] compress(byte[] b) throws IOException {
		return LZFEncoder.encode(b);
	}

	@Override
	public byte[] uncompress(byte[] b) throws IOException {
		return LZFDecoder.decode(b);
	}

}
