package com.garena.dnfmaster.controller;

import com.garena.dnfmaster.constant.MailType;
import com.garena.dnfmaster.pojo.Charac;
import com.garena.dnfmaster.pojo.Item;
import com.garena.dnfmaster.service.CharacService;
import com.garena.dnfmaster.service.MailService;
import com.garena.dnfmaster.util.AppContextUtils;
import com.garena.dnfmaster.util.DialogUtils;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.MapChangeListener;
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
import java.util.stream.Collectors;


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
        mailService = AppContextUtils.getBean(MailService.class);
        characService = AppContextUtils.getBean(CharacService.class);
    }

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
        nameColumn.setMinWidth(250);

        itemTableView.getTableColumns().addAll(idColumn, nameColumn);
        itemTableView.getFilters().addAll(
                new IntegerFilter<>("编号", Item::getId),
                new StringFilter<>("名称", Item::getName)
        );
        itemTableView.getItems().addAll(parseItems());

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
        setupTable();
        setupComboBoxes();
    }

    public void onMailClearButtonClicked() {
        mailService.clearMails();
        DialogUtils.showInfo("邮件操作", "已清除服务端所有的邮件记录");
    }

    public void onMailSendButtonClicked() {
        AccountPanelController accountPanelController = AppContextUtils.getBean(AccountPanelController.class);
        List<Charac> characters = accountPanelController.getSelectedCharacters();
        String commaSeperatedItemIds = itemIdTextField.getText();
        if (characters.isEmpty() || commaSeperatedItemIds.isEmpty()) {
            DialogUtils.showWarning("发送邮件", "请选择至少一个角色和输入至少一种物品之后再进行发送邮件操作");
            return;
        }

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
        for (Charac character : characters) {
            mailService.sendMails(character.getNo(), commaSeperatedItemIds, inputItemQuantity, inputGold, inputUpgrade, inputSeperateUpgrade, amplifyOption, sealOption, mailType);
        }

        List<String> characterNames = characters.stream().map(Charac::getName).collect(Collectors.toList());
        DialogUtils.showInfo("发送邮件", "邮件已成功发送到角色邮箱：" + characterNames);
    }

    public void onAvataAddButtonClicked() {
        AccountPanelController accountPanelController = AppContextUtils.getBean(AccountPanelController.class);
        List<Charac> characters = accountPanelController.getSelectedCharacters();
        String commaSeperatedItemIds = itemIdTextField.getText();
        if (characters.isEmpty() || commaSeperatedItemIds.isEmpty()) {
            DialogUtils.showWarning("添加装扮", "请选择至少一个角色和输入至少一种物品之后再进行发送邮件操作");
            return;
        }

        for (Charac character : characters) {
            characService.addAvata(character.getNo(), commaSeperatedItemIds);
        }
    }

    public void onCreatureAddButtonClicked() {
        AccountPanelController accountPanelController = AppContextUtils.getBean(AccountPanelController.class);
        List<Charac> characters = accountPanelController.getSelectedCharacters();
        String commaSeperatedItemIds = itemIdTextField.getText();
        if (characters.isEmpty() || commaSeperatedItemIds.isEmpty()) {
            DialogUtils.showWarning("添加宠物", "请选择至少一个角色和输入至少一种物品之后再进行发送邮件操作");
            return;
        }

        String[] options = {"宠物", "宠物蛋"};
        String result = DialogUtils.showChoiceDialog("添加宠物", "请选择宠物物品类型", "类型：", Arrays.asList(options));
        if (result != null) {
            boolean isEgg = options[1].equals(result);
            for (Charac character : characters) {
                characService.addCreature(character.getNo(), commaSeperatedItemIds, isEgg);
            }
        }

    }
}
