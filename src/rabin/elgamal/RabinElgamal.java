package rabin.elgamal;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RabinElgamal {

    private static Random SR = new SecureRandom();
    private static BigInteger THREE = BigInteger.valueOf(3);
    private static BigInteger FOUR = BigInteger.valueOf(4);

    public static BigInteger[] genKey(BigInteger x, int length) {
        BigInteger r = blumPrime(length);
        BigInteger g = blumPrime(length);
        BigInteger h = g.modPow(x, r);
        return new BigInteger[]{h, g, r};
    }

    public static BigInteger[] encrypt(BigInteger m, BigInteger h, BigInteger g, BigInteger r, int length) {
        BigInteger y = new BigInteger(length, SR);
        BigInteger c1 = g.modPow(y, r);
        BigInteger c2 = m.multiply(h.modPow(y, r)).mod(r);
        return new BigInteger[]{c1, c2};
    }
    
    public static BigInteger decrypt(BigInteger c1, BigInteger c2 , BigInteger secretKey, BigInteger r) {
        BigInteger c = c2.modPow(secretKey, r);
        BigInteger d = c.modInverse(r);
        return d.multiply(c1).mod(r);

//        BigInteger N = p.multiply(q);
//
//        BigInteger m_p = BigIntegerUtils.sqrt(c).mod(p);
//        BigInteger m_q = BigIntegerUtils.sqrt(c).mod(q);
//
//        BigInteger[] ext = ext_gcd(p,q);
//        BigInteger y_p = ext[1];
//        BigInteger y_q = ext[2];
//
//        BigInteger r = y_p.multiply(p).multiply(m_p).add(y_q.multiply(q).multiply(m_p)).mod(N);
//        BigInteger minusR = N.subtract(r);
//        BigInteger s = y_p.multiply(p).multiply(m_q).subtract(y_q.multiply(q).multiply(m_q)).mod(N);
//        BigInteger minusS = N.subtract(s);
//
//        return new BigInteger[]{r,minusR,s,minusS};
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

    public static BigInteger blumPrime(int length) {
        BigInteger p;
        do {
            p=BigInteger.probablePrime(length, SR);
        }
        while(!p.mod(FOUR).equals(THREE));
        return p;
    }

    
    
}
