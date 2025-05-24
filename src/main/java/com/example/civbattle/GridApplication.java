package com.example.civbattle;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;

public class GridApplication extends Application {

    private GridPane gridPane;
    private VBox statsPanel; // Panel statystyk cywilizacji
    private Cywilizacja Egipt; // Cywilizacja Egipt
    private Cywilizacja Rzym; // Cywilizacja Rzym

    @Override
    public void start(Stage stage) {
        // Inicjalizacja cywilizacji
        Egipt = new Cywilizacja(0);
        Egipt.surowce = new int[]{100, 200, 300};
        Rzym = new Cywilizacja(1);
        Rzym.surowce = new int[]{2, 3, 1};

        // Tworzenie siatki
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); // Wyśrodkowanie siatki

        // Dynamiczny odstęp po lewej stronie
        Region leftSpacer = new Region();
        leftSpacer.prefWidthProperty().bind(stage.widthProperty().multiply(0.1)); // 10% szerokości okna

        // Kontener dla odstępu i siatki
        HBox gridContainer = new HBox(leftSpacer, gridPane);
        gridContainer.setAlignment(Pos.CENTER); // Wyśrodkowanie poziome siatki

        // Powiązanie rozmiaru siatki z pozostałą przestrzenią
        gridPane.prefWidthProperty().bind(stage.widthProperty().multiply(0.8));
        gridPane.prefHeightProperty().bind(stage.heightProperty().multiply(0.8));

        // Pionowa linia podziału
        Line vdiv = new Line();
        vdiv.setStyle("-fx-stroke: black;");
        vdiv.endYProperty().bind(stage.heightProperty());

        // Pozycjonowanie elementów
        BorderPane root = new BorderPane();
        root.setLeft(gridContainer);
        root.setCenter(vdiv);

        // Przycisk zmiany rozmiaru siatki
        Button resizeButton = new Button("Resize Grid");
        resizeButton.setOnAction(e -> showResizeDialog());
        root.setTop(resizeButton);

        // Panel statystyk
        statsPanel = new VBox(10);
        statsPanel.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 1;");
        statsPanel.setPrefWidth(200); // Stała szerokość panelu
        updateStatsPanel(); // Wypełnienie panelu danymi

        // Dodanie panelu statystyk po prawej stronie
        root.setRight(statsPanel);

        // Ustawienia sceny
        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Civilization Battle Simulator");
        stage.setScene(scene);
        stage.show();

        // Domyślny rozmiar siatki
        createGrid(10, 10);
    }

    private void showResizeDialog() {
        // Okno dialogowe zmiany rozmiaru siatki
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
                    // Domyślna siatka 5x5
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
        // Czyszczenie poprzedniej siatki
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // Ustawienie proporcji siatki
        gridPane.maxWidthProperty().bind(Bindings.min(gridPane.prefWidthProperty(), gridPane.prefHeightProperty()));
        gridPane.maxHeightProperty().bind(gridPane.maxWidthProperty());

        // Dodanie wierszy
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / rows);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        // Dodanie kolumn
        for (int j = 0; j < cols; j++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / cols);
            gridPane.getColumnConstraints().add(colConstraints);
        }

        // Tworzenie komórek siatki
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Region cell = new Region();
                cell.setStyle("-fx-border-color: black; -fx-background-color: white;");

                // Kwadratowe komórki
                cell.prefWidthProperty().bind(Bindings.min(
                        gridPane.widthProperty().divide(cols),
                        gridPane.heightProperty().divide(rows)
                ));
                cell.prefHeightProperty().bind(cell.prefWidthProperty());

                gridPane.add(cell, j, i);
            }
        }
    }

    private void updateStatsPanel() {
        statsPanel.getChildren().clear(); // Czyszczenie poprzednich statystyk

        // Statystyki Egiptu
        statsPanel.getChildren().add(new Text("Cywilizacja: " + Egipt.nameCywilizacji));
        statsPanel.getChildren().add(new Text("Surowce: " + Arrays.toString(Egipt.surowce)));
        statsPanel.getChildren().add(new Text("Jednostki: " + (Egipt.jednostki != null ? Egipt.jednostki.size() : 0)));
        statsPanel.getChildren().add(new Text("Osady: " + (Egipt.osady != null ? Egipt.osady.size() : 0)));
        statsPanel.getChildren().add(new Separator());

        // Statystyki Rzymu
        statsPanel.getChildren().add(new Text("Cywilizacja: " + Rzym.nameCywilizacji));
        statsPanel.getChildren().add(new Text("Surowce: " + Arrays.toString(Rzym.surowce)));
        statsPanel.getChildren().add(new Text("Jednostki: " + (Rzym.jednostki != null ? Rzym.jednostki.size() : 0)));
        statsPanel.getChildren().add(new Text("Osady: " + (Rzym.osady != null ? Rzym.osady.size() : 0)));
        statsPanel.getChildren().add(new Separator());
    }

    public static void main(String[] args) {
        System.out.println("start");
        Symulacja symulacja = new Symulacja(20,20,5,"Ala ma kota");

        launch();

    }
}

