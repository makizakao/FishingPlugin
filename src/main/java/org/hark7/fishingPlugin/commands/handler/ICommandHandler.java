package org.hark7.fishingPlugin.commands.handler;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * コマンドハンドラーインターフェース
 * <p>
 * 各コマンドの実行とタブ補完を処理するためのインターフェースです。
 */
public interface ICommandHandler {
    /**
     * このコマンドがオペレーターのみ使用可能かどうかを返します。
     *
     * @return オペレーターのみ使用可能ならtrue、そうでなければfalse
     */
    boolean useOnlyOp();

    /**
     * コマンドの実行処理を行います。
     *
     * @param player コマンドを実行するプレイヤー
     * @param args   コマンドの引数
     */
    void execute(Player player, String[] args);

    /**
     * コマンドのタブ補完を処理します。
     *
     * @param args コマンドの引数
     * @return タブ補完候補のリスト
     */
    List<String> tabComplete(String[] args);
}
