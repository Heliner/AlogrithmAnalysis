import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class SwapUIController implements Initializable {
    static List<BaseSwapPage> cachedPageList = new ArrayList<>();
    static final HashMap<String, BaseSwapPage> pageSwapInstancePool = new HashMap<>();
    static int curPageIndex = -1;
    BaseSwapPage curSwapPageModel = null;
    String tempPageModel = null;

    @FXML
    public ListView leftViewList;
    @FXML
    public ListView rightViewList;
    @FXML
    public Label lostRatio;
    @FXML
    public Label lostPage;
    @FXML
    public Label serialize;
    @FXML
    public Label curPageIndexLabel;
    @FXML
    public Menu selectModelMenu;
    @FXML
    public TextField insertPageNumTextField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftViewList.setItems(FXCollections.observableList(Arrays.asList(1, 2, 2, 3, 4, 5, 6, 7, 8, 89, 1)));
        /**/
    }

    public void reInitial(String swapModel, int memPageSize) {
        /*清空当前页面缓存表*/
        cachedPageList.clear();

        if (pageSwapInstancePool.containsKey(swapModel)) {
            curSwapPageModel = pageSwapInstancePool.get("swapModel");
        } else {
            System.err.println("当前对象池中不存在对应的方法");
        }
        /*重置对象*/
        reInitialBaseSwapModel(curSwapPageModel, memPageSize);
        cachedPageList.add(curSwapPageModel);
        curPageIndex = 0;
    }

    void reInitialBaseSwapModel(BaseSwapPage baseSwapPage, int memPageSize) {
        baseSwapPage.lostPage = 0;
        /**/
        baseSwapPage.serialize = -1;
        baseSwapPage.init(memPageSize);
    }

    public void updateView() {
        BaseSwapPage curBaseSwapPage = cachedPageList.get(curPageIndex);

        serialize.setText("" + curBaseSwapPage.serialize);
        lostPage.setText("" + curBaseSwapPage.lostPage);
        lostRatio.setText("" + curBaseSwapPage.lostPage / curBaseSwapPage.serialize);
        curPageIndexLabel.setText("" + curPageIndex);
        /*更新tableListView*/
        leftViewList.getItems().setAll(curBaseSwapPage.cachedPage());
    }

    public void randomInsertPage() {
        curSwapPageModel.randomSwapPage();
        /*将当前保存到list中*/
        //TODO 深度复制对象
        cachedPageList.add(curSwapPageModel);
        curPageIndex++;
        updateView();
    }

    public void insertPage() {
        String pageNumString = insertPageNumTextField.getText();
        if ("".equals(pageNumString)) {
            setAlert("错误", "参数为空");
        } else {
            curSwapPageModel.swapOne(Integer.valueOf(pageNumString));
            cachedPageList.add(curSwapPageModel);
            curPageIndex++;
            updateView();
        }
    }

    private void setAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private FlowPane popUpThingPane;
    private Stage pageSizePage;
    private Scene pageSizeScene;
    private TextField pageSizeTextField;
    private Button confirmButton;
    private Button deleteButton;

    private void popUp2SelectCachePageSize() {
        confirmButton = new Button("确认");
        deleteButton = new Button("取消");


        pageSizeTextField = new TextField();
        pageSizeTextField.setPromptText("页面大小");

        popUpThingPane = new FlowPane();
        popUpThingPane.getChildren().addAll(pageSizeTextField, confirmButton, deleteButton);
        pageSizeScene = new Scene(popUpThingPane, 250, 250);
        pageSizePage = new Stage();
        pageSizePage.setScene(pageSizeScene);
        pageSizePage.show();


        confirmButton.setOnMouseClicked(e -> {
            String pageSizeString = pageSizeTextField.getText();
            if ("".equals(pageSizeString))
                setAlert("错误", "输入为空");
            else {
                Integer cachePageSize = Integer.valueOf(pageSizeString);
                if (cachePageSize != null) {
                    reInitial(tempPageModel, cachePageSize);
                } else {
                    setAlert("错误", "页面缓存大小不合理");
                }
            }
        });
        deleteButton.setOnMouseClicked(e -> {
            pageSizePage.close();
        });
        /*设置关闭窗口*/
        pageSizePage.setOnCloseRequest(e -> {
        });

    }


    public void ButtonClicked(ActionEvent e) {
        if (e.getSource() == confirmButton)
            pageSizePage.showAndWait();
        else
            pageSizePage.close();
    }

    @FXML
    void selectModelMenuItemClicked(ActionEvent e) {
        MenuItem menuItem = (MenuItem) e.getSource();
        tempPageModel = menuItem.getText();
        popUp2SelectCachePageSize();
    }
}


