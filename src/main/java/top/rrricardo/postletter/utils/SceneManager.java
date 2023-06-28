package top.rrricardo.postletter.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rrricardo.postletter.PostLetter;

import java.io.IOException;
import java.util.Stack;

/**
 * 场景管理工具
 */
public class SceneManager {
    private static final JMetro s_jMetro = new JMetro();

    @Nullable
    private static Stage stage = null;
    private static final Stack<SceneNode> sceneStack = new Stack<>();

    /**
     * 设置项目舞台
     * @param stage 舞台
     */
    public static void setStage(@NotNull Stage stage) {
        SceneManager.stage = stage;
    }

    /**
     * 弹栈 并显示上一个场景
     */
    public static void popScene() {
        if (sceneStack.size() == 1) {
            throw new IllegalCallerException("内部状态栈仅有一个状态");
        }

        var item = sceneStack.peek();
        item.controller.close();

        sceneStack.pop();

        refresh();
    }

    /**
     * 清空栈
     * 替换为当前的场景
     * @param filename 腐朽没落文件名
     * @param width 窗口宽度
     * @param height 窗口高度
     * @param title 窗口标题
     */
    public static void replaceScene(String filename, int width, int height, String title) throws IOException{

        for (var item : sceneStack) {
            item.controller.close();
        }
        sceneStack.clear();     //清空栈

        var uri = PostLetter.class.getResource(filename);
        if (uri == null) {
            throw new IllegalArgumentException("腐朽没落文件不存在");
        }

        var loader = new FXMLLoader(uri);
        var newScene = new SceneNode(new Scene(loader.load(), width, height), title, loader.getController());

        sceneStack.push(newScene);
        newScene.controller.open();
        refresh();
    }

    /**
     * 创建一个新场景并压入栈中显示
     * @param filename 腐朽没落文件名
     * @param width 窗口宽度
     * @param height 窗口高度
     * @param title 窗口标题
     * @throws IOException 创建过程中的异常
     */
    public static void createScene(String filename, int width, int height, String title) throws IOException{

        var uri = PostLetter.class.getResource(filename);
        if (uri == null) {
            throw new IllegalArgumentException("腐朽没落文件不存在");
        }

        var loader = new FXMLLoader(uri);
        var newScene = new SceneNode(new Scene(loader.load(), width, height), title, loader.getController());
        sceneStack.push(newScene);
        newScene.controller.open();
        refresh();
    }

    /**
     * 在当前显示的窗口之上，再弹出一个新窗口
     * @param filename 腐朽没落文件名
     * @param width 窗口宽度
     * @param height   窗口高度
     * @param title 窗口标题
     * @throws IOException  创建过程中的异常
     */
    public static void showAnotherScene(String filename, int width, int height, String title) throws IOException{
        var uri = PostLetter.class.getResource(filename);
        if (uri == null) {
            throw new IllegalArgumentException("腐朽没落文件不存在");
        }

        var loader = new FXMLLoader(uri);
        Scene scene = new Scene(loader.load(), width, height);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(false);
        ((ControllerBase)loader.getController()).open();
        stage.showAndWait();
    }

    /**
     * 刷新显示当前栈顶的场景
     */
    private static void refresh() {
        var node = sceneStack.peek();

        if (stage == null) {
            throw new IllegalCallerException("没有设置stage");
        }
        s_jMetro.setScene(node.scene);
        stage.setScene(node.scene);
        stage.setTitle(node.title);
    }


    private record SceneNode(@NotNull Scene scene, @NotNull String title, ControllerBase controller) { }
}
