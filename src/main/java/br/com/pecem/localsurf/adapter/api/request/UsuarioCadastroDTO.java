package br.com.pecem.localsurf.adapter.api.request;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UsuarioCadastroDTO {

    private Long id;
    private String nome;
    private String apelido;
    private String email;
    private Byte[] foto;
    private String telefone;
    private Date dataCadastro;
    private String status;
    private RoleDTO role;
}
