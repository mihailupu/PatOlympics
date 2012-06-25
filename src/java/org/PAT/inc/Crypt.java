package org.PAT.inc;

public class Crypt {

    private static Object lock = new Object();

    private static long ePerm32Tab[][][] = new long[4][256][2];

    private static int pc1_1 = 128;
    private static int pc1_2 = 2;
    private static int pc1_3 = 8;

    private static int pc2_1 = 128;
    private static int pc2_2 = 8;

    private static long doPc1[] = new long[pc1_1*pc1_2*pc1_3];

    private static long doPc2[] = new long[pc2_1*pc2_2];

    private static long efp[][][] = new long[16][64][2];
    private static int byteMask[]={ 0x80, 0x40, 0x20, 0x10,
	0x08, 0x04, 0x02, 0x01 };

    private static int pc1[] = {
	57, 49, 41, 33, 25, 17,  9,  1, 58, 50, 42, 34, 26, 18,
	10,  2, 59, 51, 43, 35, 27, 19, 11,  3, 60, 52, 44, 36,
	63, 55, 47, 39, 31, 23, 15,  7, 62, 54, 46, 38, 30, 22,
	14,  6, 61, 53, 45, 37, 29, 21, 13,  5, 28, 20, 12,  4
    };

    private static int rots[] = { 1, 1, 2, 2, 2, 2, 2, 2, 1,
	2, 2, 2, 2, 2, 2, 1 };

    private static int pc2[] = {
	14, 17, 11, 24,  1,  5,  3, 28, 15,  6, 21, 10,
	23, 19, 12,  4, 26,  8, 16,  7, 27, 20, 13,  2,
	41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
	44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
    };

    private static int esel[] = {
	32,  1,  2,  3,  4,  5,  4,  5,  6,  7,  8,  9,
	8,  9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
	16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
	24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32,  1
    };

    private static int eInverse[] = new int[64];

    private static int perm32[] = {
	16,  7, 20, 21, 29, 12, 28, 17,  1, 15, 23, 26,  5, 18, 31, 10,
	2,   8, 24, 14, 32, 27,  3,  9, 19, 13, 30,  6, 22, 11,  4, 25
    };

    private static int sbox[][][] = {
    { { 14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7 },
      {  0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8 },
      {  4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0 },
      { 15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13 }},

    { { 15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10 },
      {  3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5 },
      {  0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15 },
      { 13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9 }},

    { { 10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8 },
      { 13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1 },
      { 13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7 },
      {  1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12 }},

    { {  7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15 },
      { 13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9 },
      { 10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4 },
      {  3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14 }},

    { {  2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9 },
      { 14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6 },
      {  4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14 },
      { 11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3 }},

    { { 12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11 },
      { 10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8 },
      {  9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6 },
      {  4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13 }},

    { {  4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1 },
      { 13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6 },
      {  1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2 },
      {  6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12 }},

    { { 13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7 },
      {  1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2 },
      {  7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8 },
      {  2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11 }}
    };

    /**
     * This is final permutation matrix.
     */
    private static int finalPerm[] = {
	40,  8, 48, 16, 56, 24, 64, 32, 39,  7, 47, 15, 55, 23, 63, 31,
	38,  6, 46, 14, 54, 22, 62, 30, 37,  5, 45, 13, 53, 21, 61, 29,
	36,  4, 44, 12, 52, 20, 60, 28, 35,  3, 43, 11, 51, 19, 59, 27,
	34,  2, 42, 10, 50, 18, 58, 26, 33,  1, 41,  9, 49, 17, 57, 25
    };

    private static long longMask[] = {
	0x80000000, 0x40000000, 0x20000000, 0x10000000,
	0x08000000, 0x04000000, 0x02000000, 0x01000000,
	0x00800000, 0x00400000, 0x00200000, 0x00100000,
	0x00080000, 0x00040000, 0x00020000, 0x00010000,
	0x00008000, 0x00004000, 0x00002000, 0x00001000,
	0x00000800, 0x00000400, 0x00000200, 0x00000100,
	0x00000080, 0x00000040, 0x00000020, 0x00000010,
	0x00000008, 0x00000004, 0x00000002, 0x00000001
    };


    /**
     * The 16 DES keys in BITMASK format
     */
    private static long ufcKeytab[] = new long[16];

    private static long sb[][] = new long[4][4096];

    private static boolean inited = false;
    private static byte currentSalt[] = new byte[2];
    private static int currentSaltbits;

