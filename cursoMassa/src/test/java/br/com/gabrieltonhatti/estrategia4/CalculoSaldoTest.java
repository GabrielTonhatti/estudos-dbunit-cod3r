package br.com.gabrieltonhatti.estrategia4;

import br.com.gabrieltonhatti.dao.SaldoDAO;
import br.com.gabrieltonhatti.dao.impl.SaldoDAOImpl;
import br.com.gabrieltonhatti.dao.utils.ConnectionFactory;
import br.com.gabrieltonhatti.dbunit.ImportExport;
import br.com.gabrieltonhatti.utils.DataUtils;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;

public class CalculoSaldoTest {

    // 1 Usuário
    // 1 conta
    // 1 transação

    // deve considerar transações do mesmo usuário
    // deve considerar transações da mesma conta
    // deve considerar transações pagas
    // deve considerar transações até a data corrente
    // deve somar receitas
    // deve subtrair despesas

    @Test
    public void deveCalcularSaldoCorreto() throws Exception {
//        ImportExport.importarBanco("saldo.xml");
        DatabaseConnection dbConn = new DatabaseConnection(ConnectionFactory.getConnection());
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(new FileInputStream("massas" + File.separator + "saldo.xml"));

        ReplacementDataSet dataSetAlterado = new ReplacementDataSet(dataSet);
        dataSetAlterado.addReplacementObject("[hoje]", new Date());
        dataSetAlterado.addReplacementObject("[ontem]", DataUtils.obterDatacomDiferencaDias(-1));
        dataSetAlterado.addReplacementObject("[amanha]", DataUtils.obterDatacomDiferencaDias(1));

        DatabaseOperation
                .CLEAN_INSERT
                .execute(dbConn, dataSetAlterado);
        SaldoDAO saldoDAO = new SaldoDAOImpl();
        Assert.assertEquals(Double.valueOf(162.0), saldoDAO.getSaldoConta(1L));
        Assert.assertEquals(Double.valueOf(8.0), saldoDAO.getSaldoConta(2L));
        Assert.assertEquals(Double.valueOf(4.0), saldoDAO.getSaldoConta(3L));

    }

}
