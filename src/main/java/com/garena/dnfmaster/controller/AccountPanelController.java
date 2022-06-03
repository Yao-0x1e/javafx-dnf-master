package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.common.AppContext;
import com.garena.dnfmaster.common.AppRegistry;
import com.garena.dnfmaster.constant.GrowType;
import com.garena.dnfmaster.dialog.DialogBuilder;
import com.garena.dnfmaster.pojo.Charac;
import com.garena.dnfmaster.service.AccountService;
import com.garena.dnfmaster.service.CharacService;
import com.garena.dnfmaster.service.EventService;
import com.garena.dnfmaster.service.GuildService;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class AccountPanelController implements Initializable {
    private final String[] valueOptions = {"D币", "D点", "时装代币", "普通技能点（SP）", "强化技能点（TP）", "任务技能点（QP）", "段位", "胜点", "胜场", "败场"};
    private final String[] growTypeOptions = {"未转职", "职业一", "职业二", "职业三", "职业四", "职业一（觉醒）", "职业二（觉醒）", "职业三（觉醒）", "职业四（觉醒）"};
    private final String[] expertJobOptions = {"无副职业", "附魔师", "炼金术士", "分解师", "控偶师"};
    private final String[] clearOptions = {"已接任务", "全部任务", "物品栏", "时装栏", "宠物栏"};
    private final String[] eventOptions = {"无限创建角色", "多倍爆率", "无限疲劳"};
    private final String[] otherOptions = {"全图地狱难度", "解除装备限制", "填充克隆装扮", "开启左右槽", "副职业满级", "无限负重", "公会满级"};

    @FXML
    private MFXTableView<Charac> characTableView;
    @FXML
    private MFXComboBox<String> valueComboBox;
    @FXML
    private MFXComboBox<String> growTypeComboBox;
    @FXML
    private MFXComboBox<String> expertJobComboBox;
    @FXML
    private MFXComboBox<String> clearComboBox;
    @FXML
    private MFXComboBox<String> eventComboBox;
    @FXML
    private MFXComboBox<String> otherComboBox;

    private final CharacService characService;
    private final AccountService accountService;
    private final EventService eventService;
    private final GuildService guildService;

    private TextInputDialog valueInputDialog;
    private TextInputDialog guildNameInputDialog;

    public AccountPanelController() {
        AppContext.addBean(AccountPanelController.class, this);
        characService = AppContext.getBean(CharacService.class);
        accountService = AppContext.getBean(AccountService.class);
        eventService = AppContext.getBean(EventService.class);
        guildService = AppContext.getBean(GuildService.class);
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

        Tooltip tooltip = new Tooltip("请通过鼠标点击选择列表中的单个角色，或者按住Ctrl再使用鼠标点击选择列表中的多个角色。");
        characTableView.setTooltip(tooltip);
    }

    private void setupDialogs() {
        Stage primaryStage = AppContext.getBean(Stage.class);
        Platform.runLater(() -> {
            valueInputDialog = DialogBuilder.buildTextInputDialog(primaryStage, "数值修改", "请输入修改后的数值", "数值：");
            guildNameInputDialog = DialogBuilder.buildTextInputDialog(primaryStage, "修改公会", "请输入公会名称", "名称：");
        });
    }

    private void setupComboBoxes() {
        List<Pair<MFXComboBox<String>, String[]>> pairs = Arrays.asList(
                new Pair<>(valueComboBox, valueOptions),
                new Pair<>(growTypeComboBox, growTypeOptions),
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
        setupDialogs();
    }

    public void setCharacters(List<Charac> characters) {
        ObservableList<Charac> observableList = characTableView.getItems();
        observableList.clear();
        observableList.addAll(characters);
    }

    public List<Charac> getSelectedCharacters() {
        return characTableView.getSelectionModel().getSelectedValues();
    }

    public synchronized void onValueButtonClicked() {
        int selectedIndex = valueComboBox.getSelectedIndex();
        Integer uid = AppRegistry.getValue("uid", Integer.class);
        List<Charac> characters = getSelectedCharacters();

        valueInputDialog.showAndWait().ifPresent(inputValue -> {
            if (selectedIndex == 0) {
                accountService.setCera(uid, inputValue);
            } else if (selectedIndex == 1) {
                accountService.setCeraPoint(uid, inputValue);
            } else if (selectedIndex == 2) {
                accountService.setAvataCoin(uid, inputValue);
            } else if (selectedIndex == 3) {
                characService.setSP(characters, inputValue);
            } else if (selectedIndex == 4) {
                characService.setTP(characters, inputValue);
            } else if (selectedIndex == 5) {
                characService.setQP(characters, inputValue);
            } else if (selectedIndex == 6) {
                characService.setPvpGrade(characters, inputValue);
            } else if (selectedIndex == 7) {
                characService.setPvpPoint(characters, inputValue);
            } else if (selectedIndex == 8) {
                characService.setPvpWin(characters, inputValue);
            } else if (selectedIndex == 9) {
                characService.setPvpLose(characters, inputValue);
            } else {
                assert false;
            }
        });
    }

    public synchronized void onGrowTypeButtonClicked() {
        List<Charac> characters = getSelectedCharacters();

        int selectedIndex = growTypeComboBox.getSelectedIndex();
        int job = selectedIndex <= 4 ? selectedIndex : GrowType.MIN_AWAKE_VALUE + (selectedIndex - 5);
        characService.setGrowType(characters, job);
    }

    public synchronized void onExpertJobButtonClicked() {
        List<Charac> characters = getSelectedCharacters();

        int expertJob = expertJobComboBox.getSelectedIndex();
        characService.setExpertJob(characters, expertJob);
    }

    public synchronized void onClearButtonClicked() {
        int selectedIndex = clearComboBox.getSelectedIndex();
        List<Charac> characters = getSelectedCharacters();

        if (selectedIndex == 0) {
            characService.clearQuests(characters);
        } else if (selectedIndex == 1) {
            characService.clearAllQuests(characters);
        } else if (selectedIndex == 2) {
            characService.clearInven(characters);
        } else if (selectedIndex == 3) {
            characService.clearAvatas(characters);
        } else if (selectedIndex == 4) {
            characService.clearCreatures(characters);
        } else {
            assert false;
        }
    }

    public synchronized void onEventButtonClicked() {
        int selectedIndex = eventComboBox.getSelectedIndex();
        if (selectedIndex == 0) {
            eventService.unlimitCharacterCreation();
        } else if (selectedIndex == 1) {
            eventService.enableMultipleDrops();
        } else if (selectedIndex == 2) {
            eventService.unlimitFatiguePoint();
        } else {
            assert false;
        }
    }

    public synchronized void onOtherButtonClicked() {
        int selectedIndex = otherComboBox.getSelectedIndex();

        List<Charac> characters = getSelectedCharacters();
        if (selectedIndex == 0) {
            Integer uid = AppRegistry.getValue("uid", Integer.class);
            accountService.unlockAllDungeons(uid);
        } else if (selectedIndex == 1) {
            characService.setMaxEquipLevel(characters);
        } else if (selectedIndex == 2) {
            characService.fillCloneAvatas(characters);
        } else if (selectedIndex == 3) {
            characService.unlockSlots(characters);
        } else if (selectedIndex == 4) {
            characService.setMaxExpertJobLevel(characters);
        } else if (selectedIndex == 5) {
            characService.setMaxInvenWeight(characters);
        } else if (selectedIndex == 6) {
            guildNameInputDialog.showAndWait().ifPresent(guildService::setMaxGuildProperties);
        } else {
            assert false;
        }
    }
}