    public static boolean selfTest(boolean show) {

	String required = "arlEKn0OzVJn.";

	if (inited) {
	    if (show) {
		System.err.println("Warning: initialization already done.");
	    }
	} else {
	    initDes();
	}

	String cryptTest = crypt("ar","foob");

	boolean failed = !required.equals(cryptTest);
	if (failed) {
	    if (show) {
		System.err.println("TEST FAILED! Got:"+cryptTest+" Wanted"+
			required);
	    }
	    return false;

	} else {
	    if (show) {
		System.err.println("Test succeeded");
	    }
	}

	return true;

    }

    public static String crypt(byte[] salt, byte[] original) {
	String originalString = new String(original);
	return crypt(salt, originalString);
    }

    public static String crypt(String salt, String original) {
	return crypt(salt.getBytes(), original);
    }

    public static String crypt(byte[] salt, String original) {

	if (!inited)
	    initDes();

	String res;

	synchronized (lock) {

	    byte ktab[] = new byte[9];

	    for (int i=0; i<9; i++) {
		ktab[i]=0;
	    }

	    setupSalt(salt);

	    int orig=original.length();
	    for (int i=0; i<(orig>8?9:orig); i++) {
		ktab[i] = (byte)original.charAt(i);
	    }

	    ufcMkKeytab(ktab);

	    int s[] = ufcDoit(0,0,0,0,25);

	    res = outputConversion(s[0], s[1], salt);
	}

	return res;

    }

    /**
     * Crypt only: convert from 64 bit to 11 bit ASCII
     */
    private static String outputConversion(int v1, int v2, byte[] salt) {
	char outbuf[] = new char[13];

	outbuf[0] = (char)salt[0];
	outbuf[1] = (char)((salt[1]!=0)?salt[1]:salt[0]);

	for (int i = 0; i < 5; i++) {
	    outbuf[i+2] = binToAscii((v1 >>> (26 - 6 * i)) & 0x3f);
	}

	int s = (v2 & 0xf) << 2;
	v2 = (v2 >>> 2) | ((v1 & 0x3) << 30);

	for (int i = 5; i < 10; i++) {
	    outbuf[i+2] = binToAscii((v2>>>(56 - 6 * i)) & 0x3f);
	}

	outbuf[12] = binToAscii(s);

	return new String(outbuf);

    }

    /**
     * Lookup a 6 bit value in sbox
     */
    private static int sLookup(int i, int s) {
	return sbox[i] [((s>>>4)&0x02)|(s&0x1)] [(s>>>1)&0xf];
    }

    /**
     * Function to set a bit (0..23)
     */
    private static long bitMask(long i) {
	long l = (1<<(11-(i)%12+3));
	long r = ((i)<12?16:0);
	return (l<<r);
    }

    public static void initDes() {

	synchronized (lock) {

	inited = true;
	for (int bit = 0; bit < 56; bit++) {
	    int comesFromBit = pc1[bit] - 1;
	    long mask1 = byteMask[comesFromBit % 8 + 1];
	    long mask2 = longMask[bit % 28 + 4];
	    for (int j=0; j<128; j++) {
		if ((j & mask1) != 0) {
		    doPc1[j + (bit/28)*pc1_1 +
			(comesFromBit / 8)*pc1_1*pc1_2] |= mask2;
		}
	    }
	}


	for (int bit = 0; bit < 48; bit++) {
	    int comesFromBit = pc2[bit]-1;
	    long mask1 = byteMask[comesFromBit % 7 + 1];
	    long mask2 = bitMask(bit % 24);
	    for (int j = 0; j < 128; j++) {
		if ((j & mask1) != 0) {
		    doPc2[j + (comesFromBit / 7) * pc2_1] |= mask2;
		}
	    }
	}

	for (int i=0; i<4; i++)
	    for (int j=0; j<256; j++)
		for (int k=0; k<2; k++)
		    ePerm32Tab[i][j][k] = 0;

	for (int bit=0; bit< 48; bit++) {

	    int comesFrom = perm32[esel[bit]-1]-1;
	    int mask1     = byteMask[comesFrom % 8];

	    for (int j=255; j>=0; j--) {
		if ((j & mask1) != 0) {
		    ePerm32Tab[comesFrom/8][j][bit/24] |= bitMask(bit%24);
		}
	    }
	}

	for (int sg = 0; sg<4; sg++) {
	    for (int j1 = 0; j1 < 64; j1++) {
		int s1 = sLookup(2*sg, j1);
		for (int j2 = 0; j2 < 64; j2++) {
		    int s2 = sLookup(2*sg+1,j2);
		    int toPermute = ((s1 << 4) | s2) << (24 - 8 * sg);

		    int inx = ((j1<<6)|j2);

		    sb[sg][inx] =(ePerm32Tab[0][(toPermute>>>24)&0xff][0]<<32)|
			         ePerm32Tab[0][(toPermute>>>24)&0xff][1];
		    sb[sg][inx]|=(ePerm32Tab[1][(toPermute>>>16)&0xff][0]<<32)|
			         ePerm32Tab[1][(toPermute>>>16)&0xff][1];
		    sb[sg][inx]|=(ePerm32Tab[2][(toPermute>>> 8)&0xff][0]<<32)|
			         ePerm32Tab[2][(toPermute>>> 8)&0xff][1];
		    sb[sg][inx]|=(ePerm32Tab[3][(toPermute    )&0xff][0]<<32)|
			         ePerm32Tab[3][(toPermute    )&0xff][1];

		}
	    }
	}

	for (int bit=47; bit>=0; bit--) {
	    eInverse[esel[bit] - 1     ] = bit;
	    eInverse[esel[bit] - 1 + 32] = bit + 48;
	}

	for (int i=0; i < 16; i++)
	    for (int j=0; j < 64 ; j++)
		for (int k=0; k < 2; k++)
		    efp[i][j][k] = 0;

	for (int bit=0; bit < 64; bit++) {
	    int oLong = bit / 32;
	    int oBit = bit % 32;

	    int comesFromFBit = finalPerm[bit] - 1;
	    int comesFromEBit = eInverse[comesFromFBit];
	    int comesFromWord = comesFromEBit / 6;
	    int bitWithinWord = comesFromEBit % 6;

	    long mask1 = longMask[bitWithinWord + 26];
	    long mask2 = longMask[oBit];

	    for (int wordValue = 63; wordValue>=0; wordValue--) {
		if ((wordValue & mask1) != 0) {
		    efp[comesFromWord][wordValue][oLong] |= mask2;
		}
	    }
	}

	}

    }

