package quizmaster.model;

/**
 * Represents a row in the CATEGORIES table.
 *
 * Columns: CATEGORY_ID, CATEGORY_NAME
 *
 * IMPORTANT: Category IDs may have gaps (e.g. 1, 3, 7, 12).
 *            Never hardcode category IDs — always query them from the database.
 */
public class Category {

    private int categoryId;
    private String categoryName;

    public Category() {
    }

    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // --- Getters and Setters ---

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Used by JComboBox to display the category name.
     */
    @Override
    public String toString() {
        return categoryName;
    }
}
