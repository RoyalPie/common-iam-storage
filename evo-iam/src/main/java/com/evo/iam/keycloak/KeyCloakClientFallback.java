//package com.evo.iam.keycloak;
//
//import com.evo.common.dto.TokenDTO;
//import com.evo.common.dto.response.ApiResponses;
//import com.evo.common.dto.response.Response;
//import com.evo.common.enums.ServiceUnavailableError;
//import com.evo.common.exception.ForwardInnerAlertException;
//import com.evo.common.exception.ResponseException;
//import com.evo.iam.dto.request.GetTokenRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.openfeign.FallbackFactory;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class KeyCloakClientFallback implements FallbackFactory<KeyCloakClient> {
//    @Override
//    public KeyCloakClient create(Throwable cause) {
//        return new FallbackWithFactory(cause);
//    }
//
//    @Slf4j
//    static class FallbackWithFactory implements KeyCloakClient {
//        private final Throwable cause;
//
//        FallbackWithFactory(Throwable cause) {
//            this.cause = cause;
//        }
//
//        @Override
//        public Response<TokenDTO> getToken(GetTokenRequest param) {
//            if (cause instanceof ForwardInnerAlertException) {
//                return Response.fail((RuntimeException) cause);
//            }
//            return Response.fail(
//                    new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
//        }
//
//        @Override
//        public Response<String> getUserIdFromKeycloak(String token,String username) {
//            if (cause instanceof ForwardInnerAlertException) {
//                return Response.fail((RuntimeException) cause);
//            }
//            return Response.fail(
//                    new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
//        }
//    }
//}
