package com.sunbeam.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sunbeam.pojos.Movies;

public class MovieDaoImple extends Dao implements MovieDao{
	private PreparedStatement stmtFindAll;


	public MovieDaoImple() throws Exception{

		String sqlFindAll = "SELECT * from Movies";
		stmtFindAll = con.prepareStatement(sqlFindAll);

	}
	
	public List<Movies> findAll() throws Exception {
		List<Movies> list = new ArrayList<Movies>();
		try (ResultSet rs = stmtFindAll.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String release = rs.getString("release_date");
				Movies m = new Movies(id, title, release);
				list.add(m);
			}
		} // rs.close()
		return list;
	}

}
