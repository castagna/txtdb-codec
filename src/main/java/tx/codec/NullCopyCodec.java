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
