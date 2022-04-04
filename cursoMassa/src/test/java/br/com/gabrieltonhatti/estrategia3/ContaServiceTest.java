package br.com.gabrieltonhatti.estrategia3;

import br.com.gabrieltonhatti.entidades.Conta;
import br.com.gabrieltonhatti.entidades.Usuario;
import br.com.gabrieltonhatti.service.ContaService;
import br.com.gabrieltonhatti.service.UsuarioService;
import com.github.javafaker.Faker;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContaServiceTest {

    static Faker faker = new Faker();
    ContaService service = new ContaService();
    UsuarioService userService = new UsuarioService();

    @Test
    public void testInserir() throws Exception {
        Usuario usuario = new Usuario(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.internet().password()
        );

        usuario = userService.salvar(usuario);
        Conta conta = new Conta(faker.superhero().name(), usuario);
        Conta contaSalva = service.salvar(conta);

        assertNotNull(contaSalva.getId());
        userService.printAll();
        service.printAll();
    }

    @Test
    public void testAlterar() throws Exception {
        Conta contaTeste = service.findByName(new MassaDAOImpl().obterMassa(GeradorMassas.CHAVE_CONTA));

        service.printAll();

        String novoNome = faker.ancient().god() + " " + faker.ancient().titan();
        contaTeste.setNome(novoNome);
        Conta contaAlterada = service.salvar(contaTeste);

        assertEquals(novoNome, contaAlterada.getNome());
        service.printAll();
    }

    @Test
    public void testConsultar() throws Exception {
        String nomeConta = new MassaDAOImpl().obterMassa(GeradorMassas.CHAVE_CONTA);
        Conta contaTeste = service.findByName(nomeConta);

        Conta contaBuscada = service.findById(contaTeste.getId());
        assertEquals(contaTeste.getNome(), contaBuscada.getNome());
    }

    @Test
    public void testExcluir() throws Exception {
        Conta contaTeste = service.findByName(new MassaDAOImpl().obterMassa(GeradorMassas.CHAVE_CONTA));

        service.printAll();
        service.delete(contaTeste);

        Conta contaBuscada = service.findById(contaTeste.getId());

        assertNull(contaBuscada);
        service.printAll();
    }

}
