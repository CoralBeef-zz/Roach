
package Roach;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HivemindConsoleFX extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane rootPane = new BorderPane();
        
        rootPane.setLeft(rootPane);
        
        Scene mainScene = new Scene(rootPane, 700, 500);
        stage.setTitle("Hivemind Console FX");
        stage.setScene(mainScene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
