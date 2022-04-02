package br.com.gabrieltonhatti.estrategia2;

import br.com.gabrieltonhatti.entidades.Conta;
import br.com.gabrieltonhatti.entidades.Usuario;
import br.com.gabrieltonhatti.service.ContaService;
import br.com.gabrieltonhatti.service.UsuarioService;
import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContaServiceTest {

    static Faker faker = new Faker();
    ContaService service = new ContaService();
    UsuarioService userService = new UsuarioService();
    private static Usuario usuarioGlobal;
    private Conta contaTeste;

    @BeforeClass
    public static void criarUsuario() {
        usuarioGlobal = new Usuario(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.internet().password()
        );
    }

    @Before
    public void inserirConta() throws Exception {
        Usuario usuarioSalvo = userService.salvar(usuarioGlobal);
        Conta conta = new Conta(faker.superhero().name(), usuarioGlobal);
        contaTeste = service.salvar(conta);
    }

    @Test
    public void testInserir() throws Exception {
        Conta conta = new Conta(faker.superhero().name(), usuarioGlobal);
        Conta contaSalva = service.salvar(conta);

        assertNotNull(contaSalva.getId());
        userService.printAll();
        service.printAll();
    }

    @Test
    public void testAlterar() throws Exception {
        service.printAll();

        String novoNome = faker.ancient().god();
        contaTeste.setNome(novoNome);
        Conta contaAlterada = service.salvar(contaTeste);

        assertEquals(novoNome, contaAlterada.getNome());
        service.printAll();
    }

    @Test
    public void testConsultar() throws Exception {
        Conta contaBuscada = service.findById(contaTeste.getId());
        assertEquals(contaTeste.getNome(), contaBuscada.getNome());
    }

    @Test
    public void testExcluir() throws Exception {
        service.printAll();
        service.delete(contaTeste);

        Conta contaBuscada = service.findById(contaTeste.getId());

        assertNull(contaBuscada);
        service.printAll();
    }

}
