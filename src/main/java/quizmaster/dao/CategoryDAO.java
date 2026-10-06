package quizmaster.dao;

import quizmaster.model.Category;
import quizmaster.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Categories table.
 */
public class CategoryDAO {
    
    /**
     * Finds all categories ordered by name.
     * @return List of Category objects
     */
    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT CATEGORY_ID, CATEGORY_NAME FROM CATEGORIES ORDER BY CATEGORY_NAME";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("CATEGORY_ID"));
                cat.setCategoryName(rs.getString("CATEGORY_NAME"));
                list.add(cat);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all categories: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Finds a category by its ID.
     * @param categoryId the category ID
     * @return Category object if found, null otherwise
     */
    public Category findById(int categoryId) {
        String sql = "SELECT CATEGORY_ID, CATEGORY_NAME FROM CATEGORIES WHERE CATEGORY_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category cat = new Category();
                    cat.setCategoryId(rs.getInt("CATEGORY_ID"));
                    cat.setCategoryName(rs.getString("CATEGORY_NAME"));
                    return cat;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding category by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
