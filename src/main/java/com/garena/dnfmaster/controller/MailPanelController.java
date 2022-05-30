package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.pojo.Item;
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
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class MailPanelController implements Initializable {
    @FXML
    private MFXTableView<Item> itemTableView;
    @FXML
    private MFXComboBox<String> amplifyComboBox;
    @FXML
    private MFXComboBox<String> mailTypeComboBox;
    @FXML
    private MFXComboBox<String> sealComboBox;

    private final String[] amplifyOptions = {"无红字", "体力", "精神", "力量", "智力"};
    private final String[] mailTypeOptions = {"普通邮件", "装扮邮件", "宠物邮件"};
    private final String[] sealOptions = {"是", "否"};

    private List<Item> parseItems() throws IOException {
        Resource resource = new ClassPathResource("raw/items.txt");
        List<String> lines = IOUtils.readLines(resource.getInputStream(), StandardCharsets.UTF_8);
        List<Item> items = new ArrayList<>(lines.size());
        for (String line : lines) {
            int commaIndex = line.indexOf(',');
            Item item = new Item();
            String id = line.substring(0, commaIndex);
            String name = line.substring(commaIndex + 1).strip();
            item.setId(Integer.parseInt(id));
            item.setName(name);
            items.add(item);
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    private void setupTable() throws IOException {
        MFXTableColumn<Item> idColumn = new MFXTableColumn<>("编号", true, Comparator.comparing(Item::getId));
        MFXTableColumn<Item> nameColumn = new MFXTableColumn<>("名称", true, Comparator.comparing(Item::getName));

        idColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Item::getId));
        nameColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Item::getName));
        idColumn.setAlignment(Pos.CENTER_LEFT);
        nameColumn.setAlignment(Pos.CENTER_LEFT);
        idColumn.setMinWidth(100);
        nameColumn.setMinWidth(300);

        itemTableView.getTableColumns().addAll(idColumn, nameColumn);
        itemTableView.getFilters().addAll(
                new IntegerFilter<>("编号", Item::getId),
                new StringFilter<>("名称", Item::getName)
        );
        itemTableView.getItems().addAll(parseItems());
    }

    private void setupComboBoxes() {
        List<Pair<MFXComboBox<String>, String[]>> pairs = Arrays.asList(
                new Pair<>(amplifyComboBox, amplifyOptions),
                new Pair<>(mailTypeComboBox, mailTypeOptions),
                new Pair<>(sealComboBox, sealOptions)
        );

        pairs.forEach(pair -> {
            MFXComboBox<String> comboBox = pair.getKey();
            String[] options = pair.getValue();
            comboBox.getItems().addAll(options);
            comboBox.getSelectionModel().selectItem(options[0]);
        });
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        setupComboBoxes();
    }

    public void onClearButtonClicked() {
    }

    public void onSendButtonClicked() {

    }
}
