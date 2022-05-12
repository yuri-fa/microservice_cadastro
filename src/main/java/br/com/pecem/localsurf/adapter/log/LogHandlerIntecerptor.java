package br.com.pecem.localsurf.adapter.log;

import br.com.pecem.localsurf.adapter.api.HeadersRequest;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogHandlerIntecerptor implements HandlerInterceptor {

    /**
     * @see antes de qualquer acao esse metodo é executado para colocar o transacao com identificador do log
     * pegar a transacao da Header da requisicao
     * adiciona a transacao na thread
     * e salva no mapa do MDC
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String transacao = request.getHeader(HeadersRequest.TRANSACAO_ID);

        HeadersRequest.initInstance(transacao);

        MDC.put("transacaoID",transacao);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * @see na finalizacao da acoo esse metodo e chamado para remover o transacao da thread
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
        HeadersRequest.getInstance().clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
