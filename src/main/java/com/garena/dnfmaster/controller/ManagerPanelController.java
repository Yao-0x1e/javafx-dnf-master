package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.pojo.Charac;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.util.Pair;

import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerPanelController implements Initializable {
    @FXML
    private MFXTableView<Charac> characTableView;
    @FXML
    private MFXComboBox<String> valComboBox;
    @FXML
    private MFXComboBox<String> jobComboBox;
    @FXML
    private MFXComboBox<String> clearComboBox;
    @FXML
    private MFXComboBox<String> eventComboBox;
    @FXML
    private MFXComboBox<String> otherComboBox;
    
    private final String[] valOptions = {"点券", "普通技能点", "强化技能点", "任务点", "胜场", "败场", "胜点", "段位"};
    private final String[] jobOptions = {"未转职", "职业一", "职业二", "职业三", "职业四", "职业一（觉醒）", "职业二（觉醒）", "职业三（觉醒）", "职业四（觉醒）", "无副职业", "炼金术师", "附魔师", "控偶师", "分解师"};
    private final String[] clearOptions = {"已接任务", "全部任务", "背包", "装扮", "宠物"};
    private final String[] eventOptions = {"无限创建角色", "多倍爆率", "无限疲劳"};
    private final String[] otherOptions = {"无限负重", "公会满级", "开启左右槽", "副职业满级", "解除装备限制", "填充克隆装扮", "全图地狱难度"};

    @SuppressWarnings("unchecked")
    private void setupTable() {
        MFXTableColumn<Charac> idColumn = new MFXTableColumn<>("编号", true, Comparator.comparing(Charac::getId));
        MFXTableColumn<Charac> nameColumn = new MFXTableColumn<>("名称", true, Comparator.comparing(Charac::getName));
        MFXTableColumn<Charac> levelColumn = new MFXTableColumn<>("等级", true, Comparator.comparing(Charac::getLevel));

        idColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Charac::getId));
        nameColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Charac::getName));
        levelColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Charac::getLevel));
        idColumn.setAlignment(Pos.CENTER_LEFT);
        nameColumn.setAlignment(Pos.CENTER_LEFT);
        levelColumn.setAlignment(Pos.CENTER_LEFT);

        characTableView.getTableColumns().addAll(idColumn, nameColumn, levelColumn);
        characTableView.getFilters().addAll(
                new IntegerFilter<>("编号", Charac::getId),
                new StringFilter<>("名称", Charac::getName),
                new IntegerFilter<>("等级", Charac::getLevel)
        );
        characTableView.getItems().addAll(
                new Charac(1, "Sandaion", 70),
                new Charac(2, "Michion", 70),
                new Charac(3, "Kamion", 70),
                new Charac(4, "Hailon", 70),
                new Charac(5, "Sadion", 70),
                new Charac(6, "Zaphion", 70),
                new Charac(7, "Metaion", 70),
                new Charac(8, "Raphion", 70),
                new Charac(9, "Lazion", 70),
                new Charac(10, "Gabrion", 70)
        );
    }

    private void setupComboBoxes() {
        List<Pair<MFXComboBox<String>, String[]>> pairs = Arrays.asList(
                new Pair<>(valComboBox, valOptions),
                new Pair<>(jobComboBox, jobOptions),
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
        characTableView.autosizeColumnsOnInitialization();
    }
}
