package com.example.civbattle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

public class CivBattler extends Application {

    private GridPane gridPane;
    private VBox statsPanel; // Panel z informacjami o cywilizacjach
    private Timeline simulationTimeline; // Automatyczna aktualizacja siatki

    @Override
    public void start(Stage stage) {
        // Okno dialogowe do wprowadzenia danych od użytkownika
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Stwórz Siatkę");
        dialog.setHeaderText("Podaj liczbę wierszy (N) i kolumn (M):\nPozostaw puste, aby użyć domyślnej siatki.");

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
                    int rows = rowsField.getText().isEmpty() ? 24 : Integer.parseInt(rowsField.getText().trim());
                    int cols = colsField.getText().isEmpty() ? 24 : Integer.parseInt(colsField.getText().trim());
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
        int rows = parameters[0];
        int cols = parameters[1];
        int civs = parameters[2];

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

        // Ustawienie automatycznej aktualizacji siatki co 0.5 sekundy
        simulationTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            initialSimulation.krokSymulacji();
            createGrid(rows, cols, initialSimulation);
        }));
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
        simulationTimeline.play();

        root.setRight(statsPanel);

        Scene scene = new Scene(root, 1920, 1080);
        stage.setTitle("Civilization Battle Simulator");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // Zatrzymanie symulacji przy zamknięciu aplikacji
        if (simulationTimeline != null) {
            simulationTimeline.stop();
        }
    }

    private String getCivilizationColor(int civId) {
        // Przypisanie unikalnych kolorów dla każdej cywilizacji
        return switch (civId) {
            case 0 -> "#0044cc"; // Egipt
            case 1 -> "#008800"; // Rzym
            case 2 -> "#ff6600"; // Grecja
            case 3 -> "#800080"; // Chiny
            case 4 -> "#5c4033"; // Persja
            case 5 -> "#cc0066"; // Majowie
            case 6 -> "#008b8b"; // Wikingowie
            case 7 -> "#b59f00"; // Japonia
            case 8 -> "#555555"; // Polska
            case 9 -> "#cc0000"; // Barbarzyńca
            default -> "#000000"; // Domyślnie czarny
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
                        int typ = ((Surowiec) obiekt).pTyp;
                        switch (typ) {
                            case 0:
                                imagePath = "/images/stone.png";
                                break;
                            case 1:
                                imagePath = "/images/drzewo.png";
                                break;
                            case 2:
                                imagePath = "/images/ETCS.png";
                                break;
                            default:
                                imagePath = "/images/stone.png"; //
                        }
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
                    cell.setStyle("-fx-border-color: " + borderColor + "; -fx-border-width: " + 3 + "px; -fx-background-color: white;");

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
        statsPanel.getChildren().clear();

        for (Cywilizacja civ : symulacja.listaCywilizacji) {
            if (civ != null) {
                String color = getCivilizationColor(civ.idCywilizacji);
                Text civName = new Text("Cywilizacja: " + civ.nameCywilizacji);
                civName.setStyle("-fx-fill: " + color + "; -fx-font-weight: bold;");

                // Surowce z obrazkami
                HBox surowceBox = new HBox(10);
                surowceBox.setAlignment(Pos.CENTER_LEFT);


                String[] imgPaths = {"/images/stone.png", "/images/drzewo.png", "/images/ETCS.png"};
                String[] tooltips = {"Kamień", "Drewno", "Złoto"};

                for (int i = 0; i < civ.surowce.length && i < imgPaths.length; i++) {
                    ImageView icon = new ImageView();
                    try {
                        icon.setImage(new Image(getClass().getResource(imgPaths[i]).toExternalForm()));
                    } catch (Exception e) {
                    }
                    icon.setFitWidth(24);
                    icon.setFitHeight(24);
                    Label label = new Label(String.valueOf(civ.surowce[i]), icon);
                    label.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
                    label.setContentDisplay(ContentDisplay.LEFT);
                    label.setTooltip(new Tooltip(tooltips[i]));
                    surowceBox.getChildren().add(label);
                }

                Text jednostki = new Text("Osadnicy: " + (civ.licznikOsadnikow));
                jednostki.setStyle("-fx-fill: " + color + ";");
                Text wojownicy = new Text("Wojownicy: " + (civ.licznikWojownikow));
                wojownicy.setStyle("-fx-fill: " + color + ";");
                Text osady = new Text("Osady: " + (civ.licznikOsad));
                osady.setStyle("-fx-fill: " + color + ";");

                statsPanel.getChildren().addAll(civName, surowceBox, jednostki, wojownicy, osady, new Separator());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("start");
        launch();
    }
}
