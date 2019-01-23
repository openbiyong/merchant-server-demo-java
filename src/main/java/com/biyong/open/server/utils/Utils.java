package com.biyong.open.server.utils;

import com.alibaba.fastjson.JSON;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.DestroyFailedException;

/**
 * BiYong 商户 API
 * version: java-1.0.0
 *
 * 参考文档
 *
 * https://en.wikipedia.org/wiki/RSA_(cryptosystem)
 * https://en.wikipedia.org/wiki/Advanced_Encryption_Standard
 * https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html
 */
public class Utils {

  /**
   * 随机生成 byte[16] msgId 的方法，商户可自己实现
   */
  private static byte[] newMsgId() {
    return fromHex(UUID.randomUUID().toString().replace("-", ""));
  }

  /**
   * 生成秘钥对的方法
   */
  private static void createRsaKeyPair() {
    RSA.KeyPair keyPair = RSA.KeyPair.create(2048); //推荐使用2048位RSA私钥
    System.out.println("私钥:");
    System.out.println(keyPair.getPrivate().toBase64String());
    System.out.println("公钥:");
    System.out.println(keyPair.getPublic().toBase64String());
  }

  public static class MerchantRequest {
    long timestamp;
    byte[] msgId;
    AES.KEY aes;
    byte[] data;

    public long getTimestamp() {
      return timestamp;
    }

    public byte[] getMsgId() {
      return msgId;
    }

    public AES.KEY getAesKey() {
      return aes;
    }

    public byte[] getData() {
      return data;
    }
  }

  public static class MessageCipher {
    RSA.PrivateKey privateKey;
    RSA.PublicKey publicKey;
    String rsaSignHashMode;
    String aesMode;

    public MessageCipher(
        String privateKey,
        String publicKey,
        String rsaSignHashMode,
        String aesMode) {
      if (privateKey != null) {
        this.privateKey = RSA.PrivateKey.fromBase64String(privateKey);
      }
      if (publicKey != null) {
        this.publicKey = RSA.PublicKey.fromBase64String(publicKey);
      }
      this.rsaSignHashMode = rsaSignHashMode;
      this.aesMode = aesMode;
    }

    public MerchantRequest clientEncrypt(Object data) {
      byte[] signedData = sign(
          concat(
              longToBytes(System.currentTimeMillis()),
              newMsgId(),
              (data == null ? "{}" : JSON.toJSONString(data)).getBytes(CHARSET_UTF_8)));
      return clientAesEncrypt(signedData);
    }

    public byte[] clientDecrypt(byte[] data, AES.KEY aes) {
      if (data[0] != 0) {
        return data;
      }
      if (aesMode != null) {
        data = aesDecrypt(lastBytes(data.length - 1, data), aes).getData();
      }
      data = verify(data);
      if (data == null) {
        throw new CipherError("验签失败!");
      } else {
        return lastBytes(data.length - 16, data);
      }
    }

    public byte[] serverEncrypt(String requestBody, byte[] msgId, AES.KEY aes) {
      byte[] signedData = sign(
          concat(
              msgId,
              requestBody.getBytes(CHARSET_UTF_8)));
      return aesMode == null ? signedData : concat(new byte[1], aesEncrypt(signedData, aes));
    }

    public MerchantRequest serverDecrypt(byte[] data) {
      MerchantRequest r = serverAesDecrypt(data);
      r.data = verify(r.data);
      if (r.data != null) {
        r.timestamp = bytesToLong(r.data);
        r.msgId = new byte[16];
        System.arraycopy(r.data, 8, r.msgId, 0, 16);
        r.data = lastBytes(r.data.length - 24, r.data);
      }
      return r;
    }

    byte[] sign(byte[] data) {
      byte[] sign = privateKey.sign(data, rsaSignHashMode);
      return concat(intToBytes(sign.length), sign, data);
    }

    byte[] verify(byte[] data) {
      byte[] sign = getDataBy4BytesLength(data);
      data = lastBytes(data.length - 4 - sign.length, data);
      return publicKey.verify(data, sign, rsaSignHashMode) ? data : null;
    }

    MerchantRequest clientAesEncrypt(byte[] signedData) {
      MerchantRequest r = new MerchantRequest();
      if (aesMode == null) {
        r.data = signedData;
      } else {
        r.aes = new AES.KEY(signedData, 4);
        byte[] aesEncryptedData = aesEncrypt(signedData, r.aes);
        byte[] rsaEncryptedAesKeyIv = publicKey.encrypt(concat(r.aes.key, r.aes.iv));
        r.data = concat(intToBytes(rsaEncryptedAesKeyIv.length), rsaEncryptedAesKeyIv, aesEncryptedData);
      }
      return r;
    }

