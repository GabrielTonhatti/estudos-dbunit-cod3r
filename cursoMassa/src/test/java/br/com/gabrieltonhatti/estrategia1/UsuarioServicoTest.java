package br.com.gabrieltonhatti.estrategia1;

import br.com.gabrieltonhatti.entidades.Usuario;
import br.com.gabrieltonhatti.service.UsuarioService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsuarioServicoTest {

    private final UsuarioService service = new UsuarioService();
    private static Usuario usuarioGlobal;

    @Test
    public void teste1_inserir() throws Exception {
        Usuario usuario = new Usuario("Usuario estratégia #1", "user@gmail.com", "passwd");
        usuarioGlobal = service.salvar(usuario);

        assertNotNull(usuarioGlobal.getId());
    }

    @Test
    public void teste2_consultar() throws Exception {
        Usuario usuario = service.findById(usuarioGlobal.getId());

        assertEquals("Usuario estratégia #1", usuario.getNome());
    }

    @Test
    public void teste3_alterar() throws Exception {
        Usuario usuario = service.findById(usuarioGlobal.getId());
        usuario.setNome("Usuário alterado");
        Usuario usuarioAlterado = service.salvar(usuario);

        assertEquals("Usuário alterado", usuarioAlterado.getNome());
    }

    @Test
    public void teste4_excluir() throws Exception {
        service.delete(usuarioGlobal);
        Usuario usuarioRemovido = service.findById(usuarioGlobal.getId());

        assertNull(usuarioRemovido);
    }

}
