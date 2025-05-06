package rpg.engine.cli;

import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;
import rpg.engine.common.Game;

class CommandLineUI {
    private TextInputDialog textInputDialog;

    protected CommandLineUI() {
        textInputDialog = new TextInputDialog();
        textInputDialog.setHeaderText("Command Line");
        textInputDialog.initOwner(Game.getInstance().getPrimaryStage());
        textInputDialog.setHeight(Game.getInstance().getPrimaryStage().getHeight() / 2);
        textInputDialog.setWidth(Game.getInstance().getPrimaryStage().getWidth() / 2);
        textInputDialog.initStyle(StageStyle.UNDECORATED);
    }

    public TextInputDialog getTextInputDialog() {
        return textInputDialog;
    }
}
