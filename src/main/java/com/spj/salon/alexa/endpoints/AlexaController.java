package com.spj.salon.alexa.endpoints;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;
import com.spj.salon.alexa.ports.in.IAlexaAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "alexa", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlexaController {

    private final IAlexaAdapter alexaAdapter;

    @PostMapping(value = "voice")
    public ResponseEntity<ResponseEnvelope> requestIdCard(@RequestBody RequestEnvelope requestEnvelope,
                                                          @RequestHeader Map<String, String> headers) {
        if (!validSignature(headers.get("signaturecertchainurl")))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(alexaAdapter.processAlexaRequest(requestEnvelope, headers));
    }

    /**
     * Test cases
     * The following are examples of correctly formatted URLs:
     *
     * https://s3.amazonaws.com/echo.api/echo-api-cert.pem
     * https://s3.amazonaws.com:443/echo.api/echo-api-cert.pem
     * https://s3.amazonaws.com/echo.api/../echo.api/echo-api-cert.pem
     * The following are examples of invalid URLs:
     *
     * http://s3.amazonaws.com/echo.api/echo-api-cert.pem (invalid protocol)
     * https://notamazon.com/echo.api/echo-api-cert.pem (invalid hostname)
     * https://s3.amazonaws.com/EcHo.aPi/echo-api-cert.pem (invalid path)
     * https://s3.amazonaws.com/invalid.path/echo-api-cert.pem (invalid path)
     * https://s3.amazonaws.com:563/echo.api/echo-api-cert.pem (invalid port)
     * @param signatureCertChainUrl
     * @return
     */
    private boolean validSignature(String signatureCertChainUrl) {
        try{
            URL myURL = new URL(signatureCertChainUrl);
            if(myURL.getPort()!=-1 && myURL.getPort()!=443)
                return false;
            if(!"s3.amazonaws.com".equals(myURL.getHost()))
                return false;
            if(!myURL.getPath().startsWith("/echo.api/"))
                return false;
            if(!myURL.getProtocol().equals("https"))
                return false;
        }catch (MalformedURLException mfe){
            return false;
        }
        return true;
    }
}
