package com.garena.dnfmaster.controller;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import io.github.palexdev.materialfx.utils.ToggleUtils;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.garena.dnfmaster.util.ResourceUtils.loadURL;

public class MainPanelController implements Initializable {
    @FXML
    private HBox windowHeader;
    @FXML
    private MFXFontIcon closeIcon;
    @FXML
    private MFXFontIcon minimizeIcon;
    @FXML
    private VBox navBar;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private MFXScrollPane scrollPane;
    @FXML
    private StackPane contentPane;

    private double offsetX;
    private double offsetY;
    private final Stage stage;
    private final ToggleGroup toggleGroup;

    public MainPanelController(Stage stage) {
        this.stage = stage;
        this.toggleGroup = new ToggleGroup();
        ToggleUtils.addAlwaysOneSelectedSupport(toggleGroup);
    }

    private ToggleButton createToggle(String icon, String text) {
        MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text, wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(toggleGroup);
        return toggleNode;
    }

    private void initializeLoader() {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("DatabasePanel", loadURL("/fxml/DatabasePanel.fxml")).setBeanToNodeMapper(() -> createToggle("mfx-table", "数据库配置")).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("AccountPanel", loadURL("/fxml/AccountPanel.fxml")).setBeanToNodeMapper(() -> createToggle("mfx-user", "账号配置")).setDefaultRoot(false).get());
        loader.addView(MFXLoaderBean.of("ManagementPanel", loadURL("/fxml/ManagementPanel.fxml")).setBeanToNodeMapper(() -> createToggle("mfx-toggle-on", "管理员功能")).setDefaultRoot(false).get());
        loader.addView(MFXLoaderBean.of("MailPanel", loadURL("/fxml/MailPanel.fxml")).setBeanToNodeMapper(() -> createToggle("mfx-message", "邮件功能")).setDefaultRoot(false).get());
        loader.setOnLoadedAction(beans -> {
            List<ToggleButton> nodes = beans.stream().map(bean -> {
                ToggleButton toggle = (ToggleButton) bean.getBeanToNodeMapper().get();
                toggle.setOnAction(event -> contentPane.getChildren().setAll(bean.getRoot()));
                if (bean.isDefaultView()) {
                    contentPane.getChildren().setAll(bean.getRoot());
                    toggle.setSelected(true);
                }
                return toggle;
            }).collect(Collectors.toList());
            navBar.getChildren().setAll(nodes);
        });
        loader.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Platform.exit());
        minimizeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> ((Stage) rootPane.getScene().getWindow()).setIconified(true));

        windowHeader.setOnMousePressed(event -> {
            offsetX = stage.getX() - event.getScreenX();
            offsetY = stage.getY() - event.getScreenY();
        });
        windowHeader.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + offsetX);
            stage.setY(event.getScreenY() + offsetY);
        });

        initializeLoader();
        ScrollUtils.addSmoothScrolling(scrollPane);
    }
}
