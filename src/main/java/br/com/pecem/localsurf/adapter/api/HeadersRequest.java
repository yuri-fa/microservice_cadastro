package br.com.pecem.localsurf.adapter.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public final class HeadersRequest {

    public static final String TRANSACAO_ID = "x-transacao-id";
    private static ThreadLocal<Header> tThreadLocal = new ThreadLocal<>();
    private static HeadersRequest instance;

    public static HeadersRequest getInstance() {
        return instance;
    }

    public static void initInstance(String TRANSACAO_ID){
        if (Objects.isNull(instance)){
            instance = new HeadersRequest();
        }
        tThreadLocal.set(new Header(TRANSACAO_ID));
    }

    public void clear(){
        tThreadLocal.remove();
    }

    public String getTransacaoId(){
        return tThreadLocal.get().getTransacao();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Header{
        private String transacao;
    }
}
