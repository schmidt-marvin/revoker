# Introduction

Simple conversion script to create ``JKS`` files from ``P12`` (PKCS12-formatted) files using the java ``keytool``. <br>Will only work for Windows systems with access to PowerShell and JRE installed. <br>Be aware, that this script does not overwrite existing .jks files at the target directories, so take care to clean these before executing.

# Installation and usage

1) Place the folder "Modules" into ``C:\Users\user\Documents\WindowsPowerShell`` to register the custom PS-commands defined in the module.
2) Specify a tab-delimited list of paths to .p12-formatted key stores to format within ``P12Locations.txt`` like this:
	```
	C:\Users\user\AppData\Roaming\CertificateProductionUtility\test\ccc\root\ocsp\rootOCSPCredentials.p12	test123
	C:\Users\user\AppData\Roaming\CertificateProductionUtility\test\ccc\root\rootCredentials.p12	test123
	```
3) Call ``Invoke-BulkP12ToJKS C:\path\to\P12Locations.txt`` to execute
4) .jks-formatted files will be placed into the folders of the original .p12-files.
