package br.com.gabrieltonhatti.estrategia5;

import br.com.gabrieltonhatti.dbunit.ImportExport;
import br.com.gabrieltonhatti.entidades.Conta;
import br.com.gabrieltonhatti.entidades.Usuario;
import br.com.gabrieltonhatti.service.ContaService;
import br.com.gabrieltonhatti.service.UsuarioService;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContaServiceTestDbUnit {

    ContaService service = new ContaService();
    UsuarioService userService = new UsuarioService();

    @BeforeClass
    public static void inserirConta() throws Exception {
        ImportExport.importarBanco("est5.xml");
    }

    @Test
    public void testInserir() throws Exception {
        Usuario usuario = userService.findById(1L);
        Conta conta = new Conta("Conta salva", usuario);
        Conta contaSalva = service.salvar(conta);

        assertNotNull(contaSalva.getId());
        userService.printAll();
        service.printAll();
    }

    @Test
    public void testAlterar() throws Exception {
        Conta contaTeste = service.findByName("Conta CT005 alteração");
        contaTeste.setNome("Conta alterada");
        Conta contaAlterada = service.salvar(contaTeste);
        assertEquals("Conta alterada", contaAlterada.getNome());
        service.printAll();
    }

    @Test
    public void testConsultar() throws Exception {
        Conta contaBuscada = service.findById(1L);
        assertEquals("Conta para testes", contaBuscada.getNome());
    }

    @Test
    public void testExcluir() throws Exception {
        Conta contaTeste = service.findByName("Conta para deletar");
        service.printAll();
        service.delete(contaTeste);
        Conta contaBuscada = service.findById(contaTeste.getId());
        assertNull(contaBuscada);
        service.printAll();
    }

}
