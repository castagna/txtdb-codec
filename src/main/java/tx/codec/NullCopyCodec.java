package tx.codec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class NullCopyCodec implements Codec {

	@Override
	public String getName() {
		return "NullCopy";
	}

	@Override
	public ByteBuffer compress(ByteBuffer bb) throws IOException {
		byte[] b;
		if ( bb.hasArray() ) {
			b = Arrays.copyOfRange(bb.array(), 0, bb.array().length);
		} else {
			b = new byte[bb.remaining()];
			bb.mark();
			bb.get(b);
			bb.reset();
		}
		return ByteBuffer.wrap(b);
	}

	@Override
	public ByteBuffer uncompress(ByteBuffer bb) throws IOException {
		byte[] b;
		if ( bb.hasArray() ) {
			b = Arrays.copyOfRange(bb.array(), 0, bb.array().length);
		} else {
			b = new byte[bb.remaining()];
			bb.mark();
			bb.get(b);
			bb.reset();
		}
		return ByteBuffer.wrap(b);
	}

	@Override
	public byte[] compress(byte[] b) throws IOException {
		return Arrays.copyOfRange(b, 0, b.length);
	}

	@Override
	public byte[] uncompress(byte[] b) throws IOException {
		return Arrays.copyOfRange(b, 0, b.length);
	}

}
