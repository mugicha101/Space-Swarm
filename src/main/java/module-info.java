module com.example.spaceswarm {
  requires javafx.controls;
  requires javafx.fxml;

  opens application to javafx.fxml;
  exports application;
  opens application.action to javafx.fxml;
  exports application.action;
  opens application.component to javafx.fxml;
  exports application.component;
  opens application.sprite to javafx.fxml;
  exports application.sprite;
  exports application.movement;
  opens application.movement to javafx.fxml;
  exports application.chunk;
  opens application.chunk to javafx.fxml;
}