package com.spj.salon.alexa.endpoints;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.servlet.ServletConstants;
import com.amazon.ask.servlet.verifiers.ServletRequest;
import com.amazon.ask.servlet.verifiers.SkillRequestSignatureVerifier;
import com.amazon.ask.util.JacksonSerializer;
import com.spj.salon.alexa.ports.in.IAlexaAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "alexa", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlexaController {

    private final IAlexaAdapter alexaAdapter;

    @PostMapping(value = "voice")
    public ResponseEntity<ResponseEnvelope> requestIdCard(@RequestBody RequestEnvelope requestEnvelope,
                                                          HttpServletRequest httpRequest,
                                                          @RequestHeader Map<String, String> headers) {
        try {
            log.debug("inside requestIdCard");
            //verifyAlexaRequest(httpRequest);
            log.debug("verify complete");


           /* Map<String, String> headersMap =
                    Collections.list(request.getHeaderNames())
                            .stream()
                            .collect(Collectors.toMap(
                                    name -> name,
                                    request::getHeader));*/
            getRequestEnvelop(httpRequest);
            headers.entrySet().stream().peek(a->log.debug(a.getKey()+"-"+a.getValue()));
            return ResponseEntity.ok(alexaAdapter.processAlexaRequest(requestEnvelope, headers));
        } catch (Exception e) {
            log.error("Bad Request Exception {}", e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    private void verifyAlexaRequest(HttpServletRequest httpRequest) throws IOException {
        final Serializer serializer = new JacksonSerializer();
        byte[] serializedRequestEnvelope = IOUtils.toByteArray(httpRequest.getInputStream());
        final RequestEnvelope deserializedRequestEnvelope = serializer.deserialize(IOUtils.toString(
                serializedRequestEnvelope, ServletConstants.CHARACTER_ENCODING), RequestEnvelope.class);

        ServletRequest servletRequest = new ServletRequest(httpRequest, serializedRequestEnvelope, deserializedRequestEnvelope);
        SkillRequestSignatureVerifier skillRequestSignatureVerifier = new SkillRequestSignatureVerifier();
        skillRequestSignatureVerifier.verify(servletRequest);
    }

    /*private boolean validSignature(String signatureCertChainUrl) {
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
    }*/

    private void getRequestEnvelop(HttpServletRequest httpRequest) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder builder = new StringBuilder();
        try {
            try (BufferedReader in = httpRequest.getReader()) {
                char[] buf = new char[4096];
                for (int len; (len = in.read(buf)) > 0; )
                    builder.append(buf, 0, len);
            }
        } catch (IOException e) {
            log.error("Invalid request {}", e.getMessage());
            //return null;
        }

        //return mapper.readValue(builder.toString(), RequestEnvelope.class);
    }
}