    private static void setupSalt(byte[] s) {

	if ((s[0] == currentSalt[0]) && (s[1] == currentSalt[1]))
	    return;

	currentSalt[0] = s[0];
	currentSalt[1] = s[1];

	int saltbits = 0;
	for (int i=0; i<2; i++) {
	    long c = asciiToBin(s[i]);
	    if (c<0 || c>63) {
		c = 0;
	    }

	    for (int j=0; j < 6; j++) {
		if (((c>>>j)&0x1) != 0) {
		    saltbits |= bitMask(6*i+j);
		}
	    }
	}

	shuffleSb(sb[0], currentSaltbits ^ saltbits);
	shuffleSb(sb[1], currentSaltbits ^ saltbits);
	shuffleSb(sb[2], currentSaltbits ^ saltbits);
	shuffleSb(sb[3], currentSaltbits ^ saltbits);

	currentSaltbits = saltbits;
    }

    private static void shuffleSb(long[] k, int saltbits) {
	int i = 0;
	for (int j=4095; j>=0; j--) {
	    long x=((k[i]>>>32) ^ k[i]) & saltbits;
	    k[i++] ^= (x << 32) | x;
	}
    }

    private static long asciiToBin(byte c) {
	return (c>='a'?(c-59):c>='A'?(c-53):c-'.');
    }

    private static char binToAscii(long c) {
	return (char)(c>=38?(c-38+'a'):c>=12?(c-12+'A'):c+'.');
    }

    private static void ufcMkKeytab(byte[] keyInd) {
		int k1 = 0;
		int k2 = 0;
		int key = 0;
		
		long v1 = 0;
		long v2 = 0;
		
		for (int i=7; i>=0; i--) {
		    v1 |= doPc1[(int)(k1 + (keyInd[key  ] & 0x7f))]; k1 += 128;
		    v2 |= doPc1[(int)(k1 + (keyInd[key++] & 0x7f))]; k1 += 128;
		}
		
		for (int i=0; i< 16; i++) {
		    k1 = 0;
		
		    v1 = (v1 << rots[i]) | (v1 >>> (28 - rots[i]));
		    long v;
		    v  = doPc2[k1+(int)((v1 >>> 21) & 0x7f)]; k1 += 128;
		    v |= doPc2[k1+(int)((v1 >>> 14) & 0x7f)]; k1 += 128;
		    v |= doPc2[k1+(int)((v1 >>>  7) & 0x7f)]; k1 += 128;
		    v |= doPc2[k1+(int)((v1      ) & 0x7f)]; k1 += 128;
		
		    v <<= 32;
		
		    v2 = (v2 << rots[i]) | (v2 >>> (28 - rots[i]));
		    v |= doPc2[k1+(int)((v2 >>> 21) & 0x7f)]; k1 += 128;
		    v |= doPc2[k1+(int)((v2 >>> 14) & 0x7f)]; k1 += 128;
		    v |= doPc2[k1+(int)((v2 >>>  7) & 0x7f)]; k1 += 128;
		    v |= doPc2[k1+(int)((v2      ) & 0x7f)];
		
		    ufcKeytab[k2] = v;
		    k2++;
		}
    }

