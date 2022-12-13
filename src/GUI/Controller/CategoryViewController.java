package GUI.Controller;

import BE.Category;
import GUI.Model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CategoryViewController extends BaseController {
    @FXML
    private TableColumn<?, ?> categoryColumn;
    @FXML
    private TableView<Category> categoriesTable;
    @FXML
    private TextField categoryName;
    @FXML
    private Button cxlBtn;
    @FXML
    private Button saveBtn;
    private SongModel model;

    @Override
    public void setup() {
        model = getModel().getSongModel();
        updateCategoryTable();
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cxlBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {
        try {
            String newCategory = categoryName.getText();
            model.createCategory(newCategory);
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes chosen category
     * @param actionEvent when the button is clicked
     */
    @FXML
    public void handleDelete(ActionEvent actionEvent) {
        Category c = categoriesTable.getFocusModel().getFocusedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + c.getName() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        try {
            if (alert.getResult() == ButtonType.YES) {
                model.deleteCategory(c);
                updateCategoryTable();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCategoryTable() {
        model = getModel().getSongModel();
        try {
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

            categoriesTable.getColumns().addAll();
            categoriesTable.setItems(model.getObservableCategories());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}