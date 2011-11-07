package com.rackspace.papi.httpx.node;

import com.rackspace.httpx.*;
import com.rackspace.papi.httpx.ObjectFactoryUser;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author fran
 */
public class ResponseHeadersNode extends ObjectFactoryUser implements Node {
    private final HttpServletResponse response;
    private final ResponseHead responseHead;
    private final List<String> fidelity;
    private final AcceptFidelityValidator fidelityValidator;

    public ResponseHeadersNode(HttpServletResponse response, ResponseHead responseHead, List<String> fidelity) {
        this.response = response;
        this.responseHead = responseHead;
        this.fidelity = fidelity;
        this.fidelityValidator = new AcceptFidelityValidator(fidelity);
    }

    @Override
    public void build() {
        ResponseHeaders responseHeaders = objectFactory.createResponseHeaders();

        responseHeaders.getFidelity().addAll(fidelity);

        if (fidelityValidator.hasValidFidelity()) {

            for (String headerName : response.getHeaderNames()) {

                if (fidelityValidator.hasStarFidelity()){
                    ComplexParameter complexParameter = objectFactory.createComplexParameter();
                    complexParameter.setName(headerName);

                    for (String nextElement : response.getHeaders(headerName)) {
                        complexParameter.getValue().add(nextElement);
                    }

                    responseHeaders.getHeader().add(complexParameter);
                }
            }
        }

        responseHead.setHeaders(responseHeaders);
    }
}
