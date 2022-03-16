package br.com.pecem.localsurf.domain.usecases;

import br.com.pecem.localsurf.adapter.api.request.AvaliacaoCadastroDTO;
import br.com.pecem.localsurf.domain.exception.AvaliarUsuarioException;
import br.com.pecem.localsurf.domain.model.Roles;
import br.com.pecem.localsurf.domain.model.StatusUsuario;
import br.com.pecem.localsurf.domain.model.Usuario;
import br.com.pecem.localsurf.domain.repository.RoleRepository;
import br.com.pecem.localsurf.domain.repository.UsuarioRepository;
import br.com.pecem.localsurf.domain.specification.GenericSpecificationsBuilder;
import br.com.pecem.localsurf.domain.specification.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioUseCase {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SpecificationFactory<Usuario> specificationFactory;

    public void cadastrarUsuario(Usuario usuario){

        repository.save(usuario);
    }

    public List<Usuario> consultarUsuario(String nome, String apelido, Date dataCadastroInicio, String dataCadastroFinal) {
        GenericSpecificationsBuilder<Usuario> builder = new GenericSpecificationsBuilder<>();

        if (Objects.nonNull(nome)){
            builder.with(specificationFactory.isLike("nome",nome));
        }
        if (Objects.nonNull(apelido)){
            builder.with(specificationFactory.isEquals("apelido",apelido));
        }

        return  repository.findAll(builder.build());
    }

    @Transactional
    public Usuario avaliarCadastro(Long usuarioId, AvaliacaoCadastroDTO avaliacaoCadastroDTO) throws AvaliarUsuarioException {
        try{
            Usuario usuario = repository.getById(usuarioId);
            if (avaliacaoCadastroDTO.getIsAprovador()){
                Roles role = roleRepository.getById(avaliacaoCadastroDTO.getRoleDTO().getId());

                if (Objects.nonNull(role)){
                    usuario.setRole(role);
                }

                usuario.setStatusUsuario(StatusUsuario.APROVADO);
            }else{
                usuario.setStatusUsuario(StatusUsuario.NEGADO);
            }
            return usuario;
        }catch (Exception e){
            throw new AvaliarUsuarioException("Falha na avaliação do usuario");
        }
    }

    @Transactional
    public Usuario atualizacaoUsuario(Usuario usuarioAtualizacao, Long usuarioId) {
        Optional<Usuario> usuario = repository.findById(usuarioId);
        if (usuario.isPresent()){
            Usuario usuarioSalvo = usuario.get();
            if (Objects.nonNull(usuarioAtualizacao.getApelido()) && !usuarioAtualizacao.getApelido().isEmpty()){
                usuarioSalvo.setApelido(usuarioAtualizacao.getApelido());
            }
            if (Objects.nonNull(usuarioAtualizacao.getNome()) && !usuarioAtualizacao.getNome().isEmpty()){
                usuarioSalvo.setNome(usuarioAtualizacao.getNome());
            }
            if (Objects.nonNull(usuarioAtualizacao.getFoto())){
                usuarioSalvo.setFoto(usuarioAtualizacao.getFoto());
            }
            if (Objects.nonNull(usuarioAtualizacao.getEmail()) && !usuarioAtualizacao.getEmail().isEmpty()){
                usuarioSalvo.setEmail(usuarioAtualizacao.getEmail());
            }
            return usuarioSalvo;
        }
        return null;
    }
}
