package cz.cuni.mff.ufal.textan.gui;

import cz.cuni.mff.ufal.textan.core.Client;
import cz.cuni.mff.ufal.textan.core.processreport.ProcessReportPipeline;
import cz.cuni.mff.ufal.textan.gui.reportwizard.ReportWizardStage;
import cz.cuni.mff.ufal.textan.gui.reportwizard.ReportWizardWindow;
import cz.cuni.mff.ufal.textan.gui.reportwizard.StateChangedListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;

/**
 * Controller for the TextAn application.
 */
public class TextAnController implements Initializable {

    /** Name of property controlling independence of windows. */
    static private final String INDEPENDENT_WINDOW = "windows.independent";

    /** Original title. */
    static protected final String TITLE = "TextAn";

    @FXML
    private BorderPane root;

    @FXML
    private Pane content;

    @FXML
    private CheckMenuItem menuItemIndependentWindows;

    @FXML
    private TextField loginTextField;

    /** Properties with application settings. */
    protected Properties settings = null;

    /** Property binded to stage titleProperty. */
    StringProperty titleProperty = new SimpleStringProperty(TITLE);

    /** List of children stages. */
    protected List<Stage> children = new ArrayList<>();

    /** Core client for the application.
     * It is created when settings are provided.
     */
    protected Client client = null;

    @FXML
    private void close() {
        Platform.exit();
    }

    @FXML
    private void independentWindows() {
        settings.setProperty(INDEPENDENT_WINDOW, menuItemIndependentWindows.isSelected() ? "true" : "false");
    }

    @FXML
    private void reportWizard() {
        final ProcessReportPipeline pipeline = client.createNewReportPipeline();
        StateChangedListener listener;
        if (settings.getProperty(INDEPENDENT_WINDOW, "false").equals("false")) {
            final ReportWizardWindow wizard = new ReportWizardWindow(settings);
            content.getChildren().add(wizard);
            listener = new StateChangedListener(settings, pipeline, wizard);
        } else {
            final ReportWizardStage stage = new ReportWizardStage(settings);
            children.add(stage);
            stage.showingProperty().addListener((ov, oldVal, newVal) -> {
                if (!newVal) {
                    children.remove(stage);
                }
            });
            listener = new StateChangedListener(settings, pipeline, stage);
            stage.show();
        }
        pipeline.addStateChangedListener(listener);
    }

    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        System.out.printf("Initializing...\n");
        content.addEventFilter(MouseEvent.ANY, (MouseEvent t) -> {
            if (t.getX()< 0 || t.getY() < 0
                    || t.getX() > content.getWidth()
                    || t.getY() > content.getHeight()) {
                t.consume();
            }
        });
    }

    /**
     * Sets TextAn settings.
     * @param settings new settings
     */
    public void setSettings(final Properties settings) {
        this.settings = settings;
        menuItemIndependentWindows.setSelected(
                settings.getProperty(INDEPENDENT_WINDOW, "false").equals("true"));
        loginTextField.setText(settings.getProperty("username", System.getProperty("user.name")));
        loginTextField.textProperty().addListener(
            (ObservableValue<? extends String> ov, String oldVal, String newVal) -> {
                settings.setProperty("username", newVal);
            }
        );
        client = new Client(settings);
    }

    /**
     * Returns title property.
     * @return title property
     */
    public StringProperty titleProperty() {
        return titleProperty;
    }

    /**
     * Returns window of the root.
     * @return window of the root
     */
    private javafx.stage.Window getWindow() {
        return root.getScene().getWindow();
    }

    /**
     * Called when application is stopped to store settings etc.
     */
    public void stop() {
        content.getChildren().stream()
                .filter(n -> n instanceof Window)
                .map(n -> (Window) n)
                .forEach(w -> w.close());
        //copy needed as closing removes stages from children
        for (Stage stage : new ArrayList<>(children)) {
            stage.close();
        }
    }
}
