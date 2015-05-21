import random
import struct 

oFile = open( 'zorder.bin', 'wb')

random.seed(1)


#67108864 for a 1 GB file
#32768000 for a 500 MB file
#13107200 for a 2 MB file
#65536 for a 1 MB file
for k in range(65536  ):
        n1 = random.randint( 0, 100000000)
        n2 = random.randint( 0, 100000000)
    
        a = ''
        a = a+ struct.pack( 'q', n1 )
        a = a+ struct.pack( 'q', n2 )
    
        oFile.write( a )

oFile.close()

