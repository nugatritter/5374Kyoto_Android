/**
 * Copyright 2017 kubotaku1119 <kubotaku1119@gmail.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kubotaku.android.code4kyoto5374.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

/**
 * Encryption / decryption utility class
 */

public class EncryptUtil {

    private static final String KEY_PROVIDER = "AndroidKeyStore";

    private static final String KEY_ALIAS = "code_for_kyoto";

    // Cipher algorithm (api level 18 - 22)
    private static final String ALGORITHM_OLD = "RSA/ECB/PKCS1Padding";

    // Cipher algorithm (api level 23+)
    private static final String ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    /**
     * Encrypts the specified string（+Base64)
     * <p>
     * However, it is valid only for API Level 18 and later. Previous versions returns the original string as it is
     * </p>
     *
     * @param context   Context
     * @param plainText String
     * @return Encrypted (+Base64) strings
     */
    public static String encryptString(@NonNull Context context, @NonNull final String plainText) {
        if (plainText == null) {
            return plainText;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return encryptStringImpl(context, plainText);
        } else {
            return plainText;
        }
    }

    /**
     * Compound the encrypted string.
     * <p>
     * However, it is valid only for API Level 18 and later.Previous terminal returns the original string as it is
     * </p>
     *
     * @param context       Context
     * @param encryptedText String
     * @return Compound strings
     */
    public static String decryptString(@NonNull Context context, @NonNull final String encryptedText) {
        if (encryptedText == null) {
            return encryptedText;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return decryptStringImpl(context, encryptedText);
        } else {
            return encryptedText;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static String encryptStringImpl(Context context, final String plainText) {
        String encryptedText = plainText;
        try {
            final KeyStore keyStore = getKeyStore(context);

            PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();

            String algorithm = ALGORITHM_OLD;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                algorithm = ALGORITHM;
            }
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, cipher);
            cipherOutputStream.write(plainText.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] bytes = outputStream.toByteArray();
            encryptedText = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedText;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static String decryptStringImpl(Context context, final String encryptedText) {
        String plainText = null;
        try {
            final KeyStore keyStore = getKeyStore(context);

            PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, null);

            String algorithm = ALGORITHM_OLD;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                algorithm = ALGORITHM;
            }
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(encryptedText, Base64.DEFAULT)), cipher);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int b;
            while ((b = cipherInputStream.read()) != -1) {
                outputStream.write(b);
            }
            outputStream.close();
            plainText = outputStream.toString("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static KeyStore getKeyStore(Context context) {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(KEY_PROVIDER);
            keyStore.load(null);

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // for api level 23+
                    generateNewKey();
                } else {
                    // for api level 18 - 22
                    generateNewKeyOld(context);
                }
            }

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return keyStore;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void generateNewKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, KEY_PROVIDER);

            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(
                            KEY_ALIAS,
                            KeyProperties.PURPOSE_DECRYPT)
                            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                            .build());
            keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static void generateNewKeyOld(Context context) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, KEY_PROVIDER);

            Calendar instance = Calendar.getInstance();
            Date start = instance.getTime();

            instance.add(Calendar.YEAR, 1);
            Date end = instance.getTime();

            keyPairGenerator.initialize(
                    new KeyPairGeneratorSpec.Builder(context)
                            .setAlias(KEY_ALIAS)
                            .setSubject(new X500Principal("CN=" + KEY_ALIAS))
                            .setSerialNumber(BigInteger.valueOf(20161225))
                            .setStartDate(start)
                            .setEndDate(end)
                            .build());

            keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

}
