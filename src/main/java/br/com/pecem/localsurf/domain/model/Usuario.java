package br.com.pecem.localsurf.domain.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String nome;
    @Column
    private String apelido;
    @Column
    private String email;
    @Column
    private String telefone;
    @Lob()
    @Nullable
    @Column(length = 20971520)
    @Basic(fetch = FetchType.LAZY)
    private Byte[] foto;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;
    @Column
    private StatusUsuario statusUsuario;
    @JoinColumn(referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Roles role;
}

