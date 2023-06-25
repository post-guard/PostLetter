package top.rrricardo.postletter.utils;

public interface ControllerBase {
    default void close() {}
    default void open() {}
}
