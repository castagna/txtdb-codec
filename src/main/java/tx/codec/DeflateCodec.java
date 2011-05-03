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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;

// from: http://svn.apache.org/repos/asf/avro/trunk/lang/java/avro/src/main/java/org/apache/avro/file/DeflateCodec.java

public class DeflateCodec implements Codec {

	private ByteArrayOutputStream outputBuffer;
	private Deflater deflater;
	private Inflater inflater;
	// currently only do 'nowrap' -- RFC 1951, not zlib
	private boolean nowrap = true;
	private int compressionLevel;

	public DeflateCodec(int compressionLevel) {
		this.compressionLevel = compressionLevel;
	}

	@Override
	public String getName() {
		return "Deflate (" + compressionLevel + ")";
	}
	
	@Override
	public ByteBuffer compress(ByteBuffer data) throws IOException {
		ByteArrayOutputStream baos = getOutputBuffer(data.remaining());
		DeflaterOutputStream ios = new DeflaterOutputStream(baos, getDeflater());
		writeAndClose(data, ios);
		ByteBuffer result = ByteBuffer.wrap(baos.toByteArray());
		return result;
	}

	@Override
	public ByteBuffer uncompress(ByteBuffer data) throws IOException {
		ByteArrayOutputStream baos = getOutputBuffer(data.remaining());
		InflaterOutputStream ios = new InflaterOutputStream(baos, getInflater());
		writeAndClose(data, ios);
		ByteBuffer result = ByteBuffer.wrap(baos.toByteArray());
		return result;
	}

	@Override
	public byte[] compress(byte[] data) throws IOException {
		ByteArrayOutputStream baos = getOutputBuffer(data.length);
		DeflaterOutputStream ios = new DeflaterOutputStream(baos, getDeflater());
		writeAndClose(data, ios);
		return baos.toByteArray();
	}

	@Override
	public byte[] uncompress(byte[] data) throws IOException {
		ByteArrayOutputStream baos = getOutputBuffer(data.length);
		InflaterOutputStream ios = new InflaterOutputStream(baos, getInflater());
		writeAndClose(data, ios);
		return baos.toByteArray();
	}

	private void writeAndClose(ByteBuffer data, OutputStream to) throws IOException {
		byte[] input;
		int offset;
		if ( data.hasArray() ) {
			input = data.array();
			offset = data.arrayOffset() + data.position();
		} else {
			input = new byte[data.remaining()];
			data.get(input);
			data.rewind();
			offset = data.position();
		}
		int length = data.remaining();
		try {
			to.write(input, offset, length);
		} finally {
			to.close();
		}
	}

	private void writeAndClose(byte[] data, OutputStream to) throws IOException {
		try {
			to.write(data, 0, data.length);
		} finally {
			to.close();
		}
	}
	
	// get and initialize the inflater for use.
	private Inflater getInflater() {
		if (null == inflater) {
			inflater = new Inflater(nowrap);
		}
		inflater.reset();
		return inflater;
	}

	// get and initialize the deflater for use.
	private Deflater getDeflater() {
		if (null == deflater) {
			deflater = new Deflater(compressionLevel, nowrap);
		}
		deflater.reset();
		return deflater;
	}

	// get and initialize the output buffer for use.
	private ByteArrayOutputStream getOutputBuffer(int suggestedLength) {
		if (null == outputBuffer) {
			outputBuffer = new ByteArrayOutputStream(suggestedLength);
		}
		outputBuffer.reset();
		return outputBuffer;
	}

}
