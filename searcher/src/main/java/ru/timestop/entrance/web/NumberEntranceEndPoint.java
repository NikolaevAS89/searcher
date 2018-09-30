package ru.timestop.entrance.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.timestop.entrance.generated.GetResultRequest;
import ru.timestop.entrance.generated.GetResultResponse;
import ru.timestop.entrance.generated.Result;
import ru.timestop.entrance.service.ResultService;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 30.09.2018
 */
@Endpoint
public class NumberEntranceEndPoint {
    private static final String NAMESPACE_URI = "http://timestop.ru/entrance/generated";

    @Autowired
    private ResultService resultService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getResult")
    @ResponsePayload
    public GetResultResponse getResult(@RequestPayload GetResultRequest request) {
        Result result =  resultService.getResult(request.getNumber());

        GetResultResponse response = new GetResultResponse();
        response.setResult(result);

        return response;
    }
}
