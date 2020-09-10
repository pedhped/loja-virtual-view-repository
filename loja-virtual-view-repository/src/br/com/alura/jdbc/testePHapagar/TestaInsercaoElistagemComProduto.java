package br.com.alura.jdbc.testePHapagar;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.com.alura.jdbc.dao.ProdutoDAO;
import br.com.alura.jdbc.factory.ConnectionFactory;
import br.com.alura.jdbc.modelo.Produto;


/*ESTE MODELO DE CLASSE PODE SER USADO EM PROJETOS REAIS POIS USA UM PADRÃO ACEITO;*/
public class TestaInsercaoElistagemComProduto {

	public static void main(String[] args) throws SQLException {

		Produto comoda = new Produto("Geladeira", "Consul Azul");

		try (Connection connection = new ConnectionFactory().recuperarConexao()) {

			ProdutoDAO produtoDAO = new ProdutoDAO(connection);
			
			//Salva Produto
			produtoDAO.salvar(comoda);
			
			//Lista Produtos
			List<Produto> listaDeProdutos = produtoDAO.listar();
			
			listaDeProdutos.stream().forEach(lp->System.out.println(lp));/*Usando forEach com lambda*/
		}
	}

}
