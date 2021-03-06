package codes;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameOptionScene {

    int languageOption;
    Stage stage;
    Scene gameOptionScene;
    SceneBuilder sceneBuilder;
    GameAudio gameAudio;

    protected GameOptionScene(Stage stage) {
        this.stage = stage;
        sceneBuilder = new SceneBuilder();
        createGameOptionScene();
        gameAudio = new GameAudio();
    }

    protected void createGameOptionScene() {
        sceneBuilder.createMenuVBox(5);
        gameOptionScene = sceneBuilder.createScene();
    }

    protected void languageBasedWorks(int languageOption) {
        this.languageOption = languageOption;
        setLabelText();
        setButtonOneText();
        setButtonTwoText();
    }

    protected void setLabelText() {
        if (languageOption == 1) sceneBuilder.labelOne.setText("স্বাগতম!");
        else sceneBuilder.labelOne.setText("Welcome!");
    }

    protected void setButtonOneText() {
        if (languageOption == 1) sceneBuilder.buttonOne.setText("ডিফল্ট গেম");
        else sceneBuilder.buttonOne.setText("Default Game");
    }

    protected void setButtonTwoText() {
        if (languageOption == 1) sceneBuilder.buttonTwo.setText("গেমের সাথে ল্যানে চ্যাট");
        else sceneBuilder.buttonTwo.setText("Game with Chatting in LAN");
    }

    protected void initializeButtonEventWorks(GameScene gameScene, ClientServerSelectionScene clientServerSelectionScene) {
        sceneBuilder.buttonOne.setOnAction(e -> {
            buttonOneEventWorks(gameScene);
        });
        sceneBuilder.buttonTwo.setOnAction(e -> {
            buttonTwoEventWorks(gameScene, clientServerSelectionScene);
        });
    }

    private void buttonOneEventWorks(GameScene gameScene) {
        gameScene.getPaneOfGame().getChildren().removeAll(gameScene.getChatBox().getMessages(), gameScene.getChatBox().getInput(), gameScene.getChatBox().getMessagesLabel(), gameScene.getChatBox().getInputLabel(), gameScene.getChatBox().getMessagesLabelBackGround(), gameScene.getChatBox().getInputLabelBackGround());
        stage.setScene(gameScene.getSceneOfGame());
        gameAudio.callButtonClickedAudio();
        BoardListener boardListener = new BoardListener(stage, gameScene.getGameBoard(), gameScene.getPaneOfGame(), languageOption, this, null, false, false);
    }

    private void buttonTwoEventWorks(GameScene gameScene, ClientServerSelectionScene clientServerSelectionScene) {
        stage.setScene(clientServerSelectionScene.getClientServerSelectionScene());
        gameAudio.callButtonClickedAudio();
        ClientScene clientScene = new ClientScene(languageOption);
        ServerScene serverScene = new ServerScene(languageOption);
        clientServerSelectionScene.buttonEventHandler(stage, clientScene, serverScene);
        clientScene.ipFieldEventHandler(stage, gameScene, this);
        serverScene.gameButtonEventHandler(stage, gameScene, this);
    }

    protected Scene getGameOptionScene() {
        return gameOptionScene;
    }
}
