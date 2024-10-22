package io.github.aplini.anvilFallingFix;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnvilFallingFix extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler
    public void onBlockBreak2(EntityDropItemEvent event) {

        // 要求掉落物属于铁砧
        if(!event.getItemDrop().getItemStack().getType().toString().endsWith("ANVIL")){
            return;
        }

        Location itemLocation = event.getEntity().getLocation();

        // 要求 y 轴约等于 0
        if(Math.abs(itemLocation.y()) >= 1e-10){
            return;
        }

        // 要求当前位置上的方块属于铁砧
        if(!itemLocation.getBlock().getType().toString().endsWith("ANVIL")){
            return;
        }

        Location nextBlockLocation = itemLocation.clone().subtract(0, 1, 0);
        Block nextBlock = nextBlockLocation.getBlock();

        // 要求下面一个方块属于铁砧
        if(!nextBlock.getType().toString().endsWith("ANVIL")){
            return;
        }

        // 取消掉落物
        event.setCancelled(true);

        // 复制下面一个方块到上方
        Bukkit.getScheduler().runTaskLater(this, () -> {
            itemLocation.setY(0);
            Block itemLocationBlock = itemLocation.getBlock();
            itemLocationBlock.setType(nextBlock.getType());
            itemLocationBlock.setBlockData(nextBlock.getBlockData());
        }, 1);

    }
}
