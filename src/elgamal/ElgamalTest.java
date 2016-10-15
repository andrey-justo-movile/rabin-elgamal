package elgamal;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;

public class ElgamalTest {

    public static void main(String[] args) throws UnsupportedEncodingException {
        BigInteger secretKey = null;
        if (args.length > 1) {
            secretKey =  new BigInteger(args[0]);
        } else {
            secretKey =  new BigInteger("1234567890"); // TODO: choose better default key
        }

        String originalText = "Hello world!";
        String decryptedText = "";

        BigInteger[] key = Elgamal.genKey(secretKey, 3072);
        BigInteger p = key[0];
        BigInteger b = key[1];
        BigInteger c = key[2];

        BigInteger message = new BigInteger(originalText.getBytes(Charset.forName("ascii")));
        BigInteger[] cipherText = Elgamal.encrypt(message, c, b, p, 3072);

        BigInteger EC = cipherText[0];
        BigInteger h = cipherText[1];
        
        BigInteger decryptedMessage = Elgamal.decrypt(EC, h, secretKey, p);
        decryptedText = new String(decryptedMessage.toByteArray(), Charset.forName("ascii"));

        System.out.println("For ->");
        System.out.println("Public keys -> p: " + p);
        System.out.println("Public keys -> q: " + b);
        System.out.println("Public key -> N: " + c);
        System.out.println("Original Text: " + originalText);
        System.out.println("Encrypted Text: " + EC);
        System.out.println("Results:");
        System.out.println("----------------------------");
        System.out.println("Decrypted Text: " + decryptedText);
    }
}