    MerchantRequest serverAesDecrypt(byte[] encryptedData) {
      if (aesMode == null) {
        // 未加密
        MerchantRequest r = new MerchantRequest();
        r.data = encryptedData;
        return r;
      } else {
        byte[] rsaEncryptedAesKeyIv = getDataBy4BytesLength(encryptedData);
        byte[] aesEncryptedData = lastBytes(
            encryptedData.length - 4 - rsaEncryptedAesKeyIv.length, encryptedData);
        return aesDecrypt(aesEncryptedData, new AES.KEY(privateKey.decrypt(rsaEncryptedAesKeyIv)));
      }
    }


    byte[] aesEncrypt(byte[] signedData, AES.KEY aes) {
      return AES.createEncoder(aes.key, aes.iv, aesMode).encrypt(signedData);
    }

    MerchantRequest aesDecrypt(byte[] aesEncryptedData, AES.KEY aes) {
      MerchantRequest r = new MerchantRequest();
      r.aes = aes;
      r.data = AES.createDecoder(r.aes.key, r.aes.iv, aesMode).decrypt(aesEncryptedData);
      return r;
    }
  }

  public static class AES {
    static final Set<String> allowMode;
    static final Set<String> allowPadding;

    static {
      allowMode = new HashSet<>();
      allowMode.add("CFB");
      allowMode.add("CTR");
      allowMode.add("OFB");
      allowMode.add("PCBC");
      allowMode.add("CBC");
      allowPadding = new HashSet<>();
      allowPadding.add("NoPadding");
      allowPadding.add("PKCS5Padding");
      allowPadding.add("ISO10126Padding");
    }

    public static boolean isAllowMode(String mode) {
      String[] m = mode.split("/");
      if (m.length != 2) {
        return false;
      }
      if (m[0].equals("CTR") && m[1].equals("ISO10126Padding") ||
          (m[1].equals("NoPadding") && (m[0].equals("PCBC") || m[0].equals("CBC")))) {
        return false;
      }
      return allowMode.contains(m[0]) && allowPadding.contains(m[1]);
    }

    public static class KEY {
      private byte[] key;
      private byte[] iv;

      public byte[] getKey() {
        return key;
      }

      public byte[] getIv() {
        return iv;
      }

      KEY(byte[] data) {
        this(data, 0);
      }

      KEY(byte[] data, int l) {
        key = new byte[16];
        iv = new byte[16];
        System.arraycopy(data, l, key, 0, 16);
        System.arraycopy(data, l + 16, iv, 0, 16);
      }
    }

    private static class AESCipher {
      private Cipher cipher;

      AESCipher(int mode, byte[] key, byte[] iv, String type) {
        try {
          cipher = Cipher.getInstance("AES/" + type);
          cipher.init(mode, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        } catch (Exception e) {
          throw new CipherError(e.getMessage());
        }
      }

      synchronized byte[] doFinal(byte[] content) {
        try {
          return cipher.doFinal(content);
        } catch (Exception e) {
          throw new CipherError(e.getMessage());
        }
      }
    }

    public static Encoder createEncoder(byte[] key, byte[] iv, String type) {
      return new Encoder(key, iv, type);
    }

    public static Decoder createDecoder(byte[] key, byte[] iv, String type) {
      return new Decoder(key, iv, type);
    }

    public static class Encoder extends AESCipher {
      private Encoder(byte[] key, byte[] iv, String type) {
        super(Cipher.ENCRYPT_MODE, key, iv, type);
      }

      public byte[] encrypt(byte[] content) {
        return doFinal(content);
      }

      public byte[] encrypt(String content) {
        return encrypt(content.getBytes(CHARSET_UTF_8));
      }
    }


    public static class Decoder extends AESCipher {
      private Decoder(byte[] key, byte[] iv, String type) {
        super(Cipher.DECRYPT_MODE, key, iv, type);
      }

      public byte[] decrypt(byte[] content) {
        return doFinal(content);
      }

      public String decryptToString(byte[] content) {
        return new String(decrypt(content), CHARSET_UTF_8);
      }
    }
  }

  public static class RSA {
    public static final Set<String> allowHashMode;

