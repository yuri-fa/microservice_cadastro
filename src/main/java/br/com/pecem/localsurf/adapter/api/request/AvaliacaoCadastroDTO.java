package br.com.pecem.localsurf.adapter.api.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class AvaliacaoCadastroDTO {

    private Boolean isAprovador;
    private RoleDTO roleDTO;

}
