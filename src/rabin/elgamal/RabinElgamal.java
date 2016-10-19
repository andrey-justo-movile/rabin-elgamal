package rabin.elgamal;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rabin.Rabin;

public class RabinElgamal {

    private static Random SR = new SecureRandom();

    public static BigInteger[] genKey(BigInteger secretKey, int length) {
        BigInteger p = BigInteger.probablePrime(length, SR);
        BigInteger b = generateGroup(p, length);
        BigInteger c = b.modPow(secretKey, p);
        
        BigInteger[] rabinKeys = Rabin.genKey(length * 2);
        
        return new BigInteger[]{p,b,c, rabinKeys[0], rabinKeys[1], rabinKeys[2]};
    }

    public static BigInteger[] encrypt(BigInteger message, BigInteger c, BigInteger b, BigInteger p, BigInteger N, int length) {
        BigInteger r = new BigInteger(length, SR);
        BigInteger EC = message.multiply(c.modPow(r, p)).mod(p);
        BigInteger h = b.modPow(r, p);

        return new BigInteger[]{EC, Rabin.encrypt(h, N)};
    }

    public static BigInteger[] decrypt(BigInteger EC, BigInteger h, BigInteger secretKey, BigInteger p, BigInteger pRabin, BigInteger qRabin) {
        BigInteger[] rabinDecrypts = Rabin.decrypt(h, pRabin, qRabin);
        
        List<BigInteger> decrypts = new ArrayList<>();
        for (BigInteger possibleDecryptedRabin: rabinDecrypts) {
            BigInteger c = possibleDecryptedRabin.modPow(secretKey, p);
            BigInteger d = c.modInverse(p);
            decrypts.add(d.multiply(EC).mod(p));
        }
        
        return decrypts.toArray(new BigInteger[4]);
    }
    
    public static BigInteger generateGroup(BigInteger p, int length) {
        BigInteger g = null;
        do {
            g = BigInteger.probablePrime(length, SR);
        } while(g == null || p.subtract(g).signum() != 1);
            
        return g;
    }
    
}