    static {
      allowHashMode = new HashSet<>();
      allowHashMode.add("SHA512");
      allowHashMode.add("SHA384");
      allowHashMode.add("SHA256");
      allowHashMode.add("SHA224");
      allowHashMode.add("SHA1");
      allowHashMode.add("MD5");
      allowHashMode.add("MD2");
    }

    public static boolean isAllowHashMode(String mode) {
      return allowHashMode.contains(mode);
    }

    public static class KeyPair {
      private PrivateKey privateKey;
      private PublicKey publicKey;

      public static KeyPair convert(java.security.KeyPair keyPair) {
        return new KeyPair(keyPair);
      }

      public static KeyPair create(int keySize) {
        try {
          KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
          keyPairGenerator.initialize(keySize);
          return KeyPair.convert(keyPairGenerator.genKeyPair());
        } catch (NoSuchAlgorithmException e) {
          throw new CipherError(e.getMessage());
        }
      }

      public static KeyPair fromBase64PrivateKey(String key) {
        try {
          KeyPair keyPair = new KeyPair();
          keyPair.privateKey = RSA.PrivateKey.fromBase64String(key);
          KeyFactory kf = KeyFactory.getInstance("RSA");
          RSAPublicKeySpec keySpec = new RSAPublicKeySpec(
              kf.getKeySpec(keyPair.privateKey.privateKey, RSAPrivateKeySpec.class).getModulus(),
              BigInteger.valueOf(65537));
          java.security.PublicKey publicKey = kf.generatePublic(keySpec);
          keyPair.publicKey = RSA.PublicKey.convert(publicKey);
          return keyPair;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
          throw new CipherError(e.getMessage());
        }
      }

      KeyPair() {
      }

      KeyPair(java.security.KeyPair keyPair) {
        this.publicKey = PublicKey.convert(keyPair.getPublic());
        this.privateKey = PrivateKey.convert(keyPair.getPrivate());
      }

      public PublicKey getPublic() {
        return publicKey;
      }

      public PrivateKey getPrivate() {
        return privateKey;
      }
    }

    public static class PrivateKey implements java.security.PrivateKey {
      private java.security.PrivateKey privateKey;

      public static PrivateKey convert(java.security.PrivateKey privateKey) {
        PrivateKey key = new PrivateKey();
        key.privateKey = privateKey;
        return key;
      }

      public static PrivateKey fromBase64String(String key) {
        try {
          PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
          KeyFactory kf = KeyFactory.getInstance("RSA");
          return convert(kf.generatePrivate(keySpec));
        } catch (Exception e) {
          throw new CipherError(e.getMessage());
        }
      }

      public String toBase64String() {
        return Base64.getEncoder().encodeToString(getEncoded());
      }

      public byte[] sign(byte[] data, String hashMode) {
        return RSA.sign(privateKey, data, hashMode);
      }

      public byte[] encrypt(byte[] data) {
        return RSA.encrypt(privateKey, data);
      }

      public byte[] decrypt(byte[] data) {
        return RSA.decrypt(privateKey, data);
      }

      @Override
      public String getAlgorithm() {
        return privateKey.getAlgorithm();
      }

      @Override
      public String getFormat() {
        return privateKey.getFormat();
      }

      @Override
      public byte[] getEncoded() {
        return privateKey.getEncoded();
      }

      @Override
      public void destroy() throws DestroyFailedException {
        privateKey.destroy();
      }

      @Override
      public boolean isDestroyed() {
        return privateKey.isDestroyed();
      }
    }

    public static class PublicKey implements java.security.PublicKey {
      private java.security.PublicKey publicKey;

      public static PublicKey convert(java.security.PublicKey publicKey) {
        PublicKey key = new PublicKey();
        key.publicKey = publicKey;
        return key;
      }

      public static PublicKey fromBase64String(String key) {
        try {
          X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
          KeyFactory kf = KeyFactory.getInstance("RSA");
          return convert(kf.generatePublic(spec));
        } catch (Exception e) {
          throw new CipherError(e.getMessage());
        }
      }

      public String toBase64String() {
        return Base64.getEncoder().encodeToString(getEncoded());
      }

      public boolean verify(byte[] data, byte[] signature, String hashMode) {
        return RSA.verify(publicKey, data, signature, hashMode);
      }

      public byte[] encrypt(byte[] data) {
        return RSA.encrypt(publicKey, data);
      }

      public byte[] decrypt(byte[] data) {
        return RSA.decrypt(publicKey, data);
      }

      @Override
      public String getAlgorithm() {
        return publicKey.getAlgorithm();
      }

      @Override
      public String getFormat() {
        return publicKey.getFormat();
      }

