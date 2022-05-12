package br.com.pecem.localsurf.adapter.api;

import br.com.pecem.localsurf.adapter.api.request.AvaliacaoCadastroDTO;
import br.com.pecem.localsurf.adapter.api.request.RoleDTO;
import br.com.pecem.localsurf.adapter.api.request.UsuarioCadastroDTO;
import br.com.pecem.localsurf.domain.exception.AvaliarUsuarioException;
import br.com.pecem.localsurf.domain.model.LogEvento;
import br.com.pecem.localsurf.domain.model.Roles;
import br.com.pecem.localsurf.domain.model.StatusUsuario;
import br.com.pecem.localsurf.domain.model.Usuario;
import br.com.pecem.localsurf.domain.usecases.UsuarioUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuario")
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioUseCase usuarioUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @LogEvento
    public ResponseEntity<Void> cadastro(@RequestHeader(HeadersRequest.TRANSACAO_ID) String transacao,
            @RequestBody UsuarioCadastroDTO usuarioCadastroDTO){
        var usuario = Usuario.builder()
                .nome(usuarioCadastroDTO.getNome())
                .apelido(usuarioCadastroDTO.getApelido())
                .email(usuarioCadastroDTO.getEmail())
                .foto(usuarioCadastroDTO.getFoto())
                .telefone(usuarioCadastroDTO.getTelefone())
                .dataCadastro(usuarioCadastroDTO.getDataCadastro())
                .statusUsuario(StatusUsuario.SOLICITADO)
                .role(new Roles("Administrador"))
                .build();
        usuarioUseCase.cadastrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<List<UsuarioCadastroDTO>> consulta(@RequestParam(value = "nome",required = false) String nome,
                                           @RequestParam(value = "apelido",required = false) String apelido,
                                           @RequestParam(value = "dataCadastroInicio",required = false) Date dataCadastroInicio,
                                           @RequestParam(value = "dataCadastroFinal",required = false) String dataCadastroFinal){

        List<Usuario> usuarioList = usuarioUseCase.consultarUsuario(nome,apelido,dataCadastroInicio,dataCadastroFinal);
        if (Objects.isNull(usuarioList) || usuarioList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<UsuarioCadastroDTO> usuarioCadastroDTOList = usuarioList.stream().map(usuario -> {
            var usuarioDTO = UsuarioCadastroDTO.builder()
                    .id(usuario.getId())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .telefone(usuario.getTelefone())
                    .dataCadastro(usuario.getDataCadastro())
                    .status(usuario.getStatusUsuario().name())
                    .foto(usuario.getFoto())
                    .role(Objects.isNull(usuario.getRole()) ?
                            RoleDTO.builder().build() :
                            RoleDTO.builder().id(usuario.getRole().getId()).permissao(usuario.getRole().getPermissao()).build())
                    .build();
            return usuarioDTO;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(usuarioCadastroDTOList);
    }

    @PatchMapping(path = "{usuarioId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioCadastroDTO> avaliarCadastro(@RequestBody AvaliacaoCadastroDTO avaliacaoCadastroDTO, @PathVariable("usuarioId") Long usuarioId){
        try{
            Usuario usuario = usuarioUseCase.avaliarCadastro(usuarioId,avaliacaoCadastroDTO);
            var usuarioDTO = UsuarioCadastroDTO.builder()
                    .id(usuario.getId())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .telefone(usuario.getTelefone())
                    .dataCadastro(usuario.getDataCadastro())
                    .status(usuario.getStatusUsuario().name())
                    .foto(usuario.getFoto())
                    .role(Objects.isNull(usuario.getRole()) ?
                            RoleDTO.builder().build() :
                            RoleDTO.builder().id(usuario.getRole().getId()).permissao(usuario.getRole().getPermissao()).build())
                    .build();
            return ResponseEntity.ok(usuarioDTO);
        }catch (AvaliarUsuarioException aue){
            return (ResponseEntity<UsuarioCadastroDTO>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path ="{usuarioId}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioCadastroDTO> atualizarUsuario(@RequestBody UsuarioCadastroDTO usuarioCadastroDTO,@PathVariable("usuarioId") Long usuarioId){
        var usuario = Usuario.builder()
                .statusUsuario(StatusUsuario.ALTERADO)
                .apelido(usuarioCadastroDTO.getApelido())
                .email(usuarioCadastroDTO.getEmail())
                .foto(usuarioCadastroDTO.getFoto())
                .nome(usuarioCadastroDTO.getNome())
                .telefone(usuarioCadastroDTO.getTelefone()).build();
        Usuario usuarioRetorno = usuarioUseCase.atualizacaoUsuario(usuario,usuarioId);
        var usuarioDTO = UsuarioCadastroDTO.builder()
                .id(usuarioRetorno.getId())
                .nome(usuarioRetorno.getNome())
                .email(usuarioRetorno.getEmail())
                .telefone(usuarioRetorno.getTelefone())
                .dataCadastro(usuarioRetorno.getDataCadastro())
                .status(usuarioRetorno.getStatusUsuario().name())
                .foto(usuarioRetorno.getFoto())
                .role(Objects.isNull(usuarioRetorno.getRole()) ?
                        RoleDTO.builder().build() :
                        RoleDTO.builder().id(usuarioRetorno.getRole().getId()).permissao(usuarioRetorno.getRole().getPermissao()).build())
                .build();
        return  ResponseEntity.ok(usuarioDTO);
    }
}
