package github.july_summer.julyitems.skills.customskills;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.ConfigManager;
import github.july_summer.julyitems.command.SubCommand;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.skills.*;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class LightSkill implements SkillExecute, SkillCustomLore {

    public static HashMap<Integer, Integer> entityIdMap = Maps.newHashMap();

    public LightSkill(){
        SkillManager.registerCommand(LightSkill.class);
        SkillManager.registerCustomLore("light", this);
    }

    @Override
    public boolean isDisplayLore() {
        return true;
    }

    @Override
    public String getSkillDisplayLore(SkillData data) {
        return ConfigManager.getValue("defaultDisplayLore.skill.customLore.chanceDisplayLore").toString().replace("{chance}", data.getData(0).toString());
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event) {
        if(event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
            Entity entity = damageByEntityEvent.getEntity();
            if(entity instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity)entity;
                if(Util.isChance(Util.objectToInteger(data.getData(0)))) {
                    LightningStrike light = livingEntity.getWorld().strikeLightning(livingEntity.getLocation());
                    entityIdMap.put(light.getEntityId(), Util.objectToInteger(data.getData(1)));

                    if(entity instanceof Player){
                        ((Player)entity).sendTitle((String) ConfigManager.getValue("skills.light.entityTitle"), "");
                    }
                    p.sendTitle((String) ConfigManager.getValue("skills.light.damagerTitle"), "");
                }
            }
        }
    }

    @SubCommand(cmd = "addSkill light <几率> <伤害>", msg = "添加闪电技能", checkArgs1 = 2, checkArgs2 = 1)
    public static void lightSkill(CommandSender sender, String[] args){
        if(!Util.isNumber(args[3]) || !Util.isNumber(args[4])){
            sender.sendMessage("§c几率/伤害 请填写整数");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        jitem.addSkill(args[2], SkillTrigger.ATTACK_ENTITY, new SkillData(new Object[]{args[3], args[4]}));
        sender.sendMessage("§a技能 " + SkillManager.skillDisplayNameMap.get("light") + " 添加成功");
    }

}