      @Override
      public byte[] getEncoded() {
        return publicKey.getEncoded();
      }
    }

    private static byte[] sign(java.security.PrivateKey key, byte[] data, String hashMode) {
      try {
        Signature sig = Signature.getInstance(hashMode + "WithRSA");
        sig.initSign(key);
        sig.update(data);
        return sig.sign();
      } catch (Exception e) {
        throw new CipherError(e.getMessage());
      }
    }

    private static boolean verify(
        java.security.PublicKey key, byte[] data, byte[] signature, String hashMode) {
      try {
        Signature sig = Signature.getInstance(hashMode + "WithRSA");
        sig.initVerify(key);
        sig.update(data);
        return sig.verify(signature);
      } catch (Exception e) {
        throw new CipherError(e.getMessage());
      }
    }

    private static byte[] encrypt(java.security.Key key, byte[] encrypted) {
      try {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(encrypted);
      } catch (Exception e) {
        throw new CipherError(e.getMessage());
      }
    }

    private static byte[] decrypt(java.security.Key key, byte[] encrypted) {
      try {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encrypted);
      } catch (Exception e) {
        throw new CipherError(e.getMessage());
      }
    }
  }

  private static byte[] fromHex(String s) {
    int l = s.length();
    byte[] bytes = new byte[l >> 1];
    for (int i = 0, j = 0; i < l; i += 2) {
      bytes[j++] = (byte)
          ((Character.digit(s.charAt(i), 16) << 4) +
           Character.digit(s.charAt(i + 1), 16));
    }
    return bytes;
  }

  private final static char[] hexArray = "0123456789abcdef".toCharArray();

  private static String toHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length << 1];
    for (int i = 0, j = 0; j < bytes.length; i += 2) {
      int v = bytes[j++] & 0xFF;
      hexChars[i] = hexArray[v >>> 4];
      hexChars[i + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  public static byte[] intToBytes(int v) {
    return new byte[]{
        (byte) (v >>> 24),
        (byte) (v >>> 16),
        (byte) (v >>> 8),
        (byte) v};
  }

  public static int bytesToInt(byte[] b) {
    return (b[0] & 0xFF) << 24 | (b[1] & 0xFF) << 16 | (b[2] & 0xFF) << 8 | (b[3] & 0xFF);
  }

  public static byte[] longToBytes(long v) {
    return new byte[]{
        (byte) (v >>> 56),
        (byte) (v >>> 48),
        (byte) (v >>> 40),
        (byte) (v >>> 32),
        (byte) (v >>> 24),
        (byte) (v >>> 16),
        (byte) (v >>> 8),
        (byte) v};
  }

  public static long bytesToLong(byte[] b) {
    return ((long) bytesToInt(b) << 32) + (longLowBytesToInt(b) & 0x00000000ffffffffL);
  }

  public static int longLowBytesToInt(byte[] b) {
    return (b[4] & 0xFF) << 24 | (b[5] & 0xFF) << 16 | (b[6] & 0xFF) << 8 | (b[7] & 0xFF);
  }

  public static byte[] readInputStream(InputStream in) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[1024];
    while ((nRead = in.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }
    buffer.flush();
    return buffer.toByteArray();
  }

  public static byte[] concat(byte[]... allData) {
    int totalLength = 0;
    for (byte[] data : allData) {
      totalLength += data.length;
    }
    byte[] result = new byte[totalLength];
    int pointer = 0;
    for (byte[] data : allData) {
      System.arraycopy(data, 0, result, pointer, data.length);
      pointer += data.length;
    }
    return result;
  }

  private static byte[] getDataBy4BytesLength(byte[] data) {
    if (data == null || data.length < 4) {
      throw new CipherError("错误的data长度: <4");
    }
    int l = bytesToInt(data);
    if (l > data.length - 4 || l <= 0) {
      throw new CipherError("错误的data长度:" + l);
    }
    byte[] result = new byte[l];
    System.arraycopy(data, 4, result, 0, l);
    return result;
  }

  private static byte[] lastBytes(int len, byte[] data) {
    byte[] result = new byte[len];
    System.arraycopy(data, data.length - len, result, 0, len);
    return result;
  }

  public static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");

  public enum Headers {
    AppId,
    MerchantClient,
    AesEncryptMode,
    RsaSignHashMode,
  }

  public static class CipherError extends RuntimeException {
    CipherError() {
      super();
    }

    CipherError(String msg) {
      super(msg);
    }
  }
}