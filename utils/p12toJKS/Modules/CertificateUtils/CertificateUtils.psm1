Function Get-BundledCertificatesFromURL {
[CmdletBinding()]
param (
	[Parameter(Mandatory=$true,
   		HelpMessage="Web page the certificate should be downloaded from.")] 
	[string]$hostname
)
BEGIN { 
echo "begin processing $hostname"
$path = "$pwd\certs"
New-Item -ItemType Directory -Force -Path $path # ensure path exists
}

PROCESS {  
echo "`n" | # provides some sort of message to the server
openssl s_client -connect "$($hostname):443" -showcerts | # connect and print all certs sent over from server
sed -n -e '/BEGIN\ CERTIFICATE/,/END\ CERTIFICATE/ p' | # filter all verbose prints from connection but certs
Out-File -FilePath "$path\$hostname-aspem.cer" -Encoding ASCII # write as ASCII-file
}     

END { 
"finished processing $hostname"
}

}

Function Get-CertsFromURLList {
[CmdletBinding()]
param (
	[Parameter(Mandatory=$true,
   		HelpMessage="List of web pages certificates should be downloaded from.")] 
	[string]$listPath
)
BEGIN { 

}

PROCESS {  
foreach($line in Get-Content $listPath)
{
	Get-BundledCertificatesFromURL $listPath
}
}     

END { 
"finished processing list at $listPath"
}

}

Function Invoke-BulkP12ToJKS {
[CmdletBinding()]
param (
	[Parameter(Mandatory=$true,
   		HelpMessage="Tab-delimited list of paths to a .p12-CertStore and matching password")] 
	[string]$listPath
)
BEGIN { 

}

PROCESS {  
foreach ($line in Get-Content $listPath)
{
	$loc = ($line -split "`t")[0]
	$newloc = $loc.replace('p12', 'jks')
	$pass = ($line -split "`t")[1]
	echo "($loc).jks"

	keytool -v `
	-importkeystore -srckeystore $loc `
	-srcstoretype PKCS12 -srcstorepass $pass `
	-destkeystore $newloc `
	-deststoretype JKS -deststorepass $pass 
}
}     

END { 
"finished processing list at $listPath"
}

}