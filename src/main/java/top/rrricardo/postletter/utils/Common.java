package top.rrricardo.postletter.utils;

public class Common {
    /**
     * 从好友/群列表跳转前一刻写入，跳转后立即读出，其他情况不能使用
     */
    public static int sessionId = 0;

    /**
     * 从群列表点击查看群成员前一刻写入，弹出窗口后立即读出，其他情况不能使用
     */
    public static int permission = 0;

}
