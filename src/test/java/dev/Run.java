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
			new DeflateCodec(1),
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
		System.out.printf("%12s: %d blocks (%d bytes each), uncompressed = %12d, compressed = %12d (%4.0f%%), elapsed time %5d ms = %5.4f ms per block = %5.0f MB/sec\n",
				codec.getName(), 
				i, 
				SystemTDB.BlockSize, 
				bytes_total_uncompressed, 
				bytes_total_compressed,
				bytes_total_compressed * 1.0 / bytes_total_uncompressed * 100, 
				(stop-start),
				(stop-start) * 1.0 / i,
				( bytes_total_uncompressed + bytes_total_compressed ) * 1.0 / ( (stop-start) * 1.0 / 1000 ) / ( 1024 * 1024 )
		);		
	}
	
}
