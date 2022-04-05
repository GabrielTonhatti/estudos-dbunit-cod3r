package br.com.gabrieltonhatti.dbunit;

import br.com.gabrieltonhatti.dao.utils.ConnectionFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ImportExport {

    public static void main(String[] args) throws Exception {
        exportarBanco("saldo.xml");
//        importarBanco("saida.xml");
    }

    public static void importarBanco(String massa) throws DatabaseUnitException, SQLException, ClassNotFoundException, FileNotFoundException {
        DatabaseConnection dbConn = new DatabaseConnection(ConnectionFactory.getConnection());
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(new FileInputStream("massas" + File.separator + massa));

        List<String> tabelas = obterTabelas();
        desabilitaTriggers(tabelas);
        DatabaseOperation
                .CLEAN_INSERT
                .execute(dbConn, dataSet);
        habilitarTrigger(tabelas);
        atualizarSequence(tabelas);
    }

    private static void atualizarSequence(List<String> tabelas) throws SQLException, ClassNotFoundException {
        for (String tabela : tabelas) {
            Statement stmt = ConnectionFactory
                    .getConnection()
                    .createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM public." + tabela);
            rs.next();
            Long id = rs.getLong(1);
            rs.close();
            stmt.close();

            if (id > 0) {
                stmt = ConnectionFactory
                        .getConnection()
                        .createStatement();
                System.out.println(tabela + " > " + (id + 1));
                stmt.executeUpdate("ALTER SEQUENCE " + tabela + "_id_seq RESTART WITH " + (id + 1));
                stmt.close();
            }
        }

    }

    private static void habilitarTrigger(List<String> tabelas) throws SQLException, ClassNotFoundException {
        for (String tabela : tabelas) {
            System.out.println("++" + tabela);
            Statement stmt = ConnectionFactory
                    .getConnection()
                    .createStatement();
            stmt.executeUpdate("ALTER TABLE public." + tabela + " ENABLE TRIGGER ALL");
            stmt.close();
        }
    }

    private static void desabilitaTriggers(List<String> tabelas) throws SQLException, ClassNotFoundException {
        for (String tabela : tabelas) {
            System.out.println("--" + tabela);
            Statement stmt = ConnectionFactory
                    .getConnection()
                    .createStatement();
            stmt.executeUpdate("ALTER TABLE public." + tabela + " DISABLE TRIGGER ALL");
            stmt.close();
        }
    }

    private static List<String> obterTabelas() throws SQLException, ClassNotFoundException {
        List<String> tabelas = new ArrayList<String>();
        ResultSet rs = ConnectionFactory.getConnection()
                .createStatement()
                .executeQuery(
                        "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'"
                );

        while (rs.next()) {
            tabelas.add(rs.getString("table_name"));
        }

        rs.close();

        return tabelas;
    }

    public static void exportarBanco(String massa) throws Exception {
        DatabaseConnection dbConn = new DatabaseConnection(ConnectionFactory.getConnection());
        IDataSet dataSet = dbConn.createDataSet();

        DatabaseSequenceFilter databaseSequenceFilter = new DatabaseSequenceFilter(dbConn);
        FilteredDataSet filteredDataSet = new FilteredDataSet(databaseSequenceFilter, dataSet);

        FileOutputStream fos = new FileOutputStream("massas" + File.separator + massa);
//        FlatXmlDataSet.write(dataSet, fos);
        FlatXmlDataSet.write(filteredDataSet, fos);
    }

}
