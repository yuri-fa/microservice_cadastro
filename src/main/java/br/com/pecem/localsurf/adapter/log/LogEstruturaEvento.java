package br.com.pecem.localsurf.adapter.log;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import net.logstash.logback.argument.StructuredArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.kv;

@ToString
public class LogEstruturaEvento {

    private final List<Evento> eventList;

    public LogEstruturaEvento() {
        this.eventList = new ArrayList<>();
    }

    public LogEstruturaEvento evento(final String chave,final Object inicio,final Object fim){
        eventList.add(Evento.builder().chave(chave).inicio(() -> inicio).fim(() -> fim).build());
        return this;
    }

    public LogEstruturaEvento evento(final String chave,final Supplier<Object> inicio,final Object fim){
        eventList.add(Evento.builder().chave(chave).inicio(inicio).fim(() -> fim).build());
        return this;
    }

    public LogEstruturaEvento evento(final String chave,final Object inicio,final Supplier<Object> fim){
        eventList.add(Evento.builder().chave(chave).inicio(() -> inicio).fim(fim).build());
        return this;
    }

    public LogEstruturaEvento evento(final String chave,final Supplier<Object> inicio,final Supplier<Object> fim){
        eventList.add(Evento.builder().chave(chave).inicio(inicio).fim(fim).build());
        return this;
    }

    public Object[] start(){
        final List<StructuredArgument> structuredArgumentList = this.eventList.stream()
                .filter(evento -> Objects.nonNull(evento.getInicio()))
                .map(evento -> {
                        final Object valor = evento.getInicio().get();
//                        System.out.println(evento.getChave()+":"+valor);
                        return kv(evento.getChave(),valor);
                    }
                )
                .collect(Collectors.toList());
        return structuredArgumentList.toArray(new StructuredArgument[structuredArgumentList.size()]);
    }

    public Object[] end(){
        final List<StructuredArgument> structuredArguments = this.eventList.stream()
                .filter(evento -> Objects.nonNull(evento.getFim()))
                .map(evento ->{
                        final Object valor = evento.getFim().get();
//                        System.out.println(evento.getChave()+":"+valor);
                        return kv(evento.getChave(),valor);
                    }
                )
                .collect(Collectors.toList());
        return structuredArguments.toArray(new StructuredArgument[structuredArguments.size()]);
    }

    @Getter
    @Builder
    @ToString
    private static class Evento{
        private String chave;
        private Supplier<Object> inicio;
        private Supplier<Object> fim;
    }

}
