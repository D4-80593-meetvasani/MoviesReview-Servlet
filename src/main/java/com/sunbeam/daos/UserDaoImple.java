package com.sunbeam.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sunbeam.pojos.User;

public class UserDaoImple extends Dao implements UserDao{

	private PreparedStatement stmtSave;
	private PreparedStatement stmtUpdatePassword;
	private PreparedStatement stmtUpdate;
	private PreparedStatement stmtFindByEmail;
	private PreparedStatement stmtFindAll;

	public UserDaoImple() throws Exception {
		String sqlSave = "INSERT INTO Users VALUES(default, ?, ?, ?, ?, ?,?)";
		stmtSave = con.prepareStatement(sqlSave);

		String sqlFindByEmail = "SELECT * FROM Users where email=?";
		stmtFindByEmail = con.prepareStatement(sqlFindByEmail);

		String sqlUpdatePassword = "UPDATE Users SET password = ? WHERE email = ?";
		stmtUpdatePassword = con.prepareStatement(sqlUpdatePassword);

		String sqlUpdate = "UPDATE Users SET first_name=?,last_name=?,email=? WHERE id = ?";
		stmtUpdate = con.prepareStatement(sqlUpdate);

		String sqlFindAll = "SELECT * from Users";
		stmtFindAll = con.prepareStatement(sqlFindAll);
	}

	public int save(User u) throws Exception {
		stmtSave.setString(1, u.getFirstName());
		stmtSave.setString(2, u.getLastName());
		stmtSave.setString(3, u.getEmail());
		stmtSave.setString(4, u.getPassword());
		stmtSave.setString(5, u.getMobile());

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date uDate = sdf.parse(u.getBirth());
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		stmtSave.setDate(6, sDate);

		int count = stmtSave.executeUpdate();

		return count;

	}

	public List<User> findAll() throws Exception {
		List<User> list = new ArrayList<User>();
		try (ResultSet rs = stmtFindAll.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String fname = rs.getString("first_name");
				String lname = rs.getString("last_name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String mobile = rs.getString("mobile");

				String birth = rs.getString("birth");

				User u = new User(id, fname, lname, email, password, mobile, birth);
				list.add(u);
			}
		} // rs.close()
		return list;
	}

	public Optional<User> findByEmail(String email) throws Exception {
		stmtFindByEmail.setString(1, email);
		try (ResultSet rs = stmtFindByEmail.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String fname = rs.getString("first_name");
				String lname = rs.getString("last_name");
				String password = rs.getString("password");
				String mobile = rs.getString("mobile");

				String birth = rs.getString("birth");

				User u = new User(id, fname, lname, email, password, mobile, birth);
				
				return Optional.of(u);
				
//				return new User(id, fname, lname, email, password, mobile, birth);
			}
		} // rs.close();
		return Optional.empty();
	}

	public int updatePassword(String email, String newPassword) throws Exception {
		stmtUpdatePassword.setString(1, newPassword);
		stmtUpdatePassword.setString(2, email);
		int count = stmtUpdatePassword.executeUpdate();
		return count;
	}

	public int update(int id, String newFName, String newLName, String newEmail) throws Exception {
		stmtUpdate.setString(1, newFName);
		stmtUpdate.setString(2, newLName);
		stmtUpdate.setString(3, newEmail);
		stmtUpdate.setInt(4, id);
		int count = stmtUpdate.executeUpdate();
		return count;
	}



}
