package br.com.pecem.localsurf.adapter.api.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class RoleDTO {
    private Long id;
    private String permissao;
}
