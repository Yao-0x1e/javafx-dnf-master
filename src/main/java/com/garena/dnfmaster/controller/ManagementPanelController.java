package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.pojo.Charac;
import com.garena.dnfmaster.service.AccountService;
import com.garena.dnfmaster.service.CharacService;
import com.garena.dnfmaster.service.EventService;
import com.garena.dnfmaster.util.AppContextUtils;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.util.Pair;

import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ManagementPanelController implements Initializable {
    @FXML
    private MFXTableView<Charac> characTableView;
    @FXML
    private MFXComboBox<String> valueComboBox;
    @FXML
    private MFXComboBox<String> jobComboBox;
    @FXML
    private MFXComboBox<String> expertJobComboBox;
    @FXML
    private MFXComboBox<String> clearComboBox;
    @FXML
    private MFXComboBox<String> eventComboBox;
    @FXML
    private MFXComboBox<String> otherComboBox;

    private final String[] valueOptions = {"点券", "普通技能点", "强化技能点", "任务点", "段位", "胜点", "胜场", "败场"};
    private final String[] jobOptions = {"未转职", "职业一", "职业二", "职业三", "职业四", "职业一（觉醒）", "职业二（觉醒）", "职业三（觉醒）", "职业四（觉醒）"};
    private final String[] expertJobOptions = {"无副职业", "附魔师", "炼金术士", "分解师", "控偶师"};
    private final String[] clearOptions = {"已接任务", "全部任务", "物品栏", "时装栏", "宠物栏"};
    private final String[] eventOptions = {"无限创建角色", "多倍爆率", "无限疲劳"};
    private final String[] otherOptions = {"无限负重", "公会满级", "开启左右槽", "副职业满级", "解除装备限制", "填充克隆装扮", "全图地狱难度"};

    private final CharacService characService;
    private final AccountService accountService;
    private final EventService eventService;

    public ManagementPanelController() {
        characService = AppContextUtils.getBean(CharacService.class);
        accountService = AppContextUtils.getBean(AccountService.class);
        eventService = AppContextUtils.getBean(EventService.class);
    }

    @SuppressWarnings("unchecked")
    private void setupTable() {
        MFXTableColumn<Charac> idColumn = new MFXTableColumn<>("编号", true, Comparator.comparing(Charac::getNo));
        MFXTableColumn<Charac> nameColumn = new MFXTableColumn<>("名称", true, Comparator.comparing(Charac::getName));
        MFXTableColumn<Charac> levelColumn = new MFXTableColumn<>("等级", true, Comparator.comparing(Charac::getLevel));

        idColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Charac::getNo));
        nameColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Charac::getName));
        levelColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Charac::getLevel));
        idColumn.setAlignment(Pos.CENTER_LEFT);
        nameColumn.setAlignment(Pos.CENTER_LEFT);
        levelColumn.setAlignment(Pos.CENTER_LEFT);

        characTableView.getTableColumns().addAll(idColumn, nameColumn, levelColumn);
        characTableView.getFilters().addAll(
                new IntegerFilter<>("编号", Charac::getNo),
                new StringFilter<>("名称", Charac::getName),
                new IntegerFilter<>("等级", Charac::getLevel)
        );
        characTableView.autosizeColumnsOnInitialization();
        characTableView.getSelectionModel().setAllowsMultipleSelection(true);
    }

    private void setupComboBoxes() {
        List<Pair<MFXComboBox<String>, String[]>> pairs = Arrays.asList(
                new Pair<>(valueComboBox, valueOptions),
                new Pair<>(jobComboBox, jobOptions),
                new Pair<>(expertJobComboBox, expertJobOptions),
                new Pair<>(clearComboBox, clearOptions),
                new Pair<>(eventComboBox, eventOptions),
                new Pair<>(otherComboBox, otherOptions)
        );

        pairs.forEach(pair -> {
            MFXComboBox<String> comboBox = pair.getKey();
            String[] options = pair.getValue();
            comboBox.getItems().addAll(options);
            comboBox.getSelectionModel().selectItem(options[0]);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        setupComboBoxes();
    }

    public void setCharacters(int uid, List<Charac> characters) {
        ObservableList<Charac> observableList = characTableView.getItems();
        observableList.clear();
        observableList.addAll(characters);
        characTableView.setUserData(uid);
    }

    public void onValueButtonClicked() {

    }

    public void onJobButtonClicked() {

    }

    public void onExpertJobButtonClicked() {

    }

    public void onClearButtonClicked() {

    }

    public void onEventButtonClicked() {

    }

    public void onOtherButtonClicked() {

    }
}
