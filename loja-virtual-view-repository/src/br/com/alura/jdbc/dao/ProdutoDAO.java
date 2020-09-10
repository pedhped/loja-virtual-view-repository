package br.com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.alura.jdbc.modelo.Categoria;
import br.com.alura.jdbc.modelo.Produto;

/*ESTE MODELO DE CLASSE PODE SER USADO EM PROJETOS REAIS POIS USA UM PADR�O ACEITO;*/

/*Objeto de acesso a dados (acr�nimo do ingl�s Data Access Object - DAO), 
 * � um padr�o para aplica��es que utilizam persist�ncia de dados, 
 * onde tem a separa��o das regras de neg�cio das regras de acesso a banco de dados, 
 * implementada com linguagens de programa��o orientadas a objetos (como por exemplo Java) e arquitetura*/

public class ProdutoDAO {

	private Connection connection;

	public ProdutoDAO(Connection connection) {
		this.connection = connection;
	}

	public void salvar(Produto produto) {
		try {
			String sql = "INSERT INTO PRODUTO (NOME, DESCRICAO) VALUES (?, ?)";

			try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

				pstm.setString(1, produto.getNome());
				pstm.setString(2, produto.getDescricao());

				pstm.execute();
				
				// Recuperando o id do registro inserido
				try (ResultSet rst = pstm.getGeneratedKeys()) {
					while (rst.next()) {
						produto.setId(rst.getInt(1));
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public void salvarComCategoria(Produto produto) {
		try {
			String sql = "INSERT INTO PRODUTO (NOME, DESCRICAO, CATEGORIA_ID) VALUES (?, ?, ?)";

			try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

				pstm.setString(1, produto.getNome());
				pstm.setString(2, produto.getDescricao());
				pstm.setInt(3, produto.getCategoriaId());

				pstm.execute();

				try (ResultSet rst = pstm.getGeneratedKeys()) {
					while (rst.next()) {
						produto.setId(rst.getInt(1));
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public List<Produto> listar() {
		List<Produto> produtos = new ArrayList<Produto>();
		try {
			String sql = "SELECT ID, NOME, DESCRICAO FROM PRODUTO";

			try (PreparedStatement pstm = connection.prepareStatement(sql)) {
				pstm.execute();

				trasformarResultSetEmProduto(produtos, pstm);
			}
			return produtos;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Produto> buscar(Categoria ct) {
		List<Produto> produtos = new ArrayList<Produto>();
		try {

			String sql = "SELECT ID, NOME, DESCRICAO FROM PRODUTO WHERE CATEGORIA_ID = ?";

			try (PreparedStatement pstm = connection.prepareStatement(sql)) {
				pstm.setInt(1, ct.getId());
				pstm.execute();

				trasformarResultSetEmProduto(produtos, pstm);
			}
			return produtos;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void deletar(Integer id) {
		try (PreparedStatement stm = connection.prepareStatement("DELETE FROM PRODUTO WHERE ID = ?")) {
			stm.setInt(1, id);
			stm.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void alterar(String nome, String descricao, Integer id) {
		try (PreparedStatement stm = connection
				.prepareStatement("UPDATE PRODUTO P SET P.NOME = ?, P.DESCRICAO = ? WHERE ID = ?")) {
			stm.setString(1, nome);
			stm.setString(2, descricao);
			stm.setInt(3, id);
			stm.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void trasformarResultSetEmProduto(List<Produto> produtos, PreparedStatement pstm) throws SQLException {
		try (ResultSet rst = pstm.getResultSet()) {
			while (rst.next()) {
				Produto produto = new Produto(rst.getInt(1), rst.getString(2), rst.getString(3));

				produtos.add(produto);
			}
		}
	}
}
