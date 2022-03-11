package br.com.pecem.localsurf.adapter.api;

import br.com.pecem.localsurf.adapter.api.request.AvaliacaoCadastroDTO;
import br.com.pecem.localsurf.adapter.api.request.RoleDTO;
import br.com.pecem.localsurf.adapter.api.request.UsuarioCadastroDTO;
import br.com.pecem.localsurf.domain.exception.AvaliarUsuarioException;
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
    public ResponseEntity<Void> cadastro(@RequestBody UsuarioCadastroDTO usuarioCadastroDTO){
        log.info("Cadastro do usuario " + usuarioCadastroDTO);
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
        log.info("Usuario " + usuario.getNome() + " cadastrado com sucesso");
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

    @PutMapping(path = "{usuarioId}",consumes = MediaType.APPLICATION_JSON_VALUE)
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
}
