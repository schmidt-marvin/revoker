# Formatting of Apache index.txt

This is the file used by OCSP servers to save certificate revocation states. CRLs and responses to OCSP requests are built from this database. Also the [OCSP Responder Server](https://github.com/xperseguers/ocsp-responder) I'm looking to work with requires this file. So, I'm trying to understand it - which is pretty painful due to the lack of good and accessible documentation. From various ressources, here's what's important.

## Basics
- ASCII file
- Six tab-seperated fields in one line representing one entry

## Plain Example

```
R   130110200751Z   100201142709Z,superseded    8E  unknown /O=Example/CN=Foo
V   130119193554Z       8F  unknown /O=Example/CN=Bar
```

## Formatted

| state | end date      | revocation date, revocation reason | serial | path to cert | subject   DN        |
|-------|---------------|------------------------------------|--------|--------------|-------------------|
| R     | 130110200751Z | 100201142709Z,superseded           | 8E     | unknown      | /O=Example/CN=Foo |
| V     | 130119193554Z |                                    | 8F     | unknown      | /O=Example/CN=Bar |

## Descriptions
Hacked together reference from several **forum posts**...
- [Most helpful ressource](https://help.cloud.fabasoft.com/index.php?topic=doc/How-to-Create-a-CA-and-User-Certificates-for-Your-Organization-in-Fabasoft-Cloud/create-a-ca-via-openssl.htm#add-a-ca-to-indextxt)
- [Reference for revocation reasons](https://www.openssl.org/docs/manmaster/man1/openssl-ca.html)

(1) State: “V” for Valid, “E” for Expired and “R” for revoked (Note that an expired may have the type "V" because the type has not been updated. 'openssl ca updatedb' does such an update.)

(2) Enddate: in the format YYMMDDHHmmssZ (the “Z” stands for Zulu/GMT)

(3) Date of Revocation: same format as “Enddate” (with a comma value for the reason)

(4) Serial: serial of the certificate

(5) Path to Certificate: mostly “unknown”

(6) Subject: subject DN of the certificate


### Revocation Resaons

| Reason (case insensitive)          |
|----------------------|
| unspecified        |
| keyCompromise        |
| CACompromise         |
| affiliationChanged   |
| superceded           |
| cessationOfOperation |
| certificateHold      |
| removeFromCRL        |