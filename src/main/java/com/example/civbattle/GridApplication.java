package com.example.civbattle;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;

public class GridApplication extends Application {

    private GridPane gridPane;
    private VBox statsPanel; // Panel statystyk cywilizacji
//    private Cywilizacja Egipt; // Cywilizacja Egipt
//    private Cywilizacja Rzym; // Cywilizacja Rzym
//    private Cywilizacja Grecja; // Cywilizacja Grecja

    @Override
    public void start(Stage stage) {
//        // Inicjalizacja cywilizacji
//        Egipt = new Cywilizacja(0);
//        Egipt.surowce = new int[]{100, 200, 300};
//        Rzym = new Cywilizacja(1);
//        Rzym.surowce = new int[]{2, 3, 1};
//        Grecja = new Cywilizacja(2);
//        Grecja.surowce = new int[]{5, 5, 5};


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

        // Pozycjonowanie elementów
        BorderPane root = new BorderPane();
        root.setLeft(gridContainer);

        // Przycisk zmiany rozmiaru siatki
        Button resizeButton = new Button("Resize Grid");
        resizeButton.setOnAction(e -> showResizeDialog());
        root.setTop(resizeButton);

        // Panel statystyk
        statsPanel = new VBox(10);
        statsPanel.setStyle("-fx-padding: 20; -fx-border-color: black; -fx-border-width: 2;");
        statsPanel.setPrefWidth(300); // Stała szerokość panelu

        // Domyślny rozmiar siatki
        Symulacja initialSimulation = new Symulacja(10, 10, 2, "Ala ma kota");
        createGrid(10, 10, initialSimulation);

        // Dodanie panelu statystyk po prawej stronie
        root.setRight(statsPanel);

        // Ustawienia sceny
        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Civilization Battle Simulator");
        stage.setScene(scene);
        stage.show();
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
            Symulacja newSimulation = new Symulacja(newSize[0], newSize[1], 2, "Ala ma kota");
            createGrid(newSize[0], newSize[1], newSimulation);
        }
    }

    private void createGrid(int rows, int cols, Symulacja symulacja) {
        // Czyszczenie poprzedniej siatki
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // Aktualizacja panelu statystyk
        updateStatsPanel(symulacja);

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

                // Pobieranie obiektu z planszy
                Obiekt obiekt = symulacja.plansza.zwrocPole(j, i);
                if (obiekt != null) {
                    ImageView imageView = new ImageView();
                    imageView.setPreserveRatio(true);
                    imageView.fitWidthProperty().bind(cell.prefWidthProperty());
                    imageView.fitHeightProperty().bind(cell.prefHeightProperty());

                    // Use classpath resources for images
                    String imagePath = null;
                    String borderColor = "black"; // Default border color
                    int borderWidth = 1; // Default border width

                    if (obiekt instanceof Wojownik) {
                        imagePath = "/images/wojownik.png";
                    } else if (obiekt instanceof Osadnik) {
                        imagePath = "/images/osadnik.png";
                    } else if (obiekt instanceof Surowiec) {
                        imagePath = "/images/surowiec.png";
                    } else if (obiekt instanceof Osada) {
                        imagePath = "/images/osada.png";
                    } else if (obiekt instanceof Barbarzynca) {
                        imagePath = "/images/barbarzynca.png";
                    }

                    // Assign border color and width based on civilization
                    if (obiekt instanceof Jednostka) {
                        int civId = ((Jednostka) obiekt).idCywilizacji;
                        borderColor = getCivilizationColor(civId);
                        borderWidth = getCivilizationBorderWidth(civId);
                    }

                    // Apply border color and width
                    cell.setStyle("-fx-border-color: " + borderColor + "; -fx-border-width: " + borderWidth + "px; -fx-background-color: white;");

                    if (imagePath != null) {
                        try {
                            imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
                        } catch (NullPointerException e) {
                            System.err.println("Image not found: " + imagePath);
                        }
                    }

                    StackPane stackPane = new StackPane(cell, imageView);
                    gridPane.add(stackPane, j, i);
                } else {
                    gridPane.add(cell, j, i);
                }
            }
        }
    }

    private String getCivilizationColor(int civId) {
        // Assign unique colors for each civilization
        return switch (civId) {
            case 0 -> "blue";   // Egipt
            case 1 -> "green";    // Rzym
            case 9 -> "red";   // Barbarzynca
            default -> "black"; // Other civilizations
        };
    }

    private int getCivilizationBorderWidth(int civId) {
        // Assign unique border widths for each civilization
        return switch (civId) {
            case 0 -> 3;   // Egipt
            case 1 -> 4;   // Rzym
            case 9 -> 2;   // Barbarzynca
            default -> 2;  // Other civilizations
        };
    }

    private void updateStatsPanel(Symulacja symulacja) {
        // Czyszczenie poprzednich statystyk
        statsPanel.getChildren().clear();

        // Statystyki dla każdej cywilizacji
        for (Cywilizacja civ : symulacja.listaCywilizacji) {
            if (civ != null) {
                statsPanel.getChildren().add(new Text("Cywilizacja: " + civ.nameCywilizacji));
                statsPanel.getChildren().add(new Text("Surowce: " + Arrays.toString(civ.surowce)));
                statsPanel.getChildren().add(new Text("Jednostki: " + (civ.jednostki != null ? civ.jednostki.size() : 0)));
                statsPanel.getChildren().add(new Text("Osady: " + (civ.osady != null ? civ.osady.size() : 0)));
                statsPanel.getChildren().add(new Separator());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("start");
        launch();
    }
}

