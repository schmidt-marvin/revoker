# Introduction
This repository contains a Java 8 implementation of a Certificate Revocation List (CRL) Distribution Point and an Online Certificate Status Protocol (OCSP) Responder.

# Changes in this fork
This fork adds customized functionality to support certificate revocation needs of my [bachelors thesis **"Signed QR-Codes"**](https://gitlab.rlp.net/marvin/ws2122-bachelor/). <br>Changes made to the original repo will be tracked here:
- [x] OCSP using ECDSA keys
- [x] Improved usage documentation<br>(Changes in README.md marked with *)


# Overview
This app is a Dropwizard app that can respond to CRL requests and OCSP requests for a given CA. You need to provide the app
with access to the index file of the CA, which is effectively the database for the CA, the crl file, and a Java KeyStore
containing the key and certificate chain to sign the OCSP responses with. This is all done within the `conf.yml` file.

[![Build Status](https://travis-ci.org/wdawson/revoker.svg?branch=master)](https://travis-ci.org/wdawson/revoker)

# Running the application

To test the application, run the following commands.

- To package the application, run:

  ```
  mvn package
  ```

- To run the server, run:

  ```
  java -jar target/revoker-0.1.0.jar server conf.yml
  ```

# *Configuration

## *Requirements

You will need to supply:
- `someconfigname.yml` --> configuration file 
- `somecrlname.pem` --> PEM-encoded CRL of your CA 
- `index.txt` --> certificate status info file (more info [here](./README_indextxt.MD))
- `somejksname.jks` --> Java Keystore (JKS) file containing a PKCS#12-encoded entry of your CA <br>(cert chain from ca to root + private key of CA)

Sample sets of config files can be found [here](./src/test/resources) and [here](./src/test/resources/customized_example").

## *YML configuration
The `config.yml` looks like this by default:
```yml
server:
  requestLog:
    timeZone: UTC
  applicationConnectors:
    - type: http
      port: 8080

  adminConnectors:
    - type: http
      port: 8081

logging:
  level: INFO
  appenders:
    - type: console

ocspResponder:
  keyStorePath: /path/to/ocsp-signing.jks
  keyStorePassphrase: "notsecret"
  keyStoreAlias: "ocsp-signing"

certificateAuthority:
  caIndexFile: /path/to/index.txt
  refreshSeconds: 10
  crlFiles:
    - name: "crl.pem"
      filePath: /path/to/crl.pem
```

Some notes on how the provided parametes affect the functionality of the app will be provided here:
- `server/applicationConnectors/port` and `server/adminConnectors/port` should not be occupying an already existing well-known port specified in the [IANA Service Name and Transport Protocol Port Number Registry](https://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xhtml)
- `ocspResponder/keyStorePath`, `ocspResponder/caIndexFile` and `ocspResponder/crlFiles/filePath` must not contain quotation marks. <br> For example, use ``/path/to/ocsp-signing.jks`` for Unix and ``C:\Users\user\Desktop\ocsp-signing.jks`` for Windows systems.
- `ocspResponder/keyStoreAlias` refers to the alias given to the PKCS#12 entry within the JKS. <br>If you're unsure of the alias name, use [the excellent KeyStore Explorer GUI](https://keystore-explorer.org/).
- `ocspResponder/crlFiles/name` defines the keyword required to access this CRL from the CRL distribution point.

# Accessible services

  - To use the admin operational menu, navigate a browser to:

    ```
    http://localhost:8081
    ```

  - *OCSP requests via POST and GET should be sent to

    ```
    http://localhost:8080/ocsp/
    ```

  - *A GET request should look like this, for example:
    ```
    http://localhost:8080/ocsp/MHoweDBaMFgwOjAJBgUrDgMCGgUABBTtlxsOgPp7xrvBIDM8XHhNB3xIZAQUhmVCR0xqIhwWe8LUHryoqMZ01HwCAQGgGjAYMBYGCSsGAQUFBzABAgEB_wQGAX5ODdB4ohowGDAWBgkrBgEFBQcwAQIBAf8EBgF-Tg3QeA..
    ```

  - *CRLs can be retrieved from 
    ```
    http://localhost:8080/crls/mycacrl.crl
    ```
    where ``mycacrl.crl`` is the keyword for the CRL and <br>
    the port is the value specified in the YML configuration file.


# Testing

## OCSP Responder
- You can use the following openssl command to test that the OCSP works correctly

  ```
  openssl ocsp -CAfile intermediate/certs/ca-chain.cert.pem \
        -url http://localhost:8080/ocsp -resp_text \
        -issuer intermediate/certs/intermediate.cert.pem \
        -cert intermediate/certs/test.example.com.cert.pem
  ```

## *CRL Distribution Point

- Navigate a browser to this URL. The CRL will be provided by the server.

  ```
    http://localhost:8080/crls/crl.pem
  ```
