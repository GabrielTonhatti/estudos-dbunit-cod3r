package br.com.gabrieltonhatti.estrategia3;

import java.sql.SQLException;

public class EstoqueBatch {

    private static final Integer ESTOQUE_MINIMO = 5;
    private static final String TIPO_MONITORADO = GeradorMassas.CHAVE_CONTA_SB;

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
        MassaDAOImpl dao = new MassaDAOImpl();
        GeradorMassas gerador = new GeradorMassas();

        while (true) {
            int estoqueAtual = dao.obterEstoque(TIPO_MONITORADO);
            System.out.println(estoqueAtual);

            if (estoqueAtual < ESTOQUE_MINIMO) {
                gerador.gerarContaSeuBarriga();
            } else {
                Thread.sleep(10000);
            }

        }
    }

}
