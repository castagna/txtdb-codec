TxTDB Codecs
------------

These are small experiments with codecs to compress Journal entries for TxTDB.

I've stolen code and ideas from:

 - Apache Avro [1]
 - Tatu Saloranta's Ning Compress [2] and his benchmark [3]


      Snappy: 40960 blocks (8192 bytes each), uncompressed =    335544320, compressed =    107915859 (  32%), elapsed time  4524 ms = 0.1104 ms per block =    93 MB/sec
         LZF: 40960 blocks (8192 bytes each), uncompressed =    335544320, compressed =    120823862 (  36%), elapsed time  4359 ms = 0.1064 ms per block =   100 MB/sec
    NullCopy: 40960 blocks (8192 bytes each), uncompressed =    335544320, compressed =    335544320 ( 100%), elapsed time   756 ms = 0.0185 ms per block =   847 MB/sec
 Deflate (0): 40960 blocks (8192 bytes each), uncompressed =    335544320, compressed =    335749120 ( 100%), elapsed time  4040 ms = 0.0986 ms per block =   158 MB/sec
 Deflate (1): 40960 blocks (8192 bytes each), uncompressed =    335544320, compressed =     78211507 (  23%), elapsed time  9271 ms = 0.2263 ms per block =    43 MB/sec
 Deflate (2): 40960 blocks (8192 bytes each), uncompressed =    335544320, compressed =     75269034 (  22%), elapsed time  9441 ms = 0.2305 ms per block =    41 MB/sec
 Deflate (6): 40960 blocks (8192 bytes each), uncompressed =    335544320, compressed =     65762393 (  20%), elapsed time 17613 ms = 0.4300 ms per block =    22 MB/sec
 Deflate (9): 40960 blocks (8192 bytes each), uncompressed =    335544320, compressed =     64571697 (  19%), elapsed time 103470 ms = 2.5261 ms per block =     4 MB/sec



 [1] http://svn.apache.org/repos/asf/avro/trunk/lang/java/avro/src/main/java/org/apache/avro/file/Codec.java
 [2] https://github.com/ning/compress
 [3] https://github.com/ning/jvm-compressor-benchmark

                                                            -- Paolo Castagna
