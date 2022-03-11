package br.com.pecem.localsurf.domain.usecases;

import br.com.pecem.localsurf.adapter.api.request.AvaliacaoCadastroDTO;
import br.com.pecem.localsurf.adapter.api.request.RoleDTO;
import br.com.pecem.localsurf.adapter.api.request.UsuarioCadastroDTO;
import br.com.pecem.localsurf.domain.exception.AvaliarUsuarioException;
import br.com.pecem.localsurf.domain.model.Roles;
import br.com.pecem.localsurf.domain.model.StatusUsuario;
import br.com.pecem.localsurf.domain.model.Usuario;
import br.com.pecem.localsurf.domain.respository.UsuarioRepository;
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
    private SpecificationFactory<Usuario> specificationFactory;

    public void cadastrarUsuario(Usuario usuario){

        repository.save(usuario);
    }

    public List<Usuario> consultarUsuario(String nome,String telefone,String email, Date dataCadastroInicio,Date dataCadastrofinal) {
        GenericSpecificationsBuilder<Usuario> builder = new GenericSpecificationsBuilder<>();
        if (Objects.nonNull(nome)){
            builder.with(specificationFactory.isEquals("name","yuri furtado alcantara"));
        }
       return  repository.findAll(builder.build());
    }

    public List<Usuario> consultarUsuario(String nome, String apelido, Date dataCadastroInicio, String dataCadastroFinal) {
        GenericSpecificationsBuilder<Usuario> builder = new GenericSpecificationsBuilder<>();

        if (Objects.nonNull(nome)){
            builder.with(specificationFactory.isEquals("nome",nome));
        }
        if (Objects.nonNull(apelido)){
            builder.with(specificationFactory.isEquals("apelido",apelido));
        }

        return  repository.findAll(builder.build());
    }

    public Usuario avaliarCadastro(Long usuarioId, AvaliacaoCadastroDTO avaliacaoCadastroDTO) throws AvaliarUsuarioException {
        try{
            Usuario usuario = repository.getById(usuarioId);
            if (avaliacaoCadastroDTO.getIsAprovador()){
                usuario.setRole(Roles.builder().id(avaliacaoCadastroDTO.getRoleDTO().getId()).build());
                usuario.setStatusUsuario(StatusUsuario.APROVADO);
            }else{
                usuario.setStatusUsuario(StatusUsuario.NEGADO);
            }
            repository.save(usuario);
            return usuario;
        }catch (Exception e){
            throw new AvaliarUsuarioException("Falha na avaliação do usuario");
        }
    }
}
