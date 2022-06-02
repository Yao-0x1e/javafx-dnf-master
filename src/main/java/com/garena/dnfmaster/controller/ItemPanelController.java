package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.common.AppContext;
import com.garena.dnfmaster.constant.MailType;
import com.garena.dnfmaster.pojo.Charac;
import com.garena.dnfmaster.pojo.Item;
import com.garena.dnfmaster.service.CharacService;
import com.garena.dnfmaster.service.MailService;
import com.garena.dnfmaster.util.DialogUtils;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.util.Pair;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.AsyncTaskExecutor;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class ItemPanelController implements Initializable {
    @FXML
    private MFXTableView<Item> itemTableView;
    @FXML
    private MFXComboBox<String> amplifyComboBox;
    @FXML
    private MFXComboBox<String> mailTypeComboBox;
    @FXML
    private MFXComboBox<String> sealComboBox;
    @FXML
    private MFXTextField itemIdTextField;
    @FXML
    private MFXTextField itemQuantityTextField;
    @FXML
    private MFXTextField goldTextField;
    @FXML
    private MFXTextField upgradeTextField;
    @FXML
    private MFXTextField seperateUpgradeTextField;


    private final String[] amplifyOptions = {"无红字", "体力", "精神", "力量", "智力"};
    private final String[] mailTypeOptions = {"普通邮件", "装扮邮件", "宠物邮件"};
    private final String[] sealOptions = {"无封装", "有封装"};

    private final MailService mailService;
    private final CharacService characService;

    public ItemPanelController() {
        mailService = AppContext.getBean(MailService.class);
        characService = AppContext.getBean(CharacService.class);
    }

    @SneakyThrows
    private ObservableList<Item> parseItems() {
        Resource resource = new ClassPathResource("raw/items.txt");
        List<String> lines = IOUtils.readLines(resource.getInputStream(), StandardCharsets.UTF_8);
        ObservableList<Item> items = FXCollections.observableArrayList();
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
        nameColumn.setMinWidth(250);

        itemTableView.getTableColumns().addAll(idColumn, nameColumn);
        itemTableView.getFilters().addAll(
                new IntegerFilter<>("编号", Item::getId),
                new StringFilter<>("名称", Item::getName)
        );

        // 异步读取物品数据再回到主线程写入
        AsyncTaskExecutor asyncTaskExecutor = AppContext.getBean(AsyncTaskExecutor.class);
        asyncTaskExecutor.submit(() -> {
            ObservableList<Item> items = parseItems();
            Platform.runLater(() -> itemTableView.setItems(items));
        });

        // 设置事件监听机制，由于鼠标事件失效，需要通过MapChangeListener来实现
        // 参考资料：https://github.com/palexdev/MaterialFX/issues/186
        MapChangeListener<Integer, Item> selectionListener = change -> updateItemIdTextField();
        itemTableView.getSelectionModel().selectionProperty().addListener(selectionListener);
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

    private void updateItemIdTextField() {
        List<Item> items = itemTableView.getSelectionModel().getSelectedValues();
        if (items.isEmpty()) {
            itemIdTextField.clear();
        } else {
            List<String> itemIds = new ArrayList<>(items.size());
            items.forEach(item -> itemIds.add(Integer.toString(item.getId())));
            String text = String.join(",", itemIds);
            itemIdTextField.setText(text);
        }
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupComboBoxes();
        setupTable();
    }

    public synchronized void onMailClearButtonClicked() {
        mailService.clearMails();
    }

    public synchronized void onMailSendButtonClicked() {
        AccountPanelController accountPanelController = AppContext.getBean(AccountPanelController.class);
        List<Charac> characters = accountPanelController.getSelectedCharacters();
        String commaSeperatedItemIds = itemIdTextField.getText();
        String inputItemQuantity = itemQuantityTextField.getText();
        String inputGold = goldTextField.getText();
        String inputUpgrade = upgradeTextField.getText();
        String inputSeperateUpgrade = seperateUpgradeTextField.getText();
        int amplifyOption = amplifyComboBox.getSelectedIndex();
        boolean sealOption = sealComboBox.getSelectedIndex() != 0;
        MailType mailType = null;
        int mailTypeSelectedIndex = mailTypeComboBox.getSelectedIndex();
        if (mailTypeSelectedIndex == 0) {
            mailType = MailType.REGULAR;
        } else if (mailTypeSelectedIndex == 1) {
            mailType = MailType.AVATA;
        } else if (mailTypeSelectedIndex == 2) {
            mailType = MailType.CREATURE;
        } else {
            assert false;
        }

        mailService.sendMails(characters, commaSeperatedItemIds, inputItemQuantity, inputGold, inputUpgrade, inputSeperateUpgrade, amplifyOption, sealOption, mailType);
    }

    public synchronized void onAvataAddButtonClicked() {
        AccountPanelController accountPanelController = AppContext.getBean(AccountPanelController.class);
        List<Charac> characters = accountPanelController.getSelectedCharacters();
        String commaSeperatedItemIds = itemIdTextField.getText();
        characService.addAvatas(characters, commaSeperatedItemIds);
    }

    public synchronized void onCreatureAddButtonClicked() {
        AccountPanelController accountPanelController = AppContext.getBean(AccountPanelController.class);
        List<Charac> characters = accountPanelController.getSelectedCharacters();
        String commaSeperatedItemIds = itemIdTextField.getText();

        String[] options = {"宠物", "宠物蛋"};
        String result = DialogUtils.showChoiceDialog("添加宠物", "请选择宠物物品类型", "类型：", Arrays.asList(options));
        if (result != null) {
            boolean isEgg = options[1].equals(result);
            characService.addCreatures(characters, commaSeperatedItemIds, isEgg);
        }
    }
}
