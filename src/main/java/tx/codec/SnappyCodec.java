/*
* Copyright © 2011 Talis Systems Ltd.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package tx.codec;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.xerial.snappy.Snappy;
import org.xerial.snappy.SnappyException;

// from: http://svn.apache.org/repos/asf/avro/trunk/lang/java/avro/src/main/java/org/apache/avro/file/SnappyCodec.java

public class SnappyCodec implements Codec {

	public SnappyCodec() {}

	@Override
	public String getName() {
		return "Snappy";
	}
	
	@Override
	public ByteBuffer compress(ByteBuffer bb) throws IOException {
		ByteBuffer out;
		int size;
		try {
			if ( bb.hasArray() ) {
				out = ByteBuffer.allocate(Snappy.maxCompressedLength(bb.remaining()));
				size = Snappy.compress(bb.array(), bb.position(), bb.remaining(), out.array(), 0);
			} else {
				out = ByteBuffer.allocateDirect(Snappy.maxCompressedLength(bb.remaining()));
				size = Snappy.compress(bb, out);
			}
			out.limit(size);
			return out;
		} catch (SnappyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public ByteBuffer uncompress(ByteBuffer bb) throws IOException {
		ByteBuffer out;
		int size;
		try {
			if ( bb.hasArray() ) {
				out = ByteBuffer.allocate(Snappy.uncompressedLength(bb.array()));
				size = Snappy.uncompress(bb.array(), bb.position(), bb.remaining(), out.array(), 0);
			} else {
				out = ByteBuffer.allocateDirect(Snappy.uncompressedLength(bb));
				size = Snappy.uncompress(bb, out);
			}
			out.limit(size);
			return out;
		} catch (SnappyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public byte[] compress(byte[] b) throws IOException {
		try {
			return Snappy.compress(b);
		} catch (SnappyException e) {
			throw new IOException(e);
		}
	}

	@Override
	public byte[] uncompress(byte[] b) throws IOException {
		try {
			return Snappy.uncompress(b);
		} catch (SnappyException e) {
			throw new IOException(e);
		}
	}

}
