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
     * 设置初始场景
     * 会情况内部的状态栈
     * @param scene 场景
     * @param title 标题
     */
    public static void setPrimaryScene(@NotNull Scene scene, @NotNull String title) {
        sceneStack.clear();
        sceneStack.push(new SceneNode(scene, title));

        refresh();
    }

    /**
     * 将新场景压入栈中并显示
     * @param scene 场景
     * @param title 标题
     */
    public static void pushScene(@NotNull Scene scene, @NotNull String title) {
        sceneStack.push(new SceneNode(scene, title));

        refresh();
    }

    /**
     * 弹栈 并显示上一个场景
     */
    public static void popScene() {
        if (sceneStack.size() == 1) {
            throw new IllegalCallerException("内部状态栈仅有一个状态");
        }
        sceneStack.pop();

        refresh();
    }

    /**
     * 清空栈
     * 替换为当前的场景
     * @param scene 场景
     * @param title 标题
     */
    public static void replaceScene(@NotNull Scene scene, @NotNull String title) {
        sceneStack.clear();
        sceneStack.push(new SceneNode(scene, title));

        refresh();
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

    /**
     * 创建场景辅助函数
     * @param filename 腐朽没落文件路径
     * @param width 窗口宽度
     * @param height 窗口高度
     * @return 场景对象
     * @throws IOException 创建过程中的异常
     */
    @NotNull
    public static Scene createScene(@NotNull String filename, int width, int height) throws IOException {
        var uri = PostLetter.class.getResource(filename);
        if (uri == null) {
            throw new IllegalArgumentException("腐朽没落文件不存在");
        }

        var loader = new FXMLLoader(uri);
        return new Scene(loader.load(), width, height);
    }

    private record SceneNode(@NotNull Scene scene, @NotNull String title) { }
}
