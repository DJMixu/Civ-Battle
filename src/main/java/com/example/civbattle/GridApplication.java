package com.example.civbattle;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class GridApplication extends Application {

    private GridPane gridPane;

    @Override
    public void start(Stage stage) {
        // Create the grid
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); // Ensure the grid is centered

        // dynamic spacer
        Region leftSpacer = new Region();
        leftSpacer.prefWidthProperty().bind(stage.widthProperty().multiply(0.1)); // 10% of stage width

        // container for the spacer and grid
        HBox gridContainer = new HBox(leftSpacer, gridPane);
        gridContainer.setAlignment(Pos.CENTER); // Center the grid horizontally

        // Bind the gridPane size to the remaining space in the container
        gridPane.prefWidthProperty().bind(stage.widthProperty().multiply(0.8));
        gridPane.prefHeightProperty().bind(stage.heightProperty().multiply(0.8));

        // Divide the space
        Line vdiv = new Line();
        vdiv.setStyle("-fx-stroke: black;");
        vdiv.endYProperty().bind(stage.heightProperty());

        // Position elements
        BorderPane root = new BorderPane();
        root.setLeft(gridContainer);
        root.setCenter(vdiv);

        // Button grid size
        Button resizeButton = new Button("Resize Grid");
        resizeButton.setOnAction(e -> showResizeDialog());
        root.setTop(resizeButton);

        // Scene setup
        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Civilization Battle Simulator");
        stage.setScene(scene);
        stage.show();

        // Grid default size
        createGrid(10, 10);
    }

    private void showResizeDialog() {
        // Resize dialog
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Resize Grid");
        dialog.setHeaderText("Enter the new number of rows (N) and columns (M):\n Leave empty for a 5x5 grid.");

        TextField rowsField = new TextField();
        rowsField.setPromptText("Rows (N)");
        TextField colsField = new TextField();
        colsField.setPromptText("Columns (M)");

        VBox inputBox = new VBox(10, new Label("Rows (N):"), rowsField, new Label("Columns (M):"), colsField);
        inputBox.setAlignment(Pos.CENTER);

        dialog.getDialogPane().setContent(inputBox);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                String rowsInput = rowsField.getText().trim();
                String colsInput = colsField.getText().trim();

                if ("".equalsIgnoreCase(rowsInput) || "".equalsIgnoreCase(colsInput)) {
                    // Default square grid
                    return new int[]{5, 5};
                }

                try {
                    int rows = Integer.parseInt(rowsInput);
                    int cols = Integer.parseInt(colsInput);
                    return new int[]{rows, cols};
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        int[] newSize = dialog.showAndWait().orElse(null);
        if (newSize != null && newSize.length == 2) {
            createGrid(newSize[0], newSize[1]);
        }
    }

    private void createGrid(int rows, int cols) {
        // Clear previous grid
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // Make square
        gridPane.maxWidthProperty().bind(Bindings.min(gridPane.prefWidthProperty(), gridPane.prefHeightProperty()));
        gridPane.maxHeightProperty().bind(gridPane.maxWidthProperty());

        // Row column constraints
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / rows);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        for (int j = 0; j < cols; j++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / cols);
            gridPane.getColumnConstraints().add(colConstraints);
        }

        // Make the grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Region cell = new Region();
                cell.setStyle("-fx-border-color: black; -fx-background-color: white;");

                // Square
                cell.prefWidthProperty().bind(Bindings.min(
                        gridPane.widthProperty().divide(cols),
                        gridPane.heightProperty().divide(rows)
                ));
                cell.prefHeightProperty().bind(cell.prefWidthProperty());

                gridPane.add(cell, j, i);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

