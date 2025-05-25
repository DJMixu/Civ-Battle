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
    private VBox statsPanel; // Panel z informacjami o cywilizacjach

    @Override
    public void start(Stage stage) {
        // Okno dialogowe do wprowadzenia danych od użytkownika
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Initialize Grid");
        dialog.setHeaderText("Podaj liczbę wierszy (N) i kolumn (M):\nPozostaw puste, aby użyć domyślnej siatki 5x5.");

        TextField rowsField = new TextField();
        rowsField.setPromptText("Wiersze (N)");
        TextField colsField = new TextField();
        colsField.setPromptText("Kolumny (M)");
        TextField civField = new TextField();
        civField.setPromptText("Liczba Cywilizacji");

        VBox inputBox = new VBox(10,
                new Label("Wiersze (N):"), rowsField,
                new Label("Kolumny (M):"), colsField,
                new Label("Liczba Cywilizacji:"), civField
        );
        inputBox.setAlignment(Pos.CENTER);

        dialog.getDialogPane().setContent(inputBox);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                try {
                    int rows = rowsField.getText().isEmpty() ? 20 : Integer.parseInt(rowsField.getText().trim());
                    int cols = colsField.getText().isEmpty() ? 20 : Integer.parseInt(colsField.getText().trim());
                    int civs = civField.getText().isEmpty() ? 4 : Integer.parseInt(civField.getText().trim());

                    return new int[]{rows, cols, civs};
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        int[] parameters = dialog.showAndWait().orElse(null);
        if (parameters == null || parameters.length != 3) {
            System.out.println("Brak poprawnych danych wejściowych. Zamykanie aplikacji.");
            return; // Wyjście, jeśli brak poprawnych danych
        }

        // Pobranie parametrów od użytkownika
        int rows =  parameters[0];
        int cols =  parameters[1];
        int civs =  parameters[2];

        // Inicjalizacja siatki i interfejsu użytkownika
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); // Wyśrodkowanie siatki

        Region leftSpacer = new Region();
        leftSpacer.prefWidthProperty().bind(stage.widthProperty().multiply(0.1)); // 10% szerokości okna

        HBox gridContainer = new HBox(leftSpacer, gridPane);
        gridContainer.setAlignment(Pos.CENTER); // Wyśrodkowanie poziome

        gridPane.prefWidthProperty().bind(stage.widthProperty().multiply(0.8));
        gridPane.prefHeightProperty().bind(stage.heightProperty().multiply(0.8));

        BorderPane root = new BorderPane();
        root.setLeft(gridContainer);

        statsPanel = new VBox(10);
        statsPanel.setStyle("-fx-padding: 20; -fx-border-color: black; -fx-border-width: 2;");
        statsPanel.setPrefWidth(300); // Stała szerokość panelu statystyk

        Symulacja initialSimulation = new Symulacja(rows, cols, civs);
        createGrid(rows, cols, initialSimulation);

        root.setRight(statsPanel);

        Scene scene = new Scene(root, 1920, 1080);
        stage.setTitle("Civilization Battle Simulator");
        stage.setScene(scene);
        stage.show();
    }

    private String getCivilizationColor(int civId) {
        // Przypisanie unikalnych kolorów dla każdej cywilizacji
        return switch (civId) {
            case 0 -> "blue";   // Egipt
            case 1 -> "green";  // Rzym
            case 2 -> "orange"; // Grecja
            case 3 -> "purple"; // Chiny
            case 4 -> "brown";  // Persja
            case 5 -> "pink";   // Majowie
            case 6 -> "cyan";   // Wikingowie
            case 7 -> "yellow"; // Japonia
            case 8 -> "gray";   // Polska
            case 9 -> "red";    // Barbarzynca
            default -> "black";
        };
    }


    private void createGrid(int rows, int cols, Symulacja symulacja) {
        // Czyszczenie starej siatki
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // Odświeżenie panelu statystyk
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

                // Pobranie obiektu z planszy
                Obiekt obiekt = symulacja.plansza.zwrocPole(j, i);
                String borderColor = "black"; // Domyślny kolor obramowania
                int borderWidth = 1; // Domyślna grubość obramowania

                if (obiekt != null) {
                    ImageView imageView = new ImageView();
                    imageView.setPreserveRatio(true);
                    imageView.fitWidthProperty().bind(cell.prefWidthProperty());
                    imageView.fitHeightProperty().bind(cell.prefHeightProperty());

                    String imagePath = null;

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

                    // Ustawienie koloru i grubości obramowania na podstawie cywilizacji
                    if (obiekt instanceof Jednostka) {
                        int civId = ((Jednostka) obiekt).idCywilizacji;
                        borderColor = getCivilizationColor(civId);
                    }

                    // Zastosowanie koloru i grubości obramowania
                    cell.setStyle("-fx-border-color: " + borderColor + "; -fx-border-width: " + 2 + "px; -fx-background-color: white;");

                    if (imagePath != null) {
                        try {
                            imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
                        } catch (NullPointerException e) {
                            System.err.println("Nie znaleziono obrazu: " + imagePath);
                        }
                    }

                    StackPane stackPane = new StackPane(cell, imageView);
                    gridPane.add(stackPane, j, i);
                } else {
                    // Zastosowanie koloru i grubości obramowania, nawet jeśli brak obiektu
                    cell.setStyle("-fx-border-color: " + borderColor + "; -fx-border-width: " + borderWidth + "px; -fx-background-color: white;");
                    gridPane.add(cell, j, i);
                }
            }
        }
    }


    private void updateStatsPanel(Symulacja symulacja) {
        // Czyszczenie starego panelu statystyk
        statsPanel.getChildren().clear();

        // Wyświetlanie statystyk dla każdej cywilizacji
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
