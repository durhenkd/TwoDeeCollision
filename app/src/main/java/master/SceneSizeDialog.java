package master;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class SceneSizeDialog {

    public static SceneSize display(String title, String message) {
        Stage window = new Stage();
        SceneSize size = new SceneSize();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        TextField height = new TextField();
        TextField width = new TextField();
        Label heightLabel = new Label("height:");
        Label widthLabel = new Label("width:");
        HBox dataEntry = new HBox(10);
        dataEntry.getChildren().addAll(widthLabel, width, heightLabel, height);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Change Size");
        closeButton.setOnAction(e -> {
            height.setStyle("-fx-background-color:white;");
            width.setStyle("-fx-background-color:white;");
            if(validateInt(height, height.getText())&&validateInt(width, width.getText()))
            {
                size.setHeight(Integer.parseInt(height.getText()));
                size.setWidth(Integer.parseInt(width.getText()));
                window.close();
            }
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, dataEntry, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10,10,10,10));

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("style.css");
        window.setScene(scene);
        window.showAndWait();

        return size;
    }

    private static boolean validateInt(TextField field, String text){
        try {
            int value = Integer.parseInt(text);
            return true;
        }
        catch (NumberFormatException e){
            System.err.println("Invalidated input at SceneSizeDialog");
            field.setStyle("-fx-background-color:red;");
            return false;
        }
    }

}