    private static long sba(long[] sb, long v) {
	if (((v>>>3)<<3) != v) {
	    return 0;
	}
	return sb[(int)(v>>>3)];
    }

    /**
     * Undo an extra E selection and do final permutations.
     */
    private static int[] ufcDoFinalPerm(int l1, int l2, int r1, int r2) {

	int v1,v2,x;
	int ary[] = new int[2];

	x = (l1 ^ l2) & currentSaltbits; l1 ^= x; l2 ^= x;
	x = (r1 ^ r2) & currentSaltbits; r1 ^= x; r2 ^= x;

	v1=0;
	v2=0;

	l1 >>>= 3;
	l2 >>>= 3;
	r1 >>>= 3;
	r2 >>>= 3;

	v1 |= efp[15][ r2          & 0x3f][0]; v2 |= efp[15][ r2 & 0x3f][1];
	v1 |= efp[14][(r2 >>>= 6)  & 0x3f][0]; v2 |= efp[14][ r2 & 0x3f][1];
	v1 |= efp[13][(r2 >>>= 10) & 0x3f][0]; v2 |= efp[13][ r2 & 0x3f][1];
	v1 |= efp[12][(r2 >>>= 6)  & 0x3f][0]; v2 |= efp[12][ r2 & 0x3f][1];

	v1 |= efp[11][ r1          & 0x3f][0]; v2 |= efp[11][ r1 & 0x3f][1];
	v1 |= efp[10][(r1 >>>= 6)  & 0x3f][0]; v2 |= efp[10][ r1 & 0x3f][1];
	v1 |= efp[ 9][(r1 >>>= 10) & 0x3f][0]; v2 |= efp[ 9][ r1 & 0x3f][1];
	v1 |= efp[ 8][(r1 >>>= 6)  & 0x3f][0]; v2 |= efp[ 8][ r1 & 0x3f][1];

	v1 |= efp[ 7][ l2          & 0x3f][0]; v2 |= efp[ 7][ l2 & 0x3f][1];
	v1 |= efp[ 6][(l2 >>>= 6)  & 0x3f][0]; v2 |= efp[ 6][ l2 & 0x3f][1];
	v1 |= efp[ 5][(l2 >>>= 10) & 0x3f][0]; v2 |= efp[ 5][ l2 & 0x3f][1];
	v1 |= efp[ 4][(l2 >>>= 6)  & 0x3f][0]; v2 |= efp[ 4][ l2 & 0x3f][1];

	v1 |= efp[ 3][ l1          & 0x3f][0]; v2 |= efp[ 3][ l1 & 0x3f][1];
	v1 |= efp[ 2][(l1 >>>= 6)  & 0x3f][0]; v2 |= efp[ 2][ l1 & 0x3f][1];
	v1 |= efp[ 1][(l1 >>>= 10) & 0x3f][0]; v2 |= efp[ 1][ l1 & 0x3f][1];
	v1 |= efp[ 0][(l1 >>>= 6)  & 0x3f][0]; v2 |= efp[ 0][ l1 & 0x3f][1];

	ary[0] = v1;
	ary[1] = v2;

	return ary;
    }

    private static int[] ufcDoit(int l1, int l2, int r1, int r2, int itr) {

	long l = (((long)l1) << 32) | ((long)l2);
	long r = (((long)r1) << 32) | ((long)r2);

	while (itr-- != 0) {
	    int k = 0;
	    for (int i=7; i>=0; i--) {
		long s = ufcKeytab[k++] ^ r;
		l ^= sba(sb[3], (s >>>  0) & 0xffff);
		l ^= sba(sb[2], (s >>> 16) & 0xffff);
		l ^= sba(sb[1], (s >>> 32) & 0xffff);
		l ^= sba(sb[0], (s >>> 48) & 0xffff);

		s = ufcKeytab[k++] ^ l;
		r ^= sba(sb[3], (s >>>  0) & 0xffff);
		r ^= sba(sb[2], (s >>> 16) & 0xffff);
		r ^= sba(sb[1], (s >>> 32) & 0xffff);
		r ^= sba(sb[0], (s >>> 48) & 0xffff);

	    }

	    long s = l;
	    l = r;
	    r = s;

	}

	l1 = (int)(l >>> 32); l2 = (int)(l & 0xffffffff);
	r1 = (int)(r >>> 32); r2 = (int)(r & 0xffffffff);
	return ufcDoFinalPerm(l1,l2,r1,r2);
    }
}
