import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Rabin {
    private static Random SR = new SecureRandom();
    private static BigInteger TWO = BigInteger.valueOf(2);
    private static BigInteger THREE = BigInteger.valueOf(3);
    private static BigInteger FOUR = BigInteger.valueOf(4);

    /**
     * Generate a blum public and private key (more efficient decryption) with a specified number of bits
     * @param bitLength Number of bits in public key
     * @return An array of BigIntegers {N,p,q}. N is the public key and p and q are the private keys
     */
    public static BigInteger[] genKey(int bitLength) {
        BigInteger p = blumPrime(bitLength/2);
        BigInteger q = blumPrime(bitLength/2);
        BigInteger N = p.multiply(q);
        return new BigInteger[]{N,p,q};
    }

    /**
     * Encrypt a value with the public key
     * @param m the value to encrypt
     * @param N the public key, N
     * @return c, the encrypted value
     */
    public static BigInteger encrypt(BigInteger m, BigInteger N) {
        return m.modPow(TWO, N);
    }

    /**
     * Decrypt a value with the private key (assumes blum key for fast decryption)
     * @param c encrypted number
     * @param p private key, p
     * @param q private key, q
     * @return array of the 4 decryption possibilities
     */
    public static BigInteger[] decrypt(BigInteger c, BigInteger p , BigInteger q) {
        BigInteger N = p.multiply(q);

        BigInteger m_p = BigIntegerUtils.sqrt(c).mod(p);
        BigInteger m_q = BigIntegerUtils.sqrt(c).mod(q);

        BigInteger[] ext = ext_gcd(p,q);
        BigInteger y_p = ext[1];
        BigInteger y_q = ext[2];

        BigInteger r = y_p.multiply(p).multiply(m_p).add(y_q.multiply(q).multiply(m_p)).mod(N);
        BigInteger minusR = N.subtract(r);
        BigInteger s = y_p.multiply(p).multiply(m_q).subtract(y_q.multiply(q).multiply(m_q)).mod(N);
        BigInteger minusS = N.subtract(s);

        return new BigInteger[]{r,minusR,s,minusS};
    }


    public static BigInteger[] ext_gcd(BigInteger a, BigInteger b) {
        BigInteger s = BigInteger.ZERO;
        BigInteger old_s = BigInteger.ONE;
        BigInteger t = BigInteger.ONE;
        BigInteger old_t = BigInteger.ZERO;
        BigInteger r = b;
        BigInteger old_r = a;
        while(!r.equals(BigInteger.ZERO)) {
            BigInteger q = old_r.divide(r);
            BigInteger tr = r;
            r = old_r.subtract(q.multiply(r));
            old_r=tr;

            BigInteger ts = s;
            s = old_s.subtract(q.multiply(s));
            old_s=ts;

            BigInteger tt = t;
            t = old_t.subtract(q.multiply(t));
            old_t=tt;
        }
        //gcd, x,y
        //x,y such that ax+by=gcd(a,b)
        return new BigInteger[]{old_r, old_s, old_t};
    }

    /**
     * Generate a random blum prime ( a prime such that p≡3 (mod 4) )
     * @param bitLength number of bits in the prime
     * @return a random blum prime
     */
    public static BigInteger blumPrime(int bitLength) {
        BigInteger p;
        do {
            p=BigInteger.probablePrime(bitLength, SR);
        }
        while(!p.mod(FOUR).equals(THREE));
        return p;
    }

}