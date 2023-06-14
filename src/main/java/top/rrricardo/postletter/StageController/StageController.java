package top.rrricardo.postletter.StageController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;

public class StageController {

    private HashMap<String, Stage> stages = new HashMap<>();


    public void addStage(String name,Stage stage){
        stages.put(name,stage);
    }

    public Stage getStage(String name){
        return stages.get(name);
    }

    public void setPrimaryStage(String primaryStageName, Stage primaryStage) {
        this.addStage(primaryStageName, primaryStage);
    }

    public boolean loadStage(String name, String resources, StageStyle... styles) {
        try {
            //加载FXML资源文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resources));
            Pane tempPane = loader.load();

            //通过Loader获取FXML对应的ViewCtr，并将本StageController注入到ViewCtr中
            IStageController controlledStage = loader.getController();
            controlledStage.setStageController(this);

            //构造对应的Stage
            Scene tempScene = new Scene(tempPane);
            Stage tempStage = new Stage();
            tempStage.setScene(tempScene);

            //配置initStyle
            for (StageStyle style : styles) {
                tempStage.initStyle(style);
            }

            //将设置好的Stage放到HashMap中
            this.addStage(name, tempStage);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 显示Stage但不隐藏任何Stage
     *
     * @param name 需要显示的窗口的名称
     * @return 是否显示成功
     */
    public boolean setStage(String name) {
        try{
            if(this.getStage(name)!=null){
                this.getStage(name).show();
                return true;
            }
            else {
                System.out.println("要设置的窗口不存在");
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 显示Stage并隐藏对应的窗口
     *
     * @param show  需要显示的窗口
     * @param close 需要删除的窗口
     * @return 是否显示成功
     */
    public boolean setStage(String show, String close) {
        try {
            if(getStage(close)!=null){
                getStage(close).close();
            }
            else {
                System.out.println("要关闭的窗口不存在");
                return false;
            }

            setStage(show);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 在Map中删除Stage加载对象
     *
     * @param name 需要删除的fxml窗口文件名
     * @return 是否删除成功
     */
    public boolean unloadStage(String name) {
        if (stages.remove(name) == null) {
            System.out.println("要移除的窗口不存在");
            return false;
        } else {
            System.out.println("窗口移除成功");
            return true;
        }
    }

}
