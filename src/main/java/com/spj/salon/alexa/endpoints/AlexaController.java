package com.spj.salon.alexa.endpoints;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;
import com.spj.salon.alexa.ports.in.IAlexaAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(alexaAdapter.processAlexaRequest(requestEnvelope, headers));
    }
}
