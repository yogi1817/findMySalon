package com.spj.salon.alexa.ports.in;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ResponseEnvelope;

import java.util.Map;
import java.util.Optional;

public interface IAlexaAdapter {
    ResponseEnvelope processAlexaRequest(RequestEnvelope requestEnvelope, Map<String, String> headers);
}
