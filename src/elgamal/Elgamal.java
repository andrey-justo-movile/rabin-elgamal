package elgamal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Elgamal {

    private static Random SR = new SecureRandom();

    public static BigInteger[] genKey(BigInteger secretKey, int length) {
        BigInteger p = BigInteger.probablePrime(length, SR);
        BigInteger b = generateGroup(p, length);
        BigInteger c = b.modPow(secretKey, p);
        return new BigInteger[]{p,b,c};
    }

    public static BigInteger[] encrypt(BigInteger message, BigInteger c, BigInteger b, BigInteger p, int length) {
        BigInteger r = new BigInteger(length, SR).mod(b);
        BigInteger EC = message.multiply(c.modPow(r, p));
        BigInteger h = b.modPow(r, p);

        return new BigInteger[]{EC, h};
    }

    public static BigInteger decrypt(BigInteger EC, BigInteger h, BigInteger secretKey, BigInteger p) {
        BigInteger c = h.modPow(secretKey, p);
        BigInteger d = c.modInverse(p);
        return d.multiply(EC).mod(p);
    }
    
    public static BigInteger generateGroup(BigInteger p, int length) {
        BigInteger g = null;
        do {
            g = BigInteger.probablePrime(length, SR);
        } while(g == null || p.subtract(g).signum() != 1);
            
        return g;
    }

}
