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
import java.io.FileWriter;

public class CivBattler extends Application {

    private GridPane gridPane;
    private VBox statsPanel;
    private Timeline simulationTimeline;

    @Override
    public void start(Stage stage) {
        // okno dialogowe
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Stwórz Siatkę");
        dialog.setHeaderText("Podaj parametry symulacji:\nPozostaw puste, aby użyć domyślnych 24x24 - 4 cywilizacje.");

        TextField rowsField = new TextField();
        rowsField.setPromptText("Wiersze");
        TextField colsField = new TextField();
        colsField.setPromptText("Kolumny");
        TextField civField = new TextField();
        civField.setPromptText("Liczba Cywilizacji max 9 (opcjonalnie)");
        TextField seedField = new TextField();
        seedField.setPromptText("Podaj seed cyfrowy (opcjonalnie)");

        VBox inputBox = new VBox(10,
                new Label("Wiersze:"), rowsField,
                new Label("Kolumny:"), colsField,
                new Label("Liczba Cywilizacji:"), civField,
                new Label("Podaj seed cyfrowy (opcjonalnie):"), seedField
        );
        inputBox.setAlignment(Pos.CENTER);

        dialog.getDialogPane().setContent(inputBox);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                try {
                    int rows = rowsField.getText().isEmpty() ? 24 : Math.max(24, Integer.parseInt(rowsField.getText().trim()));
                    int cols = colsField.getText().isEmpty() ? 24 : Math.max(24, Integer.parseInt(colsField.getText().trim()));
                    int civs = civField.getText().isEmpty() ? 4 : Math.min(8, Integer.parseInt(civField.getText().trim()));
                    int seed = seedField.getText().isEmpty() ? -1 : Integer.parseInt(seedField.getText().trim());

                    return new int[]{rows, cols, civs, seed};
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        int[] parameters = dialog.showAndWait().orElse(null);
        if (parameters == null || parameters.length != 4) {
            System.out.println("Brak poprawnych danych wejściowych. Zamykanie aplikacji.");
            return;
        }


        int rows = parameters[0];
        int cols = parameters[1];
        int civs = parameters[2];
        String seed = Integer.toString(parameters[3]);


        // inicjalizacja GUI
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        // stop start button
        Button toggleButton = new Button("⏸ Stop");
        toggleButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 16 8 16;");
        toggleButton.setAlignment(Pos.CENTER_LEFT);
        toggleButton.setOnAction(e -> {
            if (simulationTimeline.getStatus() == Timeline.Status.RUNNING) {
                simulationTimeline.pause();
                toggleButton.setText("▶ Start");
            } else {
                simulationTimeline.play();
                toggleButton.setText("⏸ Stop");
            }
        });
        // pasek narzędzi - przycisk start/stop - przycisk zapisu

        HBox topBar = new HBox(toggleButton);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setStyle("-fx-padding: 10 0 0 10;");

        VBox gridWithButton = new VBox(10, gridPane);
        gridWithButton.setAlignment(Pos.CENTER);

        Region leftSpacer = new Region();
        leftSpacer.prefWidthProperty().bind(stage.widthProperty().multiply(0.1));

        HBox gridContainer = new HBox(leftSpacer, gridWithButton);
        gridContainer.setAlignment(Pos.CENTER);

        gridPane.prefWidthProperty().bind(stage.widthProperty().multiply(0.8));
        gridPane.prefHeightProperty().bind(stage.heightProperty().multiply(0.8));

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setLeft(gridContainer);

        statsPanel = new VBox(10);
        statsPanel.setStyle("-fx-padding: 20; -fx-border-color: black; -fx-border-width: 2;");
        statsPanel.setPrefWidth(300); // Stała szerokość panelu statystyk

        Symulacja initialSimulation = new Symulacja(rows, cols, civs, seed);
        createGrid(rows, cols, initialSimulation);

        // stopklatka - animacja
        simulationTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            initialSimulation.krokSymulacji();
            createGrid(rows, cols, initialSimulation);
        }));
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
        simulationTimeline.play();


        //zapis symulacji
        Button saveButton = new Button("💾 Zapisz");
        saveButton.setStyle("-fx-font-size: 16px; -fx-padding: 8 16 8 16;");
        toggleButton.setAlignment(Pos.CENTER_RIGHT);
        saveButton.setOnAction(e -> {
            saveSimulationToFile(initialSimulation);
        });
        HBox savebar = new HBox(saveButton);
        savebar.setAlignment(Pos.TOP_RIGHT);
        savebar.setStyle("-fx-padding: 10 0 0 10;");
        root.setTop(new HBox(topBar, savebar));
        root.setRight(statsPanel);

        Scene scene = new Scene(root, 1920, 1080);
        stage.setTitle("Civilization Battle Simulator");
        stage.setScene(scene);
        stage.show();
    }
    public void saveSimulationToFile(Symulacja symulacja) {
        String fileName = "symulacja.txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Symulacja Cywilizacji - Ilość tur - " + symulacja.licznikTur + "\n");
            writer.write("Liczba cywilizacji: " + symulacja.listaCywilizacji.length + "\n");
            writer.write("\n");
            for (Cywilizacja civ : symulacja.listaCywilizacji) {
                if (civ != null) {
                    writer.write("Cywilizacja: " + civ.nameCywilizacji + "\n");
                    writer.write("Liczba wojowników: " + civ.licznikWojownikow + "\n");
                    writer.write("Liczba osadników: " + civ.licznikOsadnikow + "\n");
                    writer.write("Liczba osad: " + civ.licznikOsad + "\n");
                    writer.write("Kamień: " + civ.surowce[0] + "\n");
                    writer.write("Drewno: " + civ.surowce[1] + "\n");
                    writer.write("ETCS: " + civ.surowce[2] + "\n");
                    writer.write("\n");
                }
            }
            System.out.println("Symulacja zapisana do pliku: " + fileName);
        } catch (Exception e) {
            System.err.println("Błąd podczas zapisywania symulacji do pliku: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        if (simulationTimeline != null) {
            simulationTimeline.stop();
        }
    }

    //kolory cywilizacji
    private String getCivilizationColor(int civId) {
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
        // czyszczenie starej siatki
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        updateStatsPanel(symulacja);

        gridPane.maxWidthProperty().bind(Bindings.min(gridPane.prefWidthProperty(), gridPane.prefHeightProperty()));
        gridPane.maxHeightProperty().bind(gridPane.maxWidthProperty());


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

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Region cell = new Region();
                cell.setStyle("-fx-border-color: black; -fx-background-color: white;");


                cell.prefWidthProperty().bind(Bindings.min(
                        gridPane.widthProperty().divide(cols),
                        gridPane.heightProperty().divide(rows)
                ));
                cell.prefHeightProperty().bind(cell.prefWidthProperty());

                Obiekt obiekt = symulacja.plansza.zwrocPole(i, j);
                String borderColor = "black";
                int borderWidth = 1;

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


                    if (obiekt instanceof Jednostka) {
                        int civId = ((Jednostka) obiekt).idCywilizacji;
                        borderColor = getCivilizationColor(civId);
                    }


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

                // surowce z obrazkami
                HBox surowceBox = new HBox(10);
                surowceBox.setAlignment(Pos.CENTER_LEFT);

                String[] imgPaths = {"/images/stone.png", "/images/drzewo.png", "/images/ETCS.png"};


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
                    surowceBox.getChildren().add(label);
                }

                // jednostki z obrazkami
                HBox jednostkiBox = new HBox(10);
                jednostkiBox.setAlignment(Pos.CENTER_LEFT);

                if (civ.idCywilizacji == 9) {
                    // Barbarzyńcy:
                    ImageView barbarzyncaIcon = new ImageView();
                    try {
                        barbarzyncaIcon.setImage(new Image(getClass().getResource("/images/barbarzynca.png").toExternalForm()));
                    } catch (Exception e) {
                    }
                    barbarzyncaIcon.setFitWidth(24);
                    barbarzyncaIcon.setFitHeight(24);
                    Label barbarzyncaLabel = new Label(String.valueOf(civ.licznikWojownikow), barbarzyncaIcon);
                    barbarzyncaLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
                    barbarzyncaLabel.setContentDisplay(ContentDisplay.LEFT);

                    jednostkiBox.getChildren().add(barbarzyncaLabel);
                } else {
                    // Wojownicy
                    ImageView wojownikIcon = new ImageView();
                    try {
                        wojownikIcon.setImage(new Image(getClass().getResource("/images/wojownik.png").toExternalForm()));
                    } catch (Exception e) {
                    }
                    wojownikIcon.setFitWidth(24);
                    wojownikIcon.setFitHeight(24);
                    Label wojownikLabel = new Label(String.valueOf(civ.licznikWojownikow), wojownikIcon);
                    wojownikLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
                    wojownikLabel.setContentDisplay(ContentDisplay.LEFT);


                    // Osadnicy
                    ImageView osadnikIcon = new ImageView();
                    try {
                        osadnikIcon.setImage(new Image(getClass().getResource("/images/osadnik.png").toExternalForm()));
                    } catch (Exception e) {
                    }
                    osadnikIcon.setFitWidth(24);
                    osadnikIcon.setFitHeight(24);
                    Label osadnikLabel = new Label(String.valueOf(civ.licznikOsadnikow), osadnikIcon);
                    osadnikLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
                    osadnikLabel.setContentDisplay(ContentDisplay.LEFT);


                    // Osady
                    ImageView osadaIcon = new ImageView();
                    try {
                        osadaIcon.setImage(new Image(getClass().getResource("/images/osada.png").toExternalForm()));
                    } catch (Exception e) {
                    }
                    osadaIcon.setFitWidth(24);
                    osadaIcon.setFitHeight(24);
                    Label osadaLabel = new Label(String.valueOf(civ.licznikOsad), osadaIcon);
                    osadaLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
                    osadaLabel.setContentDisplay(ContentDisplay.LEFT);
                    jednostkiBox.getChildren().addAll(wojownikLabel, osadnikLabel, osadaLabel);
                }

                statsPanel.getChildren().addAll(civName, surowceBox, jednostkiBox, new Separator());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("START");
        launch();
    }
}

