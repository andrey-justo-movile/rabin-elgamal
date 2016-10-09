package elgamal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Elgamal {

    private static Random SR = new SecureRandom();

    public static BigInteger[] genKey(BigInteger secretKey, int length) {
        BigInteger p = BigInteger.probablePrime(length, SR);
        BigInteger b = new BigInteger("3"); // TODO: check it
        BigInteger c = b.modPow(secretKey, p);
        return new BigInteger[]{p,b,c};
    }

    public static BigInteger[] encrypt(BigInteger message, BigInteger c, BigInteger b, BigInteger p, int length) {
        BigInteger r = new BigInteger(length, SR);
        BigInteger EC = message.multiply(c.modPow(r, p)).mod(p);
        BigInteger h = b.modPow(r, p);

        return new BigInteger[]{EC, h};
    }

    public static BigInteger decrypt(BigInteger EC, BigInteger h, BigInteger secretKey, BigInteger p) {
        BigInteger c = h.modPow(secretKey, p);
        BigInteger d = c.modInverse(p);
        return d.multiply(EC).mod(p);
    }

}
