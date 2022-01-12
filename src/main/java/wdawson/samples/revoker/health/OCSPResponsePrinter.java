package wdawson.samples.revoker.health;

import org.bouncycastle.cert.ocsp.*;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wdawson.samples.revoker.resources.OCSPResponderResource;

import java.io.IOException;

public class OCSPResponsePrinter {

    public static void printOCSPResponse(OCSPResp resp) throws IOException, OCSPException {
        // init some variables
        BasicOCSPResp brep = (BasicOCSPResp) resp.getResponseObject();
        SingleResp[] singleResponses = brep.getResponses();

        System.out.println("[BEGIN OCSP RESPONSE DEBUG]");

        // Log response status
        if (resp.getStatus() == OCSPRespBuilder.SUCCESSFUL)
            System.out.println(("- response status = SUCCESSFUL"));
        else if (resp.getStatus() == OCSPRespBuilder.MALFORMED_REQUEST)
            System.out.println("- response status = MALFORMED_REQUEST");
        else if (resp.getStatus() == OCSPRespBuilder.INTERNAL_ERROR)
            System.out.println("- response status = INTERNAL_ERROR");
        else if (resp.getStatus() == OCSPRespBuilder.TRY_LATER)
            System.out.println("- response status = TRY_LATER");
        else if (resp.getStatus() == OCSPRespBuilder.SIG_REQUIRED)
            System.out.println("- response status = SIG_REQUIRED");
        else if (resp.getStatus() == OCSPRespBuilder.UNAUTHORIZED)
            System.out.println("- response status = UNAUTHORIZED");
        else
            System.out.println("- response status = unknown response status (" + resp.getStatus() + ")");

        // log certificate status (courtesy of https://github.com/igniterealtime/Openfire/blob/master/xmppserver/src/main/java/org/jivesoftware/openfire/net/OCSPChecker.java#L109)
        for (SingleResp singleResp : singleResponses) {
            System.out.println("  [BEGIN CERT STATUS DEBUG : SERIAL " + singleResp.getCertID().getSerialNumber() + "]");
            Object status = singleResp.getCertStatus();
            if (status == CertificateStatus.GOOD)
                System.out.println("  - certificate status = GOOD");
            else if (status instanceof org.bouncycastle.cert.ocsp.RevokedStatus)
                System.out.println("  - certificate status = REVOKED");
            else if (status instanceof org.bouncycastle.cert.ocsp.UnknownStatus)
                System.out.println("  - certificate status = UNKNOWN");
            else
                System.out.println("  - certificate status = CERTIFICATE NOT RECOGNIZED");
            System.out.println("  [END CERT STATUS DEBUG : SERIAL " + singleResp.getCertID().getSerialNumber() + "]");
        }

        // Log response sent to client
        System.out.println("- built response as B64 = " + Base64.toBase64String(resp.getEncoded()));
        System.out.println("[END OCSP RESPONSE DEBUG]\n");
    }

}
