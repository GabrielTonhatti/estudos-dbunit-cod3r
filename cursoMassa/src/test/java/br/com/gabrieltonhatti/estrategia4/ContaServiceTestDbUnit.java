package br.com.gabrieltonhatti.estrategia4;

import br.com.gabrieltonhatti.dao.utils.ConnectionFactory;
import br.com.gabrieltonhatti.dbunit.ImportExport;
import br.com.gabrieltonhatti.entidades.Conta;
import br.com.gabrieltonhatti.entidades.Usuario;
import br.com.gabrieltonhatti.service.ContaService;
import br.com.gabrieltonhatti.service.UsuarioService;
import org.dbunit.Assertion;
import org.dbunit.assertion.DiffCollectingFailureHandler;
import org.dbunit.assertion.Difference;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.Assert.*;

public class ContaServiceTestDbUnit {

    ContaService service = new ContaService();
    UsuarioService userService = new UsuarioService();

    @Test
    public void testInserir() throws Exception {
        ImportExport.importarBanco("est4_inserirConta.xml");

        Usuario usuario = userService.findById(1L);
        Conta conta = new Conta("Conta salva", usuario);
        Conta contaSalva = service.salvar(conta);
        assertNotNull(contaSalva.getId());
    }

    @Test
    public void testInserir_Assertion() throws Exception {
        ImportExport.importarBanco("est4_inserirConta.xml");

        Usuario usuario = userService.findById(1L);
        Conta conta = new Conta("Conta salva", usuario);
        Conta contaSalva = service.salvar(conta);

        // Estado atual do banco
        DatabaseConnection dbConn = new DatabaseConnection(ConnectionFactory.getConnection());
        IDataSet estadoFinalBanco = dbConn.createDataSet();

        // Estado esperado (XML)
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        FlatXmlDataSet dataSetEsperado = builder
                .build(new FileInputStream("massas" + File.separator + "est4_inserirConta_saida.xml"));

        // Comparar os dois estados
        // Assertion.assertEquals(dataSetEsperado, estadoFinalBanco);

        DiffCollectingFailureHandler handler = new DiffCollectingFailureHandler();
        Assertion.assertEquals(dataSetEsperado, estadoFinalBanco, handler);
        List<Difference> erros = handler.getDiffList();
        boolean erroReal = false;

        for (Difference erro : erros) {
            System.out.println(erro.toString());
            if (erro.getActualTable().getTableMetaData().getTableName().equals("contas")) {
                if (erro.getColumnName().equals("id")) {
                    if (erro.getActualTable().toString().equals(contaSalva.getId().toString())) {
                        continue;
                    } else {
                        System.out.println("Id errado mesmo!");
                        erroReal = true;
                    }
                } else {
                    erroReal = true;
                }

            } else {
                erroReal = true;
            }
        }

        assertFalse(erroReal);

    }

    @Test
    public void testInserir_Filter() throws Exception {
        ImportExport.importarBanco("est4_inserirConta.xml");

        Usuario usuario = userService.findById(1L);
        Conta conta = new Conta("Conta salva", usuario);
        service.salvar(conta);

        // Estado atual do banco
        DatabaseConnection dbConn = new DatabaseConnection(ConnectionFactory.getConnection());
        IDataSet estadoFinalBanco = dbConn.createDataSet();

        // Estado esperado (XML)
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        FlatXmlDataSet dataSetEsperado = builder
                .build(new FileInputStream("massas" + File.separator + "est4_inserirConta_saida.xml"));

        // Comparar os dois estados
        // Assertion.assertEquals(dataSetEsperado, estadoFinalBanco);
        ITable contasAtualFiltradas = DefaultColumnFilter
                .excludedColumnsTable(estadoFinalBanco.getTable("contas"), new String[]{"id"});
        ITable contasEsperadoFiltradas = DefaultColumnFilter
                .excludedColumnsTable(dataSetEsperado.getTable("contas"), new String[]{"id"});

        ITable usuarioAtualFiltradas = DefaultColumnFilter
                .excludedColumnsTable(estadoFinalBanco.getTable("usuarios"), new String[]{"conta_principal_id"});
        ITable usuarioEsperadoFiltradas = DefaultColumnFilter
                .excludedColumnsTable(dataSetEsperado.getTable("usuarios"), new String[]{"conta_principal_id"});

        Assertion.assertEquals(contasAtualFiltradas, contasEsperadoFiltradas);
        Assertion.assertEquals(usuarioAtualFiltradas, usuarioEsperadoFiltradas);
    }

    @Test
    public void testAlterar() throws Exception {
        ImportExport.importarBanco("est4_umaConta.xml");

        Conta contaTeste = service.findByName("Conta para testes");
        contaTeste.setNome("Conta alterada");
        Conta contaAlterada = service.salvar(contaTeste);
        assertEquals("Conta alterada", contaAlterada.getNome());
        service.printAll();
    }

    @Test
    public void testConsultar() throws Exception {
        ImportExport.importarBanco("est4_umaConta.xml");

        Conta contaBuscada = service.findById(1L);
        assertEquals("Conta para testes", contaBuscada.getNome());
    }

    @Test
    public void testExcluir() throws Exception {
        ImportExport.importarBanco("est4_umaConta.xml");

        Conta contaTeste = service.findByName("Conta para testes");
        service.printAll();
        service.delete(contaTeste);
        Conta contaBuscada = service.findById(contaTeste.getId());
        assertNull(contaBuscada);
        service.printAll();
    }

}
