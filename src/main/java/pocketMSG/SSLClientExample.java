package pocketMSG;

/*
 * Copyright (c) 2010-2018 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class SSLClientExample {

    /*
     * Keystore with certificate created like so (in JKS format):
     *
     *keytool -genkey -keyalg RSA -validity 3650 -keystore "keystore.jks" -storepass "storepassword" -keypass "keypassword" -alias "default" -dname "CN=127.0.0.1, OU=MyOrgUnit, O=MyOrg, L=MyCity, S=MyRegion, C=MyCountry"
     */
    public static void main(String[] args) throws Exception {
//        WebSocketChatClient chatclient = new WebSocketChatClient(new URI("wss://pocketmsg.ru:8888/v1/ws/"));
        WebSocketChatClient chatclient = new WebSocketChatClient(new URI("wss://echo.websocket.org:443/"));

        // load up the key store
//        String STORETYPE = "JKS";
//        String KEYSTORE = "keystore.jks";
//        String STOREPASSWORD = "storepassword";
//        String KEYPASSWORD = "keypassword";
//
//        KeyStore ks = KeyStore.getInstance( STORETYPE );
//        File kf = new File( KEYSTORE );
//        ks.load( new FileInputStream( kf ), STOREPASSWORD.toCharArray() );
//
//        KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
//        kmf.init( ks, KEYPASSWORD.toCharArray() );
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
//        tmf.init( ks );

        SSLContext sslContext = null;
        sslContext = SSLContext.getInstance("TLS");

//        sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
        sslContext.init(null, null, null); // will use java's default key and trust store which is sufficient unless you deal with self-signed certificates

//        SSLSocketFactory factory = sslContext.getSocketFactory();
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        chatclient.setSocketFactory(factory);

        System.out.println("scheme \t"+chatclient.getURI().getScheme());
        System.out.println("host \t"+chatclient.getURI().getHost());
        System.out.println("port \t"+chatclient.getURI().getPort());
        System.out.println("path \t"+chatclient.getURI().getPath());
        System.out.println("query \t"+chatclient.getURI().getQuery());
        System.out.println("RawUserInfo \t"+chatclient.getURI().getRawUserInfo());

        chatclient.connectBlocking();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("введите сообщение");
            String line = reader.readLine();
            if (line.equals("close")) {
                chatclient.close();
            } else {
                chatclient.send(line);
            }
        }

    }
